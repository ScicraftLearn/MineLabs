package be.minelabs.client.integration.rei.categories;

import be.minelabs.Minelabs;
import be.minelabs.client.integration.rei.displays.CoulombInfoDisplay;
import be.minelabs.item.Items;
import be.minelabs.science.coulomb.CoulombData;
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
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CoulombInfoCategory implements DisplayCategory<BasicDisplay> {

    public static final CategoryIdentifier<CoulombInfoDisplay> COULOMB_INFO =
            CategoryIdentifier.of(Minelabs.MOD_ID, "coulomb_info");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return COULOMB_INFO;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("rei.category.minelabs.coulomb_info");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Items.ELECTRON.getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getBounds().getMinX(), bounds.getBounds().getMinY());
        CoulombData data = ((CoulombInfoDisplay) display).getData();
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 10, startPoint.y + 10))
                .entries(display.getInputEntries().get(0)));

        widgets.add(Widgets.createLabel(new Point(startPoint.x + 55, startPoint.y + 10),
                Text.translatable("text.minelabs.coulomb_info.charge").append(data.charge.toString())).leftAligned());

        widgets.add(Widgets.createLabel(new Point(startPoint.x + 55, startPoint.y + 20),
                Text.translatable("text.minelabs.coulomb_info.mass").append(data.mass.toString())).leftAligned());

        widgets.add(Widgets.createLabel(new Point(startPoint.x + 55, startPoint.y + 30),
                data.stable ? Text.translatable("text.minelabs.coulomb_info.stable") : Text.translatable("text.minelabs.coulomb_info.unstable")).leftAligned());

        widgets.add(Widgets.createLabel(new Point(startPoint.x + 10, startPoint.y + 50),
                Text.of("======================")).color(0xffffff).leftAligned());

        widgets.add(Widgets.createLabel(new Point(startPoint.x + 10, startPoint.y + 65),
                Text.translatable("text.minelabs.coulomb_info.annihilation")).leftAligned());

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 10, startPoint.y + 80))
                .entries(display.getInputEntries().get(0)));
        // TODO TEXTURE: PLUS SIGN
        //widgets.add(Widgets.createTexturedWidget(new Identifier(Minelabs.MOD_ID,""), new Rectangle())); // Need a plus texture
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 40, startPoint.y + 80))
                .entries(display.getInputEntries().get(1)).markInput());
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 80)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 90, startPoint.y + 80))
                .entries(display.getOutputEntries().get(0)));


        if (!data.stable) {
            widgets.add(Widgets.createLabel(new Point(startPoint.x + 10, startPoint.y + 100),
                    Text.translatable("text.minelabs.coulomb_info.decay")).leftAligned());

            widgets.add(Widgets.createSlot(new Point(startPoint.x + 10, startPoint.y + 115))
                    .entries(display.getInputEntries().get(1)));

            widgets.add(Widgets.createArrow(new Point(startPoint.x + 30, startPoint.y + 115)));
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 60, startPoint.y + 115))
                    .entries(display.getOutputEntries().get(1)));

            widgets.add(Widgets.createLabel(new Point(startPoint.x + 80, startPoint.y + 115),
                    Text.translatable("text.minelabs.coulomb_info.chance")).leftAligned());
            widgets.add(Widgets.createLabel(new Point(startPoint.x + 90, startPoint.y + 125),
                    Text.of(data.decay_chance * 100 + "%")).leftAligned());
        }

        return widgets;
    }

    @Override
    public int getMaximumDisplaysPerPage() {
        return 1;
    }

    @Override
    public int getDisplayHeight() {
        return 150;
    }
}
