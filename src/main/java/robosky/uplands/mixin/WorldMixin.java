package robosky.uplands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robosky.uplands.UplandsMod;

import net.minecraft.class_5423;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin {

    /**
     * Prevents rain and thunder from darkening the Uplands.
     */
    @Inject(method = "getRainGradient", at = @At("HEAD"), cancellable = true)
    private void onRainGradient(CallbackInfoReturnable<Float> info) {
        if(UplandsMod.isUplandsDimensionType((class_5423)this)) {
            info.setReturnValue(0.0f);
        }
    }
}
