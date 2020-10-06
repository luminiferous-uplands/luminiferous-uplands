package robosky.uplands.clientmixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import robosky.uplands.UplandsMod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    /**
     * Remove the sun and moon from the Uplands.
     */
    @ModifyArg(
        method = "renderSky",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;color4f(FFFF)V",
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
        if(this.client.world != null && UplandsMod.isUplandsDimensionType(this.client.world)) {
            alpha = 0.0f;
        }
        return alpha;
    }

    @Environment(EnvType.CLIENT)
    @ModifyVariable(
        method = "renderSky",
        ordinal = 0,
        at = @At(value = "LOAD", ordinal = 0),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/world/ClientWorld$Properties;getSkyDarknessHeight()D"
            )
        )
    )
    private double getHorizonHeight(double heightAboveHorizon) {
        if(this.client.world != null && UplandsMod.isUplandsDimensionType(this.client.world)) {
            heightAboveHorizon = Double.POSITIVE_INFINITY;
        }
        return heightAboveHorizon;
    }
}
