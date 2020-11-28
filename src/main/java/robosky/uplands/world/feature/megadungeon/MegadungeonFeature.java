package robosky.uplands.world.feature.megadungeon;

import robosky.structurehelpers.structure.pool.ExtendedStructureFeature;
import robosky.structurehelpers.structure.pool.ExtendedStructurePoolFeatureConfig;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public final class MegadungeonFeature extends ExtendedStructureFeature {

    public MegadungeonFeature() {
        super(0, false, true);
    }

    @Override
    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed, ChunkRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, ExtendedStructurePoolFeatureConfig config) {
        // the gradient of the world surface must be shallow enough
        final int MAX_GRADIENT_BLOCKS = 6;
        // iterate over the generation region; centered about the chunk origin
        for(int posX = chunkX * 16 - 7, maxPosX = posX + 15; posX < maxPosX; posX++) {
            for(int posZ = chunkZ * 16 - 7, maxPosZ = posZ + 15; posZ < maxPosZ; posZ++) {
                int height = chunkGenerator.getHeight(posX, posZ, Heightmap.Type.WORLD_SURFACE_WG);
                // don't spawn anything at the void!
                if(height < 30) {
                    return false;
                }
                // compute the magnitude of the local gradient
                int dx = height - chunkGenerator.getHeight(posX - 1, posZ, Heightmap.Type.WORLD_SURFACE_WG);
                int dz = height - chunkGenerator.getHeight(posX, posZ - 1, Heightmap.Type.WORLD_SURFACE_WG);
                // gradient in blocks = [ dx ; dz ]
                int gradSq = dx * dx + dz * dz;
                if(gradSq > (MAX_GRADIENT_BLOCKS * MAX_GRADIENT_BLOCKS)) {
                    return false;
                }
            }
        }
        return super.shouldStartAt(chunkGenerator, biomeSource, worldSeed, random, chunkX, chunkZ, biome, chunkPos, config);
    }
}

