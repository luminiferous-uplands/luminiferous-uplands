package com.github.draylar.dimension.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PortalForcer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robosky.ether.MixinHack;

import java.util.HashSet;

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
        if (toDimension == MixinHack.ETHER_DIMTYPE || previousDimension == MixinHack.ETHER_DIMTYPE) {
            BlockPos pos = etherdimension_getTopPos(world, entity.getBlockPos());

            int y = pos.getY();
            if (entity.y < -63.0) {
                y = 257;
            }
            if (entity instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) entity).networkHandler.teleportRequest(pos.getX(), y, pos.getZ(), 0, 0, new HashSet<>());
                ((ServerPlayerEntity) entity).networkHandler.syncWithPlayerPosition();
            } else {
                entity.setPositionAndAngles(pos.getX(), y, pos.getZ(), 0, 0);
            }

            info.setReturnValue(true);
            info.cancel();
        }
    }


    /**
     * Returns the top position in the world; used for finding spawn position.
     * Prefixed with MODID to prevent mixin method conflicts.
     *
     * @param world world to search through
     * @param pos   base position of spawn; only accounts for X and Z
     * @return BlockPos with changed Y value depending on top block in world
     */
    private BlockPos etherdimension_getTopPos(final World world, final BlockPos pos) {
        BlockPos returnPos = new BlockPos(pos.getX(), 0, pos.getZ());

        // start at top block in x/z position and move down
        for (int i = 255; i > 0; i--) {
            // if block is not air, return that spot
            if (!world.getBlockState(returnPos.up(i)).equals(Blocks.AIR.getDefaultState())) {
                returnPos = returnPos.up(i);
                return returnPos;
            }
        }

        return pos;
    }
}
