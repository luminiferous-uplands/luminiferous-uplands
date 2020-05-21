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

    private final SimplexNoiseSampler noise;
    private final PerlinNoiseSampler influenceNoise;
    public UplandsBiomeSource(long seed) {
        super(ImmutableSet.of(BiomeRegistry.UPLANDS_AUTUMN_BIOME));
        biomeSampler = UplandsLayers.build(seed);
        this.noise = new SimplexNoiseSampler(new Random(seed + 3));
        this.influenceNoise = new PerlinNoiseSampler(new Random(seed - 3));
    }

    @Override
    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        float noise = this.getNoiseRange((x >> 2) * 2 + 1, (z >> 2)* 2 + 1);
        if (noise > -10) {
            return biomeSampler.sample(x, z);
        } else {
            return BiomeRegistry.UPLANDS_VOID_BIOME;
        }
    }

    @Override
    public float getNoiseRange(int i, int j) {
        int k = i / 2;
        int l = j / 2;
        int m = i % 2;
        int n = j % 2;
        float f = 100.0F - MathHelper.sqrt((float) (i * i + j * j)) * 8.0F;
        f = MathHelper.clamp(f, -100.0F, 80.0F);
        for(int o = -12; o <= 12; ++o) {
            for(int p = -12; p <= 12; ++p) {
                long q = k + o;
                long r = l + p;
                double d = this.noise.sample(((double)q), ((double)r));
                if (d < -0.925) {
                    float g = (MathHelper.abs((float)q) * 3439.0F + MathHelper.abs((float)r) * 147.0F) % 13.0F + 9.0F;
                    float h = (float)(m - o * 2);
                    float s = (float)(n - p * 2);
                    float t = 100.0F - MathHelper.sqrt(h * h + s * s) * g;

                    t += MathHelper.clamp(influenceNoise.sample(q / 50.0, 30, r / 50.0, 0, 0), -1, 1) * 64;

                    t = MathHelper.clamp(t, -100.0F, 80.0F);
                    f = Math.max(f, t);
                }
            }
        }
        return f;
    }
}
