package robosky.uplands.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import robosky.uplands.block.UplandsWaterBlock;
import robosky.uplands.world.WorldRegistry;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin extends Block {

    private FluidBlockMixin() {
        super(null);
    }

    @Inject(method = "appendProperties", at = @At("RETURN"))
    private void onAppendProperties(StateFactory.Builder<Block, BlockState> builder, CallbackInfo info) {
        // You see...since this is called in the superclass constructor,
        // none of this object's fields has been initialized (including this.fluid),
        // so we can't use the fluid to determine if this object is water or lava.
        // Nor can we compare this object with Blocks.WATER because the Blocks
        // class hasn't been initialized yet. However, Blocks.WATER is initialized
        // first, so it is null when the water block is being created and is
        // nonnull on every subsequent execution of this method.
        if(Blocks.WATER == null) {
            builder.add(UplandsWaterBlock.FALL());
        }
    }

    // so we can see the block state property values
    @Inject(method = "getOutlineShape", at = @At("RETURN"), cancellable = true)
    private void replaceOutlineShape(CallbackInfoReturnable<VoxelShape> info) {
        info.setReturnValue(VoxelShapes.fullCube());
    }

    @Inject(method = "onBlockAdded", at = @At("RETURN"))
    private void updateUplandsStateOnAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean idk, CallbackInfo info) {
        if (world.getDimension().getType() == WorldRegistry.UPLANDS_DIMENSION() &&
                !state.getFluidState().isStill() && state.getBlock() == Blocks.WATER) {
            int fall = getUplandsFall(state, world, pos);
            if (fall > UplandsWaterBlock.MAX_FALL()) {
                state = Blocks.AIR.getDefaultState();
            } else {
                state = state.with(UplandsWaterBlock.FALL(), fall);
            }
            world.setBlockState(pos, state);
        }
    }

    @Inject(method = "getStateForNeighborUpdate", at = @At("RETURN"), cancellable = true)
    private void updateUplandsState(BlockState state, Direction dir, BlockState neighbor, IWorld world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> info) {
        if (world.getDimension().getType() == WorldRegistry.UPLANDS_DIMENSION()) {
            state = info.getReturnValue();
            if (!state.getFluidState().isStill() && state.getBlock() == Blocks.WATER) {
                int fall = getUplandsFall(state, world, pos);
                if (fall > UplandsWaterBlock.MAX_FALL()) {
                    state = Blocks.AIR.getDefaultState();
                } else {
                    state = state.with(UplandsWaterBlock.FALL(), fall);
                }
                info.setReturnValue(state);
            }
        }
    }

    /**
     * Directions in which to check for water fall states. Water never flows
     * upward, so we do not check the block below.
     */
    @Unique
    private static final Direction[] SOURCES = {
        Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    /**
     * Updates the fall state of the fluid block based on the surrounding
     * fluid blocks. Horizontally flowing blocks pass their fall state
     * unchanged to this fluid block, while vertically flowing blocks
     * (level=8) pass their fall state incremented by 1.
     */
    @Unique
    private int getUplandsFall(BlockState state, IWorld world, BlockPos pos) {
        int level = state.get(FluidBlock.LEVEL);
        int fall = Integer.MAX_VALUE;
        boolean falling = state.getFluidState().get(BaseFluid.FALLING);
        for (Direction dir : SOURCES) {
            FluidState neighborFluidState = world.getFluidState(pos.offset(dir));
            BlockState neighborBlockState = world.getBlockState(pos.offset(dir));
            // ensure that the block is actually a water block
            if (neighborBlockState.getBlock() == state.getBlock()) {
                // only update falling water from the side if the source is a
                // source (still) block. Update non-falling water from all sides
                if (dir == Direction.UP || !falling || neighborFluidState.isStill()) {
                    int neighborFall = neighborBlockState.get(UplandsWaterBlock.FALL());
                    if (neighborFall < fall) {
                        // propagate fall state across horizontal surfaces
                        fall = neighborFall;
                        if (dir == Direction.UP) {
                            // increment fall state when falling..
                            fall++;
                        }
                    }
                }
            }
        }
        // Fallback to the current value if there are no water blocks around.
        // This allows water to keep its fall while draining.
        if (fall == Integer.MAX_VALUE) {
            fall = state.get(UplandsWaterBlock.FALL());
        }
        return fall;
    }
}
