package be.minelabs.client.gui.widget;

import be.minelabs.Minelabs;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CounterButtonWidget extends ButtonWidget {

    private static final int WIDTH = 22; // Combo button with
    private static final int HEIGHT = 12; // Combo button height

    private static final Identifier TEX = new Identifier(Minelabs.MOD_ID, "textures/gui/counter_buttons.png");

    private final Type type;

    public CounterButtonWidget(int x, int y, Type type, PressAction onPress) {
        super(x, y, WIDTH / 2, HEIGHT, type.text, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.type = type;
    }

    // Based on PressableWidget
    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

        if (this.visible) {
            int u = active && this.isHovered() ? WIDTH : 0;
            int v = active ? 0 : HEIGHT;

            RenderSystem.setShaderTexture(0, TEX);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            drawTexture(matrices, this.getX(), this.getY(), u + (type == Type.PLUS ? WIDTH / 2 : 0), v, WIDTH / 2, HEIGHT);
        }
    }

    public enum Type {
        PLUS(Text.literal("+")),
        MINUS(Text.literal("-"));

        final Text text;

        Type(Text text) {
            this.text = text;
        }
    }
}
