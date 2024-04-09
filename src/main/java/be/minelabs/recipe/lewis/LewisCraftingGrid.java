package be.minelabs.recipe.lewis;

import be.minelabs.Minelabs;
import be.minelabs.science.Atom;
import be.minelabs.recipe.molecules.Bond;
import be.minelabs.recipe.molecules.MoleculeItemGraph;
import be.minelabs.recipe.molecules.PartialMolecule;
import be.minelabs.inventory.OrderedInventory;
import be.minelabs.item.items.AtomItem;
import be.minelabs.util.Graph;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LewisCraftingGrid extends OrderedInventory {

    protected PartialMolecule currentMolecule = new PartialMolecule();

    private final int width;
    private final int height;

    public LewisCraftingGrid(int width, int height) {
        super(width * height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void readNbtList(NbtList nbtList) {
        super.readNbtList(nbtList);
        currentMolecule = buildPartialMoleculeFromInventory();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        currentMolecule = buildPartialMoleculeFromInventory();
    }

    public PartialMolecule getPartialMolecule() {
        return currentMolecule;
    }

    public int toSlot(int x, int y) {
        assert x >= 0 && x < width;
        assert y >= 0 && y < height;
        return y * width + x;
    }

    private Collection<Integer> getNeighbourPositions(int x, int y) {
        List<Integer> slots = new ArrayList<>();
        if (x > 0) slots.add(toSlot(x - 1, y));
        if (x < width - 1) slots.add(toSlot(x + 1, y));
        if (y > 0) slots.add(toSlot(x, y - 1));
        if (y < height - 1) slots.add(toSlot(x, y + 1));
        return slots;
    }

    /**
     * Creates a topological graph based on the crafting grid. Intended to simplify traversal.
     */
    private MoleculeItemGraph getPositionGraph() {
        MoleculeItemGraph graph = new MoleculeItemGraph();

        // First build vertices
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ItemStack stack = getStack(toSlot(x, y));
                // We assume only AtomItems make it into the LCT.
                if (stack.isEmpty()) continue;
                Atom atom = ((AtomItem) stack.getItem()).getAtom();
                graph.addVertex(atom, stack);
            }
        }

        // Then connect them
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ItemStack stack = getStack(toSlot(x, y));
                if (stack.isEmpty()) continue;
                MoleculeItemGraph.Vertex from = graph.getVertexOfItemStack(stack);
                for (int slot : getNeighbourPositions(x, y)) {
                    ItemStack neighbourStack = getStack(slot);
                    if (neighbourStack.isEmpty()) continue;
                    MoleculeItemGraph.Vertex to = graph.getVertexOfItemStack(neighbourStack);
                    try {
                        graph.addEdge(from, to, Bond.COVALENT_ZERO);
                    } catch (IllegalArgumentException e) {
                        // edges are added twice due to iteration -> just ignore.
                    }
                }
            }
        }

        return graph;
    }

    private PartialMolecule buildPartialMoleculeFromInventory() {
        MoleculeItemGraph positionGraph = getPositionGraph();

        // debug
//        Minelabs.LOGGER.info("Position graph\n" + positionGraph.toString());

        // We use MoleculeItemGraph which can keep a mapping between vertices and ItemStacks.
        MoleculeItemGraph structure = positionGraphToMoleculeItemGraph(positionGraph);
//        Minelabs.LOGGER.info("Molecule formed: " + structure.toString());
        return new PartialMolecule(structure);
    }

    /**
     * Make the actual bonds between elements.
     */
    protected MoleculeItemGraph positionGraphToMoleculeItemGraph(MoleculeItemGraph structure) {
        List<Atom> sourceOrder = Stream.of("H", "F", "Cl", "Br", "I", "S", "N", "C", "B", "P", "O", "Si", "Al", "Sn", "Pb").map(Atom::getBySymbol).toList();
        List<Atom> targetOrder = Stream.of("C", "N", "O", "B", "S", "P", "Si", "Al", "Sn", "Pb", "I", "Br", "Cl", "F", "H").map(Atom::getBySymbol).toList();

        for (Atom sourceAtom : sourceOrder) {
            boolean changed = true;
            while (changed) {
                changed = false;
                for (MoleculeItemGraph.Vertex source : structure.getVertices()) {
                    if (source.data != sourceAtom) continue;
                    if (structure.getOpenConnections(source) < 1) continue;

                    Map<MoleculeItemGraph.Vertex, Integer> openConnectionsMap = source.getNeighbours().stream()
                            .filter(t -> structure.getOpenConnections(t) > 0)
                            .collect(Collectors.toMap(Function.identity(), structure::getOpenConnections));

                    Map<MoleculeItemGraph.Vertex, Bond> edgeDataMap = openConnectionsMap.keySet().stream()
                            .collect(Collectors.toMap(Function.identity(), o -> structure.getEdge(source, o).data));

                    MoleculeItemGraph.Vertex target = openConnectionsMap.entrySet().stream()
                            .sorted(Comparator.comparingInt(entry -> edgeDataMap.get(entry.getKey()).bondOrder))
                            .min(Comparator.comparingInt(entry -> targetOrder.indexOf(((AtomItem) entry.getKey().data.getItem()).getAtom())))
                            .map(Map.Entry::getKey)
                            .orElse(null);
                    if (target == null) continue;
                    changed = changed || structure.incrementBond(source, target);
                }
            }
        }

        structure.removeZeroBonds();
        return structure;
    }

}
