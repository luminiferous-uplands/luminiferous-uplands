package robosky.ether.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robosky.ether.TickableItem;
import robosky.ether.world.WorldRegistry;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Override
    protected void destroy() {
        if (this.dimension == WorldRegistry.UPLANDS_DIMENSION()) {
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
}
