package robosky.uplands.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.IWorld;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import robosky.uplands.block.UplandsWaterBlock;

@Mixin(BaseFluid.class)
public abstract class BaseFluidMixin extends Fluid {

    /**
     * Prevents fluid from trying to flow when its fall state is maximum.
     */
    @Inject(method = "flow", at = @At("HEAD"), cancellable = true)
    private void tweakFlow(IWorld world, BlockPos pos, BlockState state, Direction dir, FluidState fluidState, CallbackInfo info) {
        BlockState source = world.getBlockState(pos.offset(dir.getOpposite()));
        if (source.getBlock() == Blocks.WATER && source.get(UplandsWaterBlock.FALL) == UplandsWaterBlock.MAX_FALL) {
            info.cancel();
        }
    }
}
