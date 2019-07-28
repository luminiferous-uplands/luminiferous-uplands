package robosky.ether.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import robosky.ether.MixinHack;

@Mixin(PlayerEntity.class)
public abstract class VoidOutMixin extends LivingEntity {
    protected VoidOutMixin(EntityType<? extends LivingEntity> entityType_1, World world_1) {
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
}
