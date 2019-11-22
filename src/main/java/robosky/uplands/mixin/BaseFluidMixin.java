package robosky.uplands.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import robosky.uplands.block.UplandsWaterBlock;
import robosky.uplands.world.WorldRegistry;

@Mixin(BaseFluid.class)
public abstract class BaseFluidMixin extends Fluid {

    @Shadow
    public native Fluid getFlowing();

    @Shadow
    public native Fluid getStill();

    @Redirect(
        method = "flow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/fluid/FluidState;getBlockState()Lnet/minecraft/block/BlockState;"
        )
    )
    private BlockState proxyBlockStateFlowing(FluidState self, IWorld world, BlockPos pos, BlockState blockState, Direction dir, FluidState self2) {
        return getBlockStateWithFall(world, pos, self);
    }

    @Redirect(
        method = "onScheduledTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/fluid/FluidState;getBlockState()Lnet/minecraft/block/BlockState;"
        )
    )
    private BlockState proxyBlockStateDraining(FluidState self, World world, BlockPos pos, FluidState self2) {
        return getBlockStateWithFall(world, pos, self);
    }

    /**
     * Updates the fall state of the fluid block based on the surrounding
     * fluid blocks. Horizontally flowing blocks pass their fall state
     * unchanged to this fluid block, while vertically flowing blocks
     * (level=8) pass their fall state incremented by 1.
     */
    @Unique
    private BlockState getBlockStateWithFall(IWorld world, BlockPos pos, FluidState state) {
        BlockState res = state.getBlockState();
        if (world.getDimension().getType() == WorldRegistry.UPLANDS_DIMENSION() && !state.isStill() && res.getBlock() == Blocks.WATER) {
            BlockPos[] poses = { pos.up(), pos.north(), pos.south(), pos.west(), pos.east() };
            int level = res.get(FluidBlock.LEVEL);
            int fall = Integer.MAX_VALUE;
            for (BlockPos neighborPos : poses) {
                FluidState neighborFluidState = world.getFluidState(neighborPos);
                BlockState neighborBlockState = world.getBlockState(neighborPos);
                // ensure that the block is actually a water block
                if (neighborBlockState.getBlock() == res.getBlock()) {
                    int neighborLevel = neighborBlockState.get(FluidBlock.LEVEL);
                    System.out.println("Neighbor level: " + level);
                    if (neighborLevel == 8 || neighborLevel < level) {
                        int neighborFall = neighborBlockState.get(UplandsWaterBlock.FALL());
                        System.out.println("Neighbor fall/fall: " + neighborFall + "/" + fall);
                        if (neighborFall < fall) {
                            fall = neighborFall + (neighborLevel == 8 ? 1 : 0);
                            System.out.println("New fall: " + fall);
                        }
                    }
                }
            }
            if (fall == Integer.MAX_VALUE) {
                fall = res.get(UplandsWaterBlock.FALL());
            }
            if (fall > UplandsWaterBlock.MAX_FALL()) {
                res = Blocks.AIR.getDefaultState();
            } else {
                System.out.println("Setting fall to: " + fall);
                res = res.with(UplandsWaterBlock.FALL(), fall);
            }
        }
        return res;
    }
}
