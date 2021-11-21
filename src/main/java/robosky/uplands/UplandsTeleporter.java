package robosky.uplands;

import java.util.Random;

import robosky.uplands.advancement.FlyIntoUplandsCriterion;
import robosky.uplands.world.feature.SpawnPlatformPiece;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public final class UplandsTeleporter {

    @FunctionalInterface
    public interface TeleportTargetSupplier {
        TeleportTarget create(Entity entity, ServerWorld world);
    }

    public static final TeleportTargetSupplier TO_UPLANDS_BEACON = (entity, world) -> {
        // teleport to the spawn platform
        UplandsPersistentState state = world.getPersistentStateManager()
            .getOrCreate(UplandsPersistentState::new, UplandsPersistentState.KEY);
        Vec3d pos = state.getPlatformLocation();
        if(pos == null) {
            BlockPos pos1 = getTopPos(world, 7, 7).up(2);
            createSpawnPlatform(world, pos1.north(6).west(4).down(6));
            pos = new Vec3d(pos1.getX(), pos1.getY(), pos1.getZ());
            state.setPlatformLocation(pos);
            world.getPersistentStateManager().set(state);
        }
        return new TeleportTarget(pos, Vec3d.ZERO, 0, 0);
    };

    private static void createSpawnPlatform(ServerWorld world, BlockPos pos) {
        SpawnPlatformPiece structure = new SpawnPlatformPiece(world.getStructureManager(), pos);
        ChunkPos cp = new ChunkPos(pos);
        structure.generate(
            world,
            world.getStructureAccessor(),
            world.getChunkManager().getChunkGenerator(),
            new Random(),
            new BlockBox(
                cp.getStartX(),
                cp.getStartZ(),
                cp.getEndX(),
                cp.getEndZ()
            ),
            cp,
            pos
        );
    }

    private static BlockPos getTopPos(World world, int x, int z) {
        BlockPos returnPos = new BlockPos(x, 0, z);
        for(int i = 140; i >= 48; i--) { // if block is not air, return that spot
            if(!(world.getBlockState(returnPos.up(i)) == Blocks.AIR.getDefaultState())) {
                return returnPos.up(i + 1);
            }
        }
        return returnPos.up(100);
    }

    public static final TeleportTargetSupplier TO_UPLANDS_FLYING = (entity, world) -> {
        if(entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity se = (ServerPlayerEntity)entity;
            FlyIntoUplandsCriterion.INSTANCE.grant(se);
        }
        return new TeleportTarget(new Vec3d(entity.getX(), -40.0, entity.getZ()), entity.getVelocity(), entity.yaw, entity.pitch);
    };

    public static final TeleportTargetSupplier FROM_UPLANDS = (entity, world) ->
        new TeleportTarget(new Vec3d(entity.getX(), 290, entity.getZ()), entity.getVelocity(), entity.yaw, entity.pitch);

    @SuppressWarnings("deprecation")
    public static void teleport(Entity entity, ServerWorld world, TeleportTargetSupplier targetSupplier) {
        FabricDimensions.teleport(entity, world, targetSupplier.create(entity, world));
    }
}
