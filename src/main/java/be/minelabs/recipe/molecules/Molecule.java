package be.minelabs.recipe.molecules;

import net.minecraft.item.Item;

public class Molecule extends PartialMolecule {
    private final Item item;

    public Molecule(MoleculeGraph structure, Item item) {
        super(structure);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
