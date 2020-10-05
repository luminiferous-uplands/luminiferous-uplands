package robosky.uplands.clientmixins;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

@Mixin(ClientWorld.class)
public abstract class WorldMixin extends World {

    private WorldMixin() {
        super(null, null, null, null, false, false, 0);
    }

//    @Environment(EnvType.CLIENT)
//    @Inject(at = @At("HEAD"), method = "getSkyDarknessHeight", cancellable = true)
//    public void getHorizonHeight(final CallbackInfoReturnable<Double> cb) {
//        if (this.dimension != null && this.dimension.getType() == WorldRegistry.UPLANDS_DIMENSION) {
//            cb.setReturnValue(Double.NEGATIVE_INFINITY);
//        }
//    }
}
