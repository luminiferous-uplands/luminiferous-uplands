package robosky.ether.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContextImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Lazy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import robosky.ether.block.BlockRegistry;
import robosky.ether.iface.BossDoorwayContext;

@Mixin(EntityContextImpl.class)
public abstract class EntityContextImplMixin implements BossDoorwayContext {

    // it is very important that the lazy is only initialized after
    // item registration. EntityContextImpl is loaded before Uplands registers
    // its items, so a lazy field is needed.
    @Unique
    private static final Lazy<ItemStack> doorway =
        new Lazy<>(() -> new ItemStack(BlockRegistry.BOSS_DOORWAY().asItem()));

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
              player.inventory.contains(doorway.get());
        } else {
          shouldSeeDoorwayOutlines = false;
        }
    }
}
