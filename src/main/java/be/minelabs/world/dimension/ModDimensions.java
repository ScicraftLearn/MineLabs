package be.minelabs.world.dimension;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.portal.frame.VanillaPortalAreaHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {

    // How far the player can reach in the subatomic dimension
    public static final float SUBATOMIC_REACH_DISTANCE = 20f;

    private static final RegistryKey<DimensionOptions> DIMENSION_KEY = RegistryKey.of(RegistryKeys.DIMENSION, new Identifier(Minelabs.MOD_ID, "subatom"));
    public static RegistryKey<World> SUBATOM_KEY = RegistryKey.of(RegistryKeys.WORLD, DIMENSION_KEY.getValue());
    public static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier(Minelabs.MOD_ID, "subatom_type"));

    public static Identifier SUBATOMICPORTAL_FRAMETESTER = new Identifier(Minelabs.MOD_ID, "subatom");


    public static void onInitialize() {
        registerPortals();
    }

    private static void registerPortals() {
        //TODO CUSTOM TEXTURE ?? -> custom portal block (must extend CustomPortalBlock)
        // Light Item -> ATOM ?
        // SOUNDS ??

        CustomPortalApiRegistry.registerPortalFrameTester(SUBATOMICPORTAL_FRAMETESTER, () -> new VanillaPortalAreaHelper() {
            /**
             * We want to place the portal one block higher so the subatomic floor isn't replaced.
             */
            @Override
            public BlockPos doesPortalFitAt(World world, BlockPos attemptPos, Direction.Axis axis) {
                BlockPos pos = super.doesPortalFitAt(world, attemptPos, axis);
                if (pos == null) return pos;
                // Super check only covers normal portal positions, we manually check the extra position at the top that is needed when the portal is placed one higher.
                if (!isEmptySpace(world.getBlockState(attemptPos.up(3))) || !isEmptySpace(world.getBlockState(attemptPos.offset(axis, 1).up(3)))){
                    return null;
                }
                return pos.up();
            }
        });

        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.SALT_BLOCK)
                .destDimID(new Identifier(Minelabs.MOD_ID, "subatom"))
                .onlyLightInOverworld()
                //.customPortalBlock((CustomPortalBlock) Blocks.PORTAL_BLOCK)
                .tintColor(153, 180, 181)
                .customFrameTester(SUBATOMICPORTAL_FRAMETESTER)
                .registerPortal();
    }
}
