package be.minelabs.state.property;

import be.minelabs.block.blocks.CornerShape;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;

public class Properties {
    //public static final EnumProperty<BohrPart> BOHR_PART = EnumProperty.of("part", BohrPart.class);
    public static final BooleanProperty MASTER = BooleanProperty.of("master");
    public static final BooleanProperty CLOUD = BooleanProperty.of("cloud");
    public static final IntProperty FIRE_COLOR = IntProperty.of("fire_color", 0, 10);

    public static final BooleanProperty ZOOMED = BooleanProperty.of("zoom");

    //USE WITH INTENT OF ADDING CUSTOM MODELS FOR COUNTERED BLOCKS
    // 0 : default (no offset)
    // 1 : Center Lab block (1 pixel offset) -> float offset = 0.0625f
    // 2 : Lab Block (2 pixel offset)        -> float offset = 0.1250f
    public static final IntProperty COUNTER = IntProperty.of("counter", 0, 2);

    // 0 : On the floor
    // 1 : Diagonal behind
    // 2 : Vertical
    public static final IntProperty TYPE = IntProperty.of("type", 0, 2);

    public static final BooleanProperty FILLED = BooleanProperty.of("filled");
    public static final BooleanProperty OXYGENATED = BooleanProperty.of("oxygenated");
    public static final EnumProperty<CornerShape> CONNECT = EnumProperty.of("connect", CornerShape.class);
}
