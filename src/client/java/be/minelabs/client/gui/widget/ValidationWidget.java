package be.minelabs.client.gui.widget;

import be.minelabs.Minelabs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ValidationWidget extends ClickableWidget {
    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/validation.png");

    private final TooltipSupplier tooltipSupplier;
    private final StatusSupplier statusSupplier;

    public ValidationWidget(int x, int y, TooltipSupplier tooltipSupplier, StatusSupplier statusSupplier) {
        super(x, y, 16, 16, Text.of(""));
        this.tooltipSupplier = tooltipSupplier;
        this.statusSupplier = statusSupplier;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.drawTexture(matrices, TEXTURE, getX(), getY(), 0, getV(), 0, width, height, 256, 256);
        if (isHovered())
            setTooltip(tooltipSupplier.getTooltip(statusSupplier.getStatus()));
    }

    private int getV() {
        return statusSupplier.getStatus() * 17;
    }

    private void setTooltip(List<Text> tooltip) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen != null) {
            screen.setTooltip(tooltip.stream().map(Text::asOrderedText).toList(), getTooltipPositioner(), isFocused());
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    public interface TooltipSupplier {
        List<Text> getTooltip(int status);
    }

    public interface StatusSupplier {
        int getStatus();
    }
}
