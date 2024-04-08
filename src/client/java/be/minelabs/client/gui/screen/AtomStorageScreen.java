package be.minelabs.client.gui.screen;

import be.minelabs.Minelabs;
import be.minelabs.screen.AtomStorageScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AtomStorageScreen extends HandledScreen<AtomStorageScreenHandler> implements ScreenHandlerProvider<AtomStorageScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(Minelabs.MOD_ID, "textures/gui/atom_storage/atom_storage.png");

    public AtomStorageScreen(AtomStorageScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundHeight = 274;
        backgroundWidth = 346;

        titleX = titleX + 78;
        titleY = titleY + 6;
        playerInventoryTitleY = this.backgroundHeight - 96;
        playerInventoryTitleX = playerInventoryTitleX + 76;
    }

    @Override
    public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight, 512, 512);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
