package robosky.uplands.clientmixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robosky.uplands.world.WorldRegistry;

@Mixin(BackgroundRenderer.class)
public class BGRenderMixin {

    @ModifyVariable(
        method = "render",
        at = @At(value = "STORE", ordinal = 0),
        ordinal = 0
    )
    private static double modifyVoidColor(double scale) {
        if (MinecraftClient.getInstance().world.dimension.getType() == WorldRegistry.UPLANDS_DIMENSION()) {
            scale = 1.0;
        }
        return scale;
    }

    @Inject(at = @At("HEAD"), method = "render")
    private static void heckLoom(CallbackInfo info) {

    }
}
