package be.minelabs.client.gui.screen;

import be.minelabs.Minelabs;
import be.minelabs.client.gui.widget.CounterButtonWidget;
import be.minelabs.client.gui.widget.ValidationWidget;
import be.minelabs.item.items.AtomItem;
import be.minelabs.recipe.lewis.LewisCraftingGrid;
import be.minelabs.recipe.molecules.BondManager;
import be.minelabs.recipe.molecules.MoleculeItemGraph;
import be.minelabs.screen.IonicBlockScreenHandler;
import be.minelabs.util.Graph;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.*;

@Environment(EnvType.CLIENT)
public class IonicScreen extends HandledScreen<IonicBlockScreenHandler> implements ScreenHandlerProvider<IonicBlockScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/ionic_gui.png");

    private ButtonWidget clear_btn;

    private CounterButtonWidget left_minus;
    private CounterButtonWidget left_plus;
    private CounterButtonWidget right_minus;
    private CounterButtonWidget right_plus;

    //just the height and width of the texture
    public IonicScreen(IonicBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundHeight = 220;
        backgroundWidth = 205;
        playerInventoryTitleX = 22;
        playerInventoryTitleY = 127;
    }

    @Override
    protected void init() {
        super.init();

        registerButtons();
        activeBTN();
    }

    @SuppressWarnings("ConstantConditions")
    private void registerButtons() {
        clear_btn = ButtonWidget.builder(Text.of("C"), button -> {
            client.interactionManager.clickButton(handler.syncId, 0);
            button.setFocused(false);
        }).dimensions(x + 177, y + 80, 18, 18).build();

        int start_left = x + 21;
        int start_right = x + 96;
        int y_counter = y + 88;

        int offsest = 24;

        left_minus = new CounterButtonWidget(start_left, y_counter, CounterButtonWidget.Type.MINUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 1);
            button.setFocused(false);
        });

        left_plus = new CounterButtonWidget(start_left + offsest, y_counter, CounterButtonWidget.Type.PLUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 2);
            button.setFocused(false);
        });

        right_minus = new CounterButtonWidget(start_right, y_counter, CounterButtonWidget.Type.MINUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 3);
            button.setFocused(false);
        });

        right_plus = new CounterButtonWidget(start_right + offsest, y_counter, CounterButtonWidget.Type.PLUS, button -> {
            client.interactionManager.clickButton(handler.syncId, 4);
            button.setFocused(false);
        });

        addDrawableChild(clear_btn);
        addDrawableChild(left_minus);
        addDrawableChild(left_plus);
        addDrawableChild(right_minus);
        addDrawableChild(right_plus);
        addDrawable(new ValidationWidget(x + 177, y + 17, (status) -> switch (status) {
            case 0 -> List.of(Text.translatable("text.minelabs.invalid"));
            case 1 -> List.of(Text.translatable("text.minelabs.multiple_molecules"));
            case 2 -> List.of(
                    Text.translatable("text.minelabs.valid"),
                    Text.translatable("text.minelabs.not_implemented"));
            case 3 -> List.of(Text.translatable("text.minelabs.valid"));
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }, this.handler::getStatus));
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        renderProgressArrow(matrices, this.x, this.y);

        // Keep mapping between stack (in graph) and slots (for rendering)
        Map<ItemStack, Slot> stackToSlotMap = new HashMap<>();
        for (int i = 0; i < IonicBlockScreenHandler.GRIDSIZE * 2; i++) {
            stackToSlotMap.put(handler.getInventory().getStack(i), handler.getSlot(i));
        }

        /*
         * Draw Bonds on screen
         */
        renderBonds(matrices, getScreenHandler().getInventory().getLeftGrid(), stackToSlotMap);
        renderBonds(matrices, getScreenHandler().getInventory().getRightGrid(), stackToSlotMap);

        /*
         * Render input slot overlays
         */
        RenderSystem.enableBlend();
        DefaultedList<Ingredient> ingredients = handler.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack atom = ingredients.get(i).getMatchingStacks()[0];
            if (!this.handler.hasRecipe() || atom.isEmpty()) {
                break;
            }

            AtomItem atomItem = (AtomItem) atom.getItem();
            String atomId = atomItem.getAtom().name().toLowerCase();
            SpriteIdentifier spriteId = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Minelabs.MOD_ID, "item/" + atomId));

            if (handler.getInventory().getIO().getStack(i).getCount() < getCorrectAmount(i)) {
                drawSprite(matrices, x + 12 + 18 * i, y + 107, 0, 16, 16, spriteId.getSprite(), 0.2F, 0.2F, 0.2F, 0.8F);
                //this.itemRenderer.renderInGuiWithOverrides(matrices, new ItemStack(Items.RED_STAINED_GLASS_PANE), x + 12 + 18 * i, y + 107);
            } else {
                this.itemRenderer.renderInGuiWithOverrides(matrices, new ItemStack(Items.GREEN_STAINED_GLASS_PANE), x + 12 + 18 * i, 107 + y);
            }
        }

        matrices.push();
        matrices.scale(0.5f, 0.5f, 0.5f);
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack stack = ingredients.get(i).getMatchingStacks()[0];
            if (!this.handler.hasRecipe() || stack.isEmpty()) {
                break;
            }
            if (handler.getInventory().getIO().getStack(i).getCount() == 0) {
                textRenderer.draw(matrices, Text.of(String.valueOf(getCorrectAmount(i))),
                        2 * (x + 12 + 18 * i) + 20, 2 * (107 + y) + 23, 5592405);
            }
        }
        matrices.pop();

        RenderSystem.disableBlend();

        int charge = handler.getLeftCharge();
        this.textRenderer.draw(matrices, charge >= 0 ? "+" + charge : String.valueOf(charge), 70 + x, 20 + y, 0);

        charge = handler.getRightCharge();
        this.textRenderer.draw(matrices, charge >= 0 ? "+" + charge : String.valueOf(charge), 147 + x, 20 + y, 0);

        this.textRenderer.draw(matrices, Text.of(String.valueOf(handler.getLeftAmount())), x + 36, y + 91, 0x404040);
        this.textRenderer.draw(matrices, Text.of(String.valueOf(handler.getRightAmount())), x + 111, y + 91, 0x404040);
    }

    private void renderBonds(MatrixStack matrices, LewisCraftingGrid grid, Map<ItemStack, Slot> stackToSlotMap) {
        if (grid.getPartialMolecule().getStructure() instanceof MoleculeItemGraph graph) {
            BondManager manager = new BondManager();
            for (MoleculeItemGraph.Edge edge : graph.getEdges()) {
                Slot slot1 = stackToSlotMap.get(graph.getItemStackOfVertex(edge.getFirst()));
                Slot slot2 = stackToSlotMap.get(graph.getItemStackOfVertex(edge.getSecond()));
                BondManager.Bond bond = manager.getOrCreateBond(slot1, slot2, edge.data.bondOrder);
                this.itemRenderer.renderInGuiWithOverrides(matrices, bond.getStack(), bond.getX() + x, bond.getY() + y);
            }
        }
    }

    private void renderProgressArrow(MatrixStack matrices, int x, int y) {
        if (handler.isCrafting()) {
            drawTexture(matrices, x + 146, y + 45, 206, 0, handler.getScaledProgress(), 19);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean ret = super.mouseClicked(mouseX, mouseY, button);
        // button stays focussed after click which we don't want. This prevents it.
        if (ret)
            setFocused(null);
        return ret;
    }

    @Override
    protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
        super.drawMouseoverTooltip(matrices, x, y);
    }

    @Override
    protected void handledScreenTick() {
        activeBTN();

        clear_btn.setTooltip(Tooltip.of(handler.isInputEmpty() ?
                Text.translatableWithFallback("text.minelabs.clear_grid", "Clear Grid") :
                Text.translatableWithFallback("text.minelabs.clear_slots", "Clear Slots")));
    }

    private void activeBTN() {
        left_minus.active = handler.getLeftAmount() > 1;
        left_plus.active = handler.getLeftAmount() < 9;

        right_minus.active = handler.getRightAmount() > 1;
        right_plus.active = handler.getRightAmount() < 9;
    }

    private int getCorrectAmount(int index) {
        if (index < handler.getSplitIndex()) {
            return handler.getLeftDensity() * handler.getLeftAmount();
        }
        return handler.getRightDensity() * handler.getRightAmount();
    }
}
