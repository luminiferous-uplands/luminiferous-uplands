package robosky.uplands.clientmixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robosky.uplands.world.WorldRegistry;

@Mixin(ClientWorld.class)
public abstract class WorldMixin extends World {

    private WorldMixin() {
        super(null, null, null, null, false);
    }

    @Environment(EnvType.CLIENT)
    @Inject(at = @At("HEAD"), method = "getSkyDarknessHeight", cancellable = true)
    public void getHorizonHeight(final CallbackInfoReturnable<Double> cb) {
        if (this.dimension != null && this.dimension.getType() == WorldRegistry.UPLANDS_DIMENSION) {
            cb.setReturnValue(Double.NEGATIVE_INFINITY);
        }
    }
}
