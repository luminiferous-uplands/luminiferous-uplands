package robosky.ether.clientmixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robosky.ether.MixinHack;

@Mixin(World.class)
public class WorldMixin {
    @Shadow
    @Final
    private Dimension dimension;

    @Environment(EnvType.CLIENT)
    @Inject(at = @At("HEAD"), method = "getHorizonHeight", cancellable = true)
    public void getHorizonHeight(final CallbackInfoReturnable<Double> cb) {
        if (dimension != null && dimension.getType() == MixinHack.HOOKS.getDimensionType()) {
            cb.setReturnValue(Double.NEGATIVE_INFINITY);
        }
    }
}
