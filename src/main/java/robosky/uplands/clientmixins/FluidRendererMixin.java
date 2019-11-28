package robosky.uplands.clientmixins;

import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import robosky.uplands.block.UplandsWaterBlock;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin {

    @Unique
    private float alpha;

    @Inject(method = "tesselate", at = @At("HEAD"))
    private void onTesselate(ExtendedBlockView world, BlockPos pos, BufferBuilder builder, FluidState state, CallbackInfoReturnable<Boolean> info) {
        // the number of blocks the fade takes up
        final float FADE_LENGTH = 10;
        alpha = 1.0f;
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() == Blocks.WATER) {
            int adjustedFall = UplandsWaterBlock.MAX_FALL() - blockState.get(UplandsWaterBlock.FALL());
            if (adjustedFall < FADE_LENGTH) {
                alpha = (adjustedFall + 1) / (float)FADE_LENGTH;
            }
        }
    }

    @ModifyArg(
        method = "tesselate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/BufferBuilder;color(FFFF)Lnet/minecraft/client/render/BufferBuilder;"
        ),
        index = 3
    )
    private float adjustAlphaForUplandsFadeOut(float a) {
        return alpha;
    }
}
