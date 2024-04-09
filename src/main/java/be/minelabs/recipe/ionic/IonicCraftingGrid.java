package be.minelabs.recipe.ionic;


import be.minelabs.recipe.lewis.LewisCraftingGrid;
import be.minelabs.recipe.molecules.MoleculeItemGraph;
import net.minecraft.item.ItemStack;

public class IonicCraftingGrid extends LewisCraftingGrid {

    public IonicCraftingGrid(int width, int height) {
        super(width, height);
    }

    /**
     * Get the charge of the current "Partial" molecule
     *
     * @return integer value
     */
    public int getCharge() {
        // TODO BETER CALC FOR EXCEPTIONS (Fe, S, ...)
        return this.currentMolecule.getStructure().getTotalOpenConnections();
    }

    @Override
    protected MoleculeItemGraph positionGraphToMoleculeItemGraph(MoleculeItemGraph structure) {
        // TODO new ALGORITHM?
        return super.positionGraphToMoleculeItemGraph(structure);
    }
}
