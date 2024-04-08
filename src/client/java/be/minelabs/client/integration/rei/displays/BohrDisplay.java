package be.minelabs.client.integration.rei.displays;

import be.minelabs.client.integration.rei.categories.BohrCategory;
import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BohrDisplay extends BasicDisplay {

    public BohrDisplay(AtomItem item) {
        super(List.of(
                EntryIngredients.of(new ItemStack(Items.ELECTRON, item.getAtom().getAtomNumber())),
                EntryIngredients.of(new ItemStack(Items.NEUTRON, item.getAtom().getInitialNeutrons())),
                EntryIngredients.of(new ItemStack(Items.PROTON, item.getAtom().getAtomNumber()))
        ), List.of(EntryIngredients.of(item)));

    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return BohrCategory.ATOM_CRAFTING;
    }
}
