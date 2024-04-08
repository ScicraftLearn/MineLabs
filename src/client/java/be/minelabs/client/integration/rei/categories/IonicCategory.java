package be.minelabs.client.integration.rei.categories;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.client.integration.rei.displays.IonicDisplay;
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
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class IonicCategory implements DisplayCategory<BasicDisplay> {
    public static final CategoryIdentifier<IonicDisplay> IONIC_CRAFTING =
            CategoryIdentifier.of(Minelabs.MOD_ID, "ionic_crafting");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return IONIC_CRAFTING;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("rei.category.minelabs.ionic_crafting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.IONIC_BLOCK.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        List<Widget> widgets = new LinkedList<>();
        final Point startPoint = new Point(bounds.getCenterX() - 105, bounds.getCenterY() - 45);
        IonicDisplay disp = (IonicDisplay) display;

        widgets.add(Widgets.createRecipeBase(bounds));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                widgets.add(Widgets.createSlot(new Point(startPoint.x + j * 18 + 18, startPoint.y + i * 18 + 8)));
                // TODO LEFT ITEMS/ENTRIES
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                widgets.add(Widgets.createSlot(new Point(startPoint.x + j * 18 + 85, startPoint.y + i * 18 + 8)));
                // TODO RIGHT ITEMS/ENTRIES
            }
        }

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 144, startPoint.y + 26)).animationDurationTicks(disp.getDuration()));

        Point output_point = new Point(startPoint.x + 176, startPoint.y + 26);
        widgets.add(Widgets.createResultSlotBackground(output_point));
        widgets.add(Widgets.createSlot(output_point)
                .disableBackground()
                .entries(display.getOutputEntries().get(0)));

        Slot container_slot = Widgets.createSlot(new Point(startPoint.x + 176, startPoint.y + 72));
        if (!disp.getContainer().isEmpty()) {
            container_slot.entries(disp.getContainer());
        } else {
            // TODO render "empty" erlenmeyer slot
        }
        widgets.add(container_slot);

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 110;
    }

    @Override
    public int getDisplayWidth(BasicDisplay display) {
        return 200;
    }
}
