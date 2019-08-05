package robosky.ether.mixin;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robosky.ether.world.WorldRegistry;

@Mixin(World.class)
public abstract class WorldMixin {

    @Shadow @Final public Dimension dimension;

    /**
     * Prevents rain and thunder from darkening the Uplands.
     */
    @Inject(method = "getRainGradient", at = @At("HEAD"), cancellable = true)
    private void onRainGradient(CallbackInfoReturnable<Float> info) {
        if (this.dimension.getType() == WorldRegistry.UPLANDS_DIMENSION()) {
            info.setReturnValue(0.0f);
        }
    }
}
