package robosky.uplands.clientmixins;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robosky.uplands.block.BlockRegistry;

@Mixin(targets = "net.minecraft.item.CompassItem$1")
public abstract class CompassItemPropertyGetterMixin {

    @Unique
    private static final BlockPos.Mutable pos = new BlockPos.Mutable();

    @Inject(method = "getAngleToSpawn(Lnet/minecraft/world/IWorld;Lnet/minecraft/entity/Entity;)D",
            at = @At("HEAD"), cancellable = true)
    private void onAngleToSpawn(IWorld world, Entity entity, CallbackInfoReturnable<Double> info) {
        final int X_RADIUS = 6;
        final int Y_RADIUS = 3;
        int targetX = Integer.MAX_VALUE;
        int targetY = Integer.MAX_VALUE;
        int targetZ = Integer.MAX_VALUE;
        int prevDist = Integer.MAX_VALUE;
        for (int x = -X_RADIUS; x <= X_RADIUS; x++) {
            for (int y = -Y_RADIUS; y <= Y_RADIUS; y++) {
                for (int z = -X_RADIUS; z <= X_RADIUS; z++) {
                    pos.set(entity).setOffset(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    if (state.getBlock() == BlockRegistry.LODESTONE()) {
                        int dist = Math.abs(x) + Math.abs(y) + Math.abs(z);
                        if (dist < prevDist) {
                            targetX = x;
                            targetY = y;
                            targetZ = z;
                            prevDist = dist;
                        }
                    }
                }
            }
        }
        if (targetY != Integer.MAX_VALUE) {
            info.setReturnValue(Math.atan2(targetZ, targetX));
        }
    }
}
