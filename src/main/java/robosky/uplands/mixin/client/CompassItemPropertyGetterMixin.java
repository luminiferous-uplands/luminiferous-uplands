package robosky.uplands.mixin.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robosky.uplands.block.BlockRegistry;
import robosky.uplands.mixin.client.AngleRandomizerAccessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.client.item.ModelPredicateProviderRegistry$2")
public abstract class CompassItemPropertyGetterMixin {

    @Unique
    private static final BlockPos.Mutable pos = new BlockPos.Mutable();

    @Inject(
        method = "call(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/entity/LivingEntity;)F",
        at = @At("HEAD")
    )
    private void calcUplandsLodestonePos(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity, CallbackInfoReturnable<Float> info) {
        Entity entity = livingEntity != null ? livingEntity : stack.getHolder();
        if(entity == null) {
            return;
        }
        if(world == null) {
            world = (ClientWorld)entity.world;
        }
        final int X_RADIUS = 6;
        final int Y_RADIUS = 3;
        int targetX = Integer.MAX_VALUE;
        int targetY = Integer.MAX_VALUE;
        int targetZ = Integer.MAX_VALUE;
        int prevDist = Integer.MAX_VALUE;
        for(int x = -X_RADIUS; x <= X_RADIUS; x++) {
            for(int y = -Y_RADIUS; y <= Y_RADIUS; y++) {
                for(int z = -X_RADIUS; z <= X_RADIUS; z++) {
                    pos.set(entity.getBlockPos()).move(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    if(state.getBlock() == BlockRegistry.LODESTONE) {
                        int dist = Math.abs(x) + Math.abs(y) + Math.abs(z);
                        if(dist < prevDist) {
                            targetX = x;
                            targetY = y;
                            targetZ = z;
                            prevDist = dist;
                        }
                    }
                }
            }
        }
        if(targetY != Integer.MAX_VALUE) {
            pos.set(entity.getBlockPos()).move(targetX, targetY, targetZ);
        } else {
            pos.set(0, -1, 0);
        }
    }

    @ModifyVariable(
        method = "call(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/entity/LivingEntity;)F",
        ordinal = 0,
        at = @At(value = "STORE", ordinal = 0)
    )
    private BlockPos modifyUplandsCompassPos(BlockPos target) {
        if(pos.getY() != -1) {
            target = pos;
        }
        return target;
    }

    /**
     * Stores the normalized angle to the target position.
     */
    @Unique
    private final ModelPredicateProviderRegistry.AngleRandomizer targetValue = new ModelPredicateProviderRegistry.AngleRandomizer();

    @Inject(method = "getAngleToPos(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/Entity;)D", at = @At("RETURN"), cancellable = true)
    private void interpolateCompassTargetChanges(Vec3d targetPos, Entity entity, CallbackInfoReturnable<Double> info) {
        long time = entity.world.getTime();
        if(targetValue.shouldUpdate(time)) {
            targetValue.update(time, info.getReturnValueD() / (2 * Math.PI));
        }
        info.setReturnValue(((AngleRandomizerAccessor)targetValue).getValue() * (2 * Math.PI));
    }
}
