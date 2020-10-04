package robosky.uplands.world.feature;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

import robosky.uplands.block.BlockRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;

public final class UplandsOreFeature extends Feature<UplandsOreFeatureConfig> {

    public static final UplandsOreFeature INSTANCE = new UplandsOreFeature();

    private UplandsOreFeature() {
        super(UplandsOreFeatureConfig::deserialize);
    }

    Clump[] SPHERES = {
        Clump.of(1),
        Clump.of(2),
        Clump.of(3),
        Clump.of(4),
        Clump.of(5),
        Clump.of(6),
        Clump.of(7),
        Clump.of(8),
        Clump.of(9),
    };

    @Override
    public boolean generate(WorldAccess world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random rand, BlockPos pos, UplandsOreFeatureConfig config) {
        Chunk toGenerateIn = world.getChunk(pos);
        int overbleed = 0;
        int radius = (int)Math.log(config.getSize()) + 1;
        if(radius > 7) radius = 7;
        for(int j = 0; j < SPHERES.length; j++) {
            Clump clump = SPHERES[j];
            if(clump.size() >= config.getSize()) {
                radius = j + 1;
                break;
            }
        }
        int clusterX = rand.nextInt(16 + overbleed - (radius * 2)) + radius;
        int clusterZ = rand.nextInt(16 + overbleed - (radius * 2)) + radius;
        int heightRange = config.getMaxHeight() - config.getMinHeight();
        if(heightRange < 1) {
            heightRange = 1;
        }
        int clusterY = rand.nextInt(heightRange) + config.getMinHeight();
        clusterX += toGenerateIn.getPos().getStartX();
        clusterZ += toGenerateIn.getPos().getStartZ();
        int generatedThisCluster = generateVeinPartGaussianClump(world, clusterX, clusterY, clusterZ,
            config.getSize(), radius, Collections.singleton(config.getState()), 85, rand);
        return generatedThisCluster > 0;
    }

    private int generateVeinPartGaussianClump(WorldAccess world, int x, int y, int z, int clumpSize, int radius, Set<BlockState> states, int density, Random rand) {
        int radIndex = radius - 1;
        Clump clump;
        if(radIndex < SPHERES.length) {
            clump = SPHERES[radIndex].copy();
        } else {
            clump = Clump.of(radius);
        }
        //int rad2 = radius * radius;
        BlockState[] blocks = states.toArray(new BlockState[0]);
        int replaced = 0;
        for(int unused = 0; unused < clump.size(); unused++) {
            if(!clump.isEmpty()) {
                BlockPos pos = clump.removeGaussian(rand, x, y, z);
                if(replace(world, pos.getX(), pos.getY(), pos.getZ(), blocks, rand)) {
                    replaced += 1;
                    if(replaced >= clumpSize) return replaced;
                }
            }
        }
        return replaced;
    }

    private int generateVeinPart(WorldAccess world, int x, int y, int z, int clumpSize, int radius, Set<BlockState> states, int density, Random rand) {
        int rad2 = radius * radius;
        BlockState[] blocks = states.toArray(new BlockState[0]);
        int replaced = 0;
        for(int zi = (z - radius); zi <= (z + radius); zi++) {
            for(int yi = (y - radius); yi <= (y + radius); yi++) {
                for(int xi = (x - radius); xi <= (x + radius); xi++) {
                    if(yi >= 0 && yi <= 255) {
                        if(rand.nextInt(100) <= density) {
                            int dx = xi - x;
                            int dy = yi - y;
                            int dz = zi - z;
                            int dist2 = dx * dx + dy * dy + dz * dz;
                            if(dist2 <= rad2) {
                                if(replace(world, xi, yi, zi, blocks, rand)) {
                                    replaced += 1;
                                    if(replaced >= clumpSize) return replaced;
                                }
                            }
                        }
                    }
                }
            }
        }

        return replaced;
    }

    private boolean replace(WorldAccess world, int x, int y, int z, BlockState[] states, Random rand) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState toReplace = world.getBlockState(pos);
        BlockState replaceWith = states[rand.nextInt(states.length)];
        if(toReplace.getBlock() != BlockRegistry.UPLANDER_STONE) {
            return false;
        }
        world.setBlockState(pos, replaceWith, 3);
        return true;
    }

    private int generateVeinPartGaussian(WorldAccess world, int x, int y, int z, int clumpSize, int radius, Set<BlockState> states, int density, Random rand) {
        int rad2 = radius * radius;
        BlockState[] blocks = states.toArray(new BlockState[0]);
        int replaced = 0;
        for(int unused = 0; unused < 200; unused++) {
            int xi = (int)(x + (rand.nextGaussian() * radius));
            int yi = (int)(y + (rand.nextGaussian() * radius));
            int zi = (int)(z + (rand.nextGaussian() * radius));
            int dx = xi - x;
            int dy = yi - y;
            int dz = zi - z;
            int dist2 = dx * dx + dy * dy + dz * dz;
            if(dist2 <= rad2) if(replace(world, xi, yi, zi, blocks, rand)) {
                replaced += 1;
                if(replaced >= clumpSize) return replaced;
            }
        }
        return replaced;
    }
}
