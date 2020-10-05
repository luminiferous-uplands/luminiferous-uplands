package robosky.uplands.clientmixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import robosky.uplands.block.UplandsWaterBlock;
import robosky.uplands.world.WorldRegistry;

@Mixin(FluidRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class FluidRendererMixin {

    @Unique
    private float alpha;

    @Inject(method = "render", at = @At("HEAD"))
    private void setUplandsWaterAlpha(BlockRenderView world, BlockPos pos, VertexConsumer builder,
            FluidState state, CallbackInfoReturnable<Boolean> info) {
        // the number of blocks the fade takes up
        final float FADE_LENGTH = 10;
        alpha = 1.0f;
        if (state.getFluid().matchesType(Fluids.WATER)) {
            int adjustedFall = UplandsWaterBlock.MAX_FALL;
            BlockState blockState = world.getBlockState(pos);
            if (blockState.getBlock() == Blocks.WATER) {
                adjustedFall -= blockState.get(UplandsWaterBlock.FALL);
            }
            // fade out into the void as well
            // todo
//            if (MinecraftClient.getInstance().world.getDimension() == WorldRegistry.UPLANDS_DIMENSION) {
//                adjustedFall = Math.min(adjustedFall, pos.getY());
//            }
            if (adjustedFall < FADE_LENGTH) {
                alpha = (adjustedFall + 1) / (float)FADE_LENGTH;
            }
        }
    }

    @ModifyArg(
        method = "vertex",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexConsumer;color(FFFF)Lnet/minecraft/client/render/VertexConsumer;"
        ),
        index = 3
    )
    private float adjustAlphaForUplandsFadeOut(float a) {
        return alpha;
    }
}
