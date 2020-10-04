package robosky.uplands.world.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
import robosky.uplands.block.BlockRegistry;
import robosky.uplands.block.UplandsBlockTags;

import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.LightType;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

public class SkyLakeFeature extends Feature<SingleStateFeatureConfig> {

    public SkyLakeFeature(Function<Dynamic<?>, ? extends SingleStateFeatureConfig> f) {
        super(f);
    }

    @Override
    public boolean generate(WorldAccess world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos st, SingleStateFeatureConfig config) {
        BlockPos start = st;
        while(start.getY() > 5 && world.isAir(start)) {
            start = start.down();
        }

        if(start.getY() <= 4) {
            return false;
        }
        start = start.down(4);

        ChunkPos chunkPos = new ChunkPos(start);
        if(!world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_REFERENCES)
            .getStructureReferences(Feature.VILLAGE.getName())
            .isEmpty()) {
            return false;
        }

        boolean[] bitset = new boolean[2048];
        int size = random.nextInt(4) + 4;

        for(int unused = 0; unused < size; unused++) {
            double xSize = random.nextDouble() * 6.0D + 3.0D;
            double ySize = random.nextDouble() * 4.0D + 2.0D;
            // double ySize = random.nextDouble() * 2.0D + 2.0D;
            double zSize = random.nextDouble() * 6.0D + 3.0D;
            double centerX = random.nextDouble() * (16.0D - xSize - 2.0D) + 1.0D + xSize / 2.0D;
            double centerY = random.nextDouble() * (8.0D - ySize - 4.0D) + 2.0D + ySize / 2.0D;
            double centerZ = random.nextDouble() * (16.0D - zSize - 2.0D) + 1.0D + zSize / 2.0D;
            for(int dx = 1; dx < 15; dx++) {
                for(int dz = 1; dz < 15; dz++) {
                    for(int dy = 1; dy < 7; dy++) {
                        double xDist = (dx - centerX) / (xSize / 2.0D);
                        double yDist = (dy - centerY) / (ySize / 2.0D);
                        double zDist = (dz - centerZ) / (zSize / 2.0D);
                        double distSq = xDist * xDist + yDist * yDist + zDist * zDist;
                        if(distSq < 1.0D) {
                            bitset[(dx * 16 + dz) * 8 + dy] = true;
                        }
                    }
                }
            }
        }

        for(int dx = 1; dx < 16; dx++) {
            for(int dz = 1; dz < 16; dz++) {
                for(int dy = 1; dy < 8; dy++) {
                    if(bitsetCheck(bitset, dx, dz, dy)) {
                        Material material = world.getBlockState(start.add(dx, dy, dz)).getMaterial();
                        if(dy >= 4 && material.isLiquid()) {
                            return false;
                        }
                        if(dy < 4 && !material.isSolid() && (world.getBlockState(start.add(dx, dy, dz)) != config.state)) {
                            return false;
                        }
                    }
                }
            }
        }

        for(int i = 0; i < 16; i++) {
            for(int dz = 0; dz < 16; dz++) {
                for(int dy = 0; dy < 8; dy++) {
                    if(bitset[(i * 16 + dz) * 8 + dy]) {
                        world.setBlockState(
                            start.add(i, dy, dz),
                            dy >= 4 ?
                                Blocks.CAVE_AIR.getDefaultState()
                                : config.state,
                            2
                        );
                    }
                }
            }
        }

        for(int dx = 1; dx < 16; dx++) {
            for(int dz = 1; dz < 16; dz++) {
                for(int dy = 1; dy < 8; dy++) {
                    if(bitset[(dx * 16 + dz) * 8 + dy]) {
                        BlockPos pos = start.add(dx, dy - 1, dz);
                        if(world.getBlockState(pos).getBlock().isIn(UplandsBlockTags.PLANTABLE_ON) &&
                            world.getLightLevel(LightType.SKY, start.add(dx, dy, dz)) > 0) {
                            world.setBlockState(pos, BlockRegistry.UPLANDER_GRASS.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        if(config.state.getMaterial() == Material.LAVA) {
            for(int dx = 0; dx < 16; dx++) {
                for(int dz = 0; dz < 16; dz++) {
                    for(int dy = 0; dy < 8; dy++) {
                        if(bitsetCheck(bitset, dx, dz, dy) && (dy < 4 || (random.nextInt(2) != 0))
                            && world.getBlockState(start.add(dx, dy, dz)).getMaterial().isSolid()) {
                            world.setBlockState(
                                start.add(dx, dy, dz),
                                BlockRegistry.UPLANDER_STONE.getDefaultState(),
                                2
                            );
                        }
                    }
                }
            }
        }
        if(config.state.getMaterial() == Material.WATER) {
            for(int dx = 0; dx < 16; dx++) {
                for(int dz = 0; dz < 16; dz++) {
                    BlockPos pos = start.add(dx, 4, dz);
                    if(world.getBiome(pos).canSetIce(world, pos, false)) {
                        world.setBlockState(pos, Blocks.ICE.getDefaultState(), 2);
                    }
                }
            }
        }
        return true;
    }

    private boolean bitsetCheck(boolean[] bitset, int dx, int dz, int dy) {
        return !bitset[(dx * 16 + dz) * 8 + dy] && (
            dx < 15 && bitset[((dx + 1) * 16 + dz) * 8 + dy]
                || dx > 0 && bitset[((dx - 1) * 16 + dz) * 8 + dy]
                || dz < 15 && bitset[(dx * 16 + dz + 1) * 8 + dy]
                || dz > 0 && bitset[(dx * 16 + dz - 1) * 8 + dy]
                || dy < 7 && bitset[(dx * 16 + dz) * 8 + dy + 1]
                || dy > 0 && bitset[(dx * 16 + dz) * 8 + dy - 1]
        );
    }
}
