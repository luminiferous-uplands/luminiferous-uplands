package robosky.uplands.world.feature.minidungeons;

import java.util.Random;

import robosky.uplands.UplandsMod;
import robosky.uplands.world.feature.FeatureRegistry;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;

public class MinidungeonFeature extends StructureFeature<DefaultFeatureConfig> {

    private final MinidungeonFeatureConfig conf;

    public MinidungeonFeature(MinidungeonFeatureConfig conf) {
        super(DefaultFeatureConfig::deserialize);
        this.conf = conf;
    }

    public final class Start extends StructureStart {

        Start(StructureFeature<?> feature, int chunkX, int chunkZ, BlockBox bbox, int references, long seed) {
            super(feature, chunkX, chunkZ, bbox, references, seed);
        }

        @Override
        public void initialize(ChunkGenerator<?> generator, StructureManager mgr, int chunkX, int chunkZ, Biome biome) {
            int x = chunkX * 16;
            int z = chunkZ * 16;
            BlockPos startingPos = new BlockPos(x, 0, z);
            BlockRotation rotation = BlockRotation.NONE;
            MinidungeonGenerator.addParts(mgr, startingPos, rotation, this.children, conf);
            this.setBoundingBoxFromChildren();
        }
    }

    public MinidungeonFeature register(String name) {
        MinidungeonFeature f = FeatureRegistry.register(name, this);
        Registry.register(Registry.STRUCTURE_FEATURE, UplandsMod.id(name), this);
        Feature.STRUCTURES.put(conf.getName(), this);
        StructurePieceType[] tpe = new StructurePieceType[1];
        tpe[0] = Registry.register(Registry.STRUCTURE_PIECE, conf.getTemplate(), (var1, var2) -> new MinidungeonGenerator.Piece(tpe[0], var1, var2));
        return f;
    }

    @Override
    public boolean shouldStartAt(BiomeAccess world, ChunkGenerator<?> generator, Random rand, int var4, int var5, Biome biome) {
        return true;
    }

    @Override
    public StructureFeature.StructureStartFactory getStructureStartFactory() {
        return Start::new;
    }

    @Override
    public String getName() {
        return conf.getName();
    }

    @Override
    public int getRadius() {
        return 8;
    }
}
