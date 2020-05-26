package robosky.uplands.world.gen;

import com.google.common.collect.ImmutableSet;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;
import robosky.uplands.world.biome.BiomeRegistry;
import robosky.uplands.world.layer.UplandsLayers;

import java.util.Random;
import java.util.Set;

public class UplandsBiomeSource extends BiomeSource {
    private final BiomeLayerSampler biomeSampler;
    public UplandsBiomeSource(long seed) {
        super(ImmutableSet.of(BiomeRegistry.UPLANDS_AUTUMN_BIOME));
        biomeSampler = UplandsLayers.build(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        return biomeSampler.sample(x, z);
    }
}
