package robosky.uplands.mixin;

import net.minecraft.block.EntityShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import robosky.uplands.iface.BossDoorwayContext;
import robosky.uplands.item.UplandsItemTags;

@Mixin(EntityShapeContext.class)
public abstract class EntityContextImplMixin implements BossDoorwayContext {

    @Unique
    private boolean shouldSeeDoorwayOutlines;

    @Override
    public boolean uplands_shouldSeeDoorwayOutlines() {
        return shouldSeeDoorwayOutlines;
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/Entity;)V", at = @At("RETURN"))
    private void onConstruct(Entity entity, CallbackInfo info) {
        if(entity instanceof PlayerEntity) {
          PlayerEntity player = (PlayerEntity)entity;
          shouldSeeDoorwayOutlines = player.isCreativeLevelTwoOp() &&
              player.inventory.contains(UplandsItemTags.BOSSROOM_TECHNICAL);
        } else {
          shouldSeeDoorwayOutlines = false;
        }
    }
}
