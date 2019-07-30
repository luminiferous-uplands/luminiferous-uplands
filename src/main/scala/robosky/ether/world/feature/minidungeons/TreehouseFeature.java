package robosky.ether.world.feature.minidungeons;

import com.mojang.datafixers.Dynamic;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;
import java.util.function.Function;

public class TreehouseFeature extends AbstractTempleFeature<DefaultFeatureConfig> {
    public TreehouseFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1) {
        super(function_1);
    }

    @Override
    protected int getSeedModifier() {
        return 0;
    }

    @Override
    public boolean shouldStartAt(ChunkGenerator<?> chunkGenerator_1, Random random_1, int int_1, int int_2) {
        return true;
    }

    @Override
    public StructureStartFactory getStructureStartFactory() {
        return null;
    }

    @Override
    public String getName() {
        return "Treehouse";
    }

    @Override
    public int getRadius() {
        return 8;
    }

    public static class TreehouseGeneratorStart extends StructureStart {
        public TreehouseGeneratorStart (StructureFeature<?> structureFeature_1, int int_1, int int_2, Biome biome_1, MutableIntBoundingBox mutableIntBoundingBox_1, int int_3, long long_1) {
            super(structureFeature_1, int_1, int_2, biome_1, mutableIntBoundingBox_1, int_3, long_1);
        }
        @Override
        public void initialize(ChunkGenerator<?> chunkGenerator, StructureManager structureManager, int chunkX, int chunkZ, Biome biome) {
            DefaultFeatureConfig defaultFeatureConfig = chunkGenerator.getStructureConfig(biome, MyMainclass.myFeature);
            int x = chunkX * 16;
            int z = chunkZ * 16;
            BlockPos startingPos = new BlockPos(x, 0, z);
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
            TreehouseGenerator.addParts(structureManager, startingPos, rotation, this.children, this.random, defaultFeatureConfig);
            this.setBoundingBoxFromChildren();
        }
    }
}
