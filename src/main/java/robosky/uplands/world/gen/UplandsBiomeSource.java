package robosky.uplands.world.gen;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import robosky.uplands.world.biome.BiomeRegistry;
import robosky.uplands.world.layer.UplandsLayers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;

public class UplandsBiomeSource extends BiomeSource {

    private final BiomeLayerSampler biomeSampler;
    private final Registry<Biome> registry;

    public UplandsBiomeSource(long seed, Registry<Biome> registry) {
        super(ImmutableList.of(registry.get(BiomeRegistry.UPLANDS_AUTUMN)));
        biomeSampler = UplandsLayers.build(seed);
        this.registry = registry;
    }

    @Override
    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        return biomeSampler.sample(this.registry, x, z);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        throw new UnsupportedOperationException("getCodec");
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return new UplandsBiomeSource(seed, this.registry);
    }
}
