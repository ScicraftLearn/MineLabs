package be.minelabs.client.integration.rei.categories;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.client.integration.rei.displays.BohrDisplay;
import be.minelabs.item.Items;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class BohrCategory implements DisplayCategory<BasicDisplay> {
    public static final CategoryIdentifier<BohrDisplay> ATOM_CRAFTING =
            CategoryIdentifier.of(Minelabs.MOD_ID, "atom_crafting");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return ATOM_CRAFTING;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("rei.category.minelabs.atom_crafting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.BOHR_BLUEPRINT.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getBounds().getMinX(), bounds.getBounds().getMinY());
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 70, startPoint.y + 10))
                .entries(display.getInputEntries().get(0))
                .markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 70, startPoint.y + 30))
                .entries(display.getInputEntries().get(1))
                .markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 70, startPoint.y + 50))
                .entries(display.getInputEntries().get(2))
                .markInput());

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 90, startPoint.y + 30)));

        Point result_point = new Point(startPoint.x + 125, startPoint.y + 30);
        widgets.add(Widgets.createResultSlotBackground(result_point));
        widgets.add(Widgets.createSlot(result_point)
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());

        Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
            // TODO
        });

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 80;
    }

    @Override
    public int getDisplayWidth(BasicDisplay display) {
        return 160;
    }
}
