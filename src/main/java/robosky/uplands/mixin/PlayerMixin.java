package robosky.uplands.mixin;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robosky.uplands.TickableItem;
import robosky.uplands.UplandsTeleporter;
import robosky.uplands.iface.UplanderBeaconUser;
import robosky.uplands.world.WorldRegistry;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements UplanderBeaconUser {

    @Unique
    private static final double VERTICAL_SPEED = 5.0;

    @Unique
    private boolean usingBeacon;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Override
    public boolean uplands_isUsingBeacon() {
        return usingBeacon;
    }

    @Override
    public void uplands_setUsingBeacon(boolean using) {
        usingBeacon = using;
        if (using) {
            Vec3d vel = this.getVelocity();
            this.setVelocity(vel.x, VERTICAL_SPEED, vel.z);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;updateTurtleHelmet()V"),
            method = "tick")
    private void updateParachute(CallbackInfo info) {
        if (getMainHandStack().getItem() instanceof TickableItem) {
            ((TickableItem) getMainHandStack().getItem()).tick((PlayerEntity) (Object) this, getMainHandStack(), Hand.MAIN_HAND);
        }
        if (getOffHandStack().getItem() instanceof TickableItem) {
            ((TickableItem) getOffHandStack().getItem()).tick((PlayerEntity) (Object) this, getOffHandStack(), Hand.OFF_HAND);
        }
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void onTickMovement(CallbackInfo info) {
        if (!world.isClient) {
            if (this.dimension == WorldRegistry.UPLANDS_DIMENSION() && this.getY() <= -60) {
                FabricDimensions.teleport(this, DimensionType.OVERWORLD, UplandsTeleporter.FromUplands$.MODULE$);
            } else if (this.dimension == DimensionType.OVERWORLD && this.getY() >= 300.0) {
                if (uplands_isUsingBeacon()) {
                    FabricDimensions.teleport(this, WorldRegistry.UPLANDS_DIMENSION(), UplandsTeleporter.ToUplandsBeacon$.MODULE$);
                } else {
                    FabricDimensions.teleport(this, WorldRegistry.UPLANDS_DIMENSION(), UplandsTeleporter.ToUplandsFlying$.MODULE$);
                }
            }
        }
        if (uplands_isUsingBeacon()) {
            Vec3d vel = this.getVelocity();
            if (vel.y < 0.1) {
                uplands_setUsingBeacon(false);
            } else {
                this.setVelocity(vel.x, VERTICAL_SPEED, vel.z);
            }
        }
    }
}
