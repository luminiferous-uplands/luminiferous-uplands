package com.github.draylar.dimension.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PortalForcer;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robosky.ether.UplandsTeleporter;
import robosky.ether.world.WorldRegistry;

@Mixin(PortalForcer.class)
public class ChangeDimensionMixin {
    @Shadow
    @Final
    private ServerWorld world;

    @Inject(at = @At("HEAD"), method = "usePortal", cancellable = true)
    private void usePortal(final Entity entity, final float entityYaw, final CallbackInfoReturnable<Boolean> info) {
        DimensionType previousDimension = entity.world.getDimension().getType();
        DimensionType toDimension = world.getDimension().getType();

        // going or coming from our custom dimension
        // regardless, we get the top position at 0,0 and set the player there
        if (toDimension == WorldRegistry.UPLANDS_DIMENSION()) {
            info.setReturnValue(UplandsTeleporter.usePortalHookTo(entity, world));
        } else if (previousDimension == WorldRegistry.UPLANDS_DIMENSION()) {
            info.setReturnValue(UplandsTeleporter.usePortalHookFrom(entity, world));
        }
    }
}
