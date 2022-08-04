package be.uantwerpen.scicraft.fluid;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.item.Items;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import java.util.Map;

public abstract class AcidFluid extends AbstractFluid {
    @Override
    public Fluid getStill() {
        return Fluids.STILL_ACID;
    }

    @Override
    public Fluid getFlowing() {
        return Fluids.FLOWING_ACID;
    }

    @Override
    public Item getBucketItem() {
        return Items.ERLENMEYER_HNO3;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        // getBlockStateLevel converts the LEVEL_1_8 of the fluid state to the LEVEL_15 the fluid block uses
        return Blocks.ACID.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    public static class Flowing extends AcidFluid {
        @Override
        protected void appendProperties(net.minecraft.state.StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }

        @Override
        public Vec3d getVelocity(BlockView world, BlockPos pos, FluidState state) {
            return super.getVelocity(world, pos, state);
        }

        @Override
        protected Map<Direction, FluidState> getSpread(WorldView world, BlockPos pos, BlockState state) {
            return super.getSpread(world, pos, state);
        }
    }

    public static class Still extends AcidFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
