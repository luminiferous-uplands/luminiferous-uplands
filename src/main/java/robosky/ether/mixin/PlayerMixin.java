package robosky.ether.mixin;

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
import robosky.ether.MixinHack;
import robosky.ether.TickableItem;
import robosky.ether.iface.UplanderBeaconUser;

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

    @Override
    protected void destroy() {
        if (this.dimension == MixinHack.HOOKS.getDimensionType()) {
            changeDimension(DimensionType.OVERWORLD);
        } else {
            super.destroy();
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
    private void flyIntoUplands(CallbackInfo info) {
        if (this.dimension == DimensionType.OVERWORLD && this.y >= 300.0) {
            changeDimension(MixinHack.HOOKS.getDimensionType());
        }
        if (uplands_isUsingBeacon()) {
            Vec3d vel = this.getVelocity();
            if(vel.y < 0.1) {
                uplands_setUsingBeacon(false);
            } else {
                this.setVelocity(vel.x, VERTICAL_SPEED, vel.z);
            }
        }
    }
}
