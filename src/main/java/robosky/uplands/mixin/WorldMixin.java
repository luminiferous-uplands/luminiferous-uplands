package robosky.uplands.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin {

//    @Shadow @Final public Dimension dimension;
//
//    /**
//     * Prevents rain and thunder from darkening the Uplands.
//     */
//    @Inject(method = "getRainGradient", at = @At("HEAD"), cancellable = true)
//    private void onRainGradient(CallbackInfoReturnable<Float> info) {
//        if (this.dimension.getType() == WorldRegistry.UPLANDS_DIMENSION) {
//            info.setReturnValue(0.0f);
//        }
//    }
}
