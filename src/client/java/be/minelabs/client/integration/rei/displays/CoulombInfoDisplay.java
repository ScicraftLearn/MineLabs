package be.minelabs.client.integration.rei.displays;

import be.minelabs.Minelabs;
import be.minelabs.client.integration.rei.categories.CoulombInfoCategory;
import be.minelabs.science.coulomb.CoulombData;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CoulombInfoDisplay extends BasicDisplay {
    private final CoulombData data;

    public CoulombInfoDisplay(CoulombData data, Identifier id) {
        super(new ArrayList<>(), new ArrayList<>());
        this.data = data;
        String[] split = id.getPath().split("/");
        String itemName = split[split.length - 1].split("\\.")[0];

        Item item = Registries.ITEM.get(new Identifier(Minelabs.MOD_ID, "subatomic/" + itemName));
        inputs.add(EntryIngredients.of(item));
        inputs.add(EntryIngredients.of(data.getAntiItem()));

        outputs.add(EntryIngredients.of(data.getAnnihilationDrop()));
        if (!data.stable){
            outputs.add(EntryIngredients.of(data.getDecayDrop()));
        }
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CoulombInfoCategory.COULOMB_INFO;
    }

    public CoulombData getData() {
        return data;
    }
}
