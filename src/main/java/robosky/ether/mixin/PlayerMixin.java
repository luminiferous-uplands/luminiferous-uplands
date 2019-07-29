package robosky.ether.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robosky.ether.MixinHack;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
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
        if (MixinHack.HOOKS.checkParachute(getMainHandStack())) {
            addPotionEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 0, false,
                    false, true));
            if (getVelocity().y < 0)
                getMainHandStack().damage(1, (PlayerEntity) (Object) this, p -> p.sendToolBreakStatus(Hand.MAIN_HAND));
            return;
        }
        if (MixinHack.HOOKS.checkParachute(getOffHandStack())) {
            addPotionEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 0, false,
                    false, true));
            if (getVelocity().y < 0)
                getOffHandStack().damage(1, (PlayerEntity) (Object) this, p -> p.sendToolBreakStatus(Hand.OFF_HAND));
        }
    }
}
