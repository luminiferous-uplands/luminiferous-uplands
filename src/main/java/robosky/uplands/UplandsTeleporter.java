//package robosky.uplands;
//
//import java.util.Random;
//
//import robosky.uplands.advancement.FlyIntoUplandsCriterion;
//import robosky.uplands.world.WorldRegistry;
//import robosky.uplands.world.feature.SpawnPlatformPiece;
//
//import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
//import net.minecraft.advancement.criterion.Criterion;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.pattern.BlockPattern;
//import net.minecraft.entity.Entity;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.server.world.ServerChunkManager;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.util.math.BlockBox;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.ChunkPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//
//public final class UplandsTeleporter {
//
//    public static Criterion<?> getUplandsCriterion() {
//        return FlyIntoUplandsCriterion.INSTANCE;
//    }
//
//    public static final EntityPlacer TO_UPLANDS_BEACON = new EntityPlacer() {
//        @Override
//        public BlockPattern.TeleportTarget placeEntity(Entity entity, ServerWorld world, Direction direction, double v, double v1) {
//            // teleport to the spawn platform
//            CompoundTag tag = world.getLevelProperties().getWorldData(WorldRegistry.UPLANDS_DIMENSION);
//            Vec3d pos;
//            if(tag.contains("SpawnPlatform")) {
//                int[] ptag = tag.getIntArray("SpawnPlatform");
//                pos = new Vec3d(ptag[0], ptag[1], ptag[2]);
//            } else {
//                BlockPos pos1 = getTopPos(world, 7, 7).up(2);
//                createSpawnPlatform(world, pos1.north(6).west(4).down(6));
//                tag.putIntArray("SpawnPlatform", new int[]{ pos1.getX(), pos1.getY(), pos1.getZ() });
//                world.getLevelProperties().setWorldData(WorldRegistry.UPLANDS_DIMENSION, tag);
//                pos = new Vec3d(pos1.getX(), pos1.getY(), pos1.getZ());
//            }
//            return new BlockPattern.TeleportTarget(pos, Vec3d.ZERO, 0);
//        }
//
//        private void createSpawnPlatform(World world, BlockPos pos) {
//            if(!world.isClient) {
//                ServerWorld sw = (ServerWorld)world;
//                SpawnPlatformPiece structure = new SpawnPlatformPiece(sw.getStructureManager(), pos);
//                ChunkPos cp = new ChunkPos(pos);
//                structure.generate(
//                    world,
//                    ((ServerChunkManager)sw.getChunkManager()).getChunkGenerator(),
//                    new Random(),
//                    new BlockBox(
//                        cp.getStartX(),
//                        cp.getStartZ(),
//                        cp.getEndX(),
//                        cp.getEndZ()
//                    ),
//                    cp
//                );
//            }
//        }
//
//        private BlockPos getTopPos(World world, int x, int z) {
//            BlockPos returnPos = new BlockPos(x, 0, z);
//            for(int i = 70; i >= 48; i--) { // if block is not air, return that spot
//                if(!(world.getBlockState(returnPos.up(i)) == Blocks.AIR.getDefaultState())) {
//                    returnPos = returnPos.up(i + 1);
//                    return returnPos;
//                }
//            }
//            return returnPos.up(56);
//        }
//    };
//
//    public static final EntityPlacer TO_UPLANDS_FLYING = (entity, world, direction, v, v1) -> {
//        if(entity instanceof ServerPlayerEntity) {
//            ServerPlayerEntity se = (ServerPlayerEntity)entity;
//            FlyIntoUplandsCriterion.INSTANCE.handle(se);
//        }
//        return new BlockPattern.TeleportTarget(new Vec3d(entity.getX(), -40.0, entity.getZ()), entity.getVelocity(), (int)entity.yaw);
//    };
//
//    public static final EntityPlacer FROM_UPLANDS = (entity, world, direction, v, v1) ->
//        new BlockPattern.TeleportTarget(new Vec3d(entity.getX(), 256, entity.getZ()), entity.getVelocity(), (int)entity.yaw);
//}
