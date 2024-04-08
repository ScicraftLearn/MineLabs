package be.minelabs.client.integration.rei.categories;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.client.integration.rei.displays.LewisDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;

import java.util.LinkedList;
import java.util.List;

public class LewisCategory implements DisplayCategory<BasicDisplay> {

    public static final CategoryIdentifier<LewisDisplay> MOLECULE_CRAFTING =
            CategoryIdentifier.of(Minelabs.MOD_ID, "molecule_crafting");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return MOLECULE_CRAFTING;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("rei.category.minelabs.molecule_crafting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.LEWIS_BLOCK.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        // https://www.youtube.com/watch?v=HbZ6ocABo-M minute 7...
        LewisDisplay disp = (LewisDisplay) display;

        // TODO MOLECULE GRAPH -> SLOT INDEX
        final Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 60);
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createRecipeBase(bounds));

        for (int m = 0; m < 5; ++m) {
            for (int l = 0; l < 5; ++l) {
                int index = l + m * 5;
                if (index < display.getInputEntries().size() - 1) {
                    widgets.add(Widgets.createSlot(new Point(startPoint.x + 8 + l * 18, startPoint.y + m * 18 + 13))
                            .entries(display.getInputEntries().get(index))
                            .markInput());
                } else {
                    widgets.add(Widgets.createSlot(
                            new Point(startPoint.x + 8 + l * 18, startPoint.y + m * 18 + 13)));
                }
            }
        }

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 100, startPoint.y + 50))
                .animationDurationTicks(disp.getDuration()));

        Point result_point = new Point(startPoint.x + 134, startPoint.y + 50);
        widgets.add(Widgets.createResultSlotBackground(result_point));
        widgets.add(Widgets.createSlot(result_point)
                .disableBackground()
                .entries(display.getOutputEntries().get(0))
                .markOutput());

        Slot container_slot = Widgets.createSlot(new Point(startPoint.x + 134, startPoint.y + 85));
        if (!disp.getContainer().isEmpty()) {
            container_slot.entries(disp.getContainer());
        } else {
            // TODO render empty erlenmeyer slot
        }
        widgets.add(container_slot);

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 108;
    }

    @Override
    public int getDisplayWidth(BasicDisplay display) {
        return 180;
    }
}
