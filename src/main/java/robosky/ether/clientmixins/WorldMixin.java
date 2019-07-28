package robosky.ether.clientmixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robosky.ether.MixinHack;

@Mixin(World.class)
public class WorldMixin {
    @Environment(EnvType.CLIENT)
    @Inject(at = @At("HEAD"), method = "getHorizonHeight", cancellable = true)
    public void getHorizonHeight(final CallbackInfoReturnable<Double> cb) {
        Dimension dimension = ((World) (Object) this).dimension;
        if (dimension != null && dimension.getType() == MixinHack.ETHER_DIMTYPE) {
            cb.setReturnValue(0D);
        }
    }
}
