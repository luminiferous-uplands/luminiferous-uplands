package robosky.uplands.clientmixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import robosky.uplands.world.WorldRegistry;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow @Final private MinecraftClient client;

    /**
     * Remove the sun and moon from the Uplands.
     */
    @ModifyArg(
        method = "renderSky",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/platform/GlStateManager;color4f(FFFF)V",
            ordinal = 0
        ),
        index = 3,
        slice = @Slice(
            from = @At(
                value = "INVOKE:FIRST",
                target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"
            )
        )
    )
    private float modifySunMoonAlpha(float alpha) {
        if (this.client.world.dimension.getType() == WorldRegistry.UPLANDS_DIMENSION()) {
            alpha = 0.0f;
        }
        return alpha;
    }
}
