package robosky.uplands.world.feature.minidungeons;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;

public class MinidungeonFeature extends StructureFeature<MinidungeonFeatureConfig> {

    public MinidungeonFeature() {
        super(MinidungeonFeatureConfig.CODEC);
    }

    public static final class Start extends StructureStart<MinidungeonFeatureConfig> {

        Start(StructureFeature<MinidungeonFeatureConfig> feature, int chunkX, int chunkZ, BlockBox bbox, int references, long seed) {
            super(feature, chunkX, chunkZ, bbox, references, seed);
        }

        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator generator, StructureManager mgr, int chunkX, int chunkZ, Biome biome, MinidungeonFeatureConfig conf) {
            int x = chunkX * 16;
            int z = chunkZ * 16;
            BlockPos startingPos = new BlockPos(x, 0, z);
            BlockRotation rotation = BlockRotation.NONE;
            MinidungeonGenerator.addParts(mgr, startingPos, rotation, this.children, conf);
            this.setBoundingBoxFromChildren();
        }
    }

    @Override
    public StructureFeature.StructureStartFactory<MinidungeonFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }
}
