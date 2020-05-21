package robosky.uplands.world.gen;

import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;

import java.util.Random;

public class UplandsChunkGenerator extends SurfaceChunkGenerator<UplandsChunkGenConfig> {
    private static final float[] BIOME_WEIGHT_TABLE = Util.make(new float[25], (fs) -> {
        for(int i = -2; i <= 2; ++i) {
            for(int j = -2; j <= 2; ++j) {
                float f = 10.0F / MathHelper.sqrt((float)(i * i + j * j) + 0.2F);
                fs[i + 2 + (j + 2) * 5] = f;
            }
        }

    });

    private final OctavePerlinNoiseSampler depthNoiseSampler;
    private final SimplexNoiseSampler noise;

    public UplandsChunkGenerator(IWorld world, BiomeSource biomeSource) {
        super(world, biomeSource, 8, 4, 128, new UplandsChunkGenConfig(), true);
        this.random.consume(2620);
        this.depthNoiseSampler = new OctavePerlinNoiseSampler(this.random, 15, 0);
        this.noise = new SimplexNoiseSampler(this.random);
    }

    protected void sampleNoiseColumn(double[] buffer, int x, int z) {
        //TODO: improve these values
        this.sampleNoiseColumn(buffer, x, z, 684.412D, 684.412D, 684.412D / 40, 684.412D / 80, 3, -10);
    }

    protected double[] computeNoiseRange(int x, int z) {
        double[] ds = new double[2];
        float scaleTotal = 0.0F;
        float depthTotal = 0.0F;
        float totalWeight = 0.0F;
        int seaLevel = this.getSeaLevel();
        float centerDepth = this.biomeSource.getBiomeForNoiseGen(x, seaLevel, z).getDepth();

        for(int x0 = -2; x0 <= 2; ++x0) {
            for(int z0 = -2; z0 <= 2; ++z0) {
                Biome biome = this.biomeSource.getBiomeForNoiseGen(x + x0, seaLevel, z + z0);
                float depth = biome.getDepth();
                float scale = biome.getScale();

                float weight = BIOME_WEIGHT_TABLE[x0 + 2 + (z0 + 2) * 5];
                if (biome.getDepth() > centerDepth) {
                    weight /= 2.0F;
                }

                scaleTotal += scale * weight;
                depthTotal += depth * weight;
                totalWeight += weight;
            }
        }

        scaleTotal /= totalWeight;
        depthTotal /= totalWeight;
        ds[0] = depthTotal + sampleExtraDepthNoise(x, z);
        ds[1] = scaleTotal;
        return ds;
    }

    public float getNoiseRange(int i, int j) {
        int k = i / 2;
        int l = j / 2;
        int m = i % 2;
        int n = j % 2;
        float f = 100.0F - MathHelper.sqrt((float) (i * i + j * j)) * 8.0F;
        f = MathHelper.clamp(f, -100.0F, 80.0F);
        for(int o = -12; o <= 12; ++o) {
            for(int p = -12; p <= 12; ++p) {
                long q = (long)(k + o);
                long r = (long)(l + p);
                if (q * q + r * r > 4096L && this.noise.sample((double)q, (double)r) < -0.8999999761581421D) {
                    float g = (MathHelper.abs((float)q) * 3439.0F + MathHelper.abs((float)r) * 147.0F) % 13.0F + 9.0F;
                    float h = (float)(m - o * 2);
                    float s = (float)(n - p * 2);
                    float t = 100.0F - MathHelper.sqrt(h * h + s * s) * g;
                    t = MathHelper.clamp(t, -100.0F, 80.0F);
                    f = Math.max(f, t);
                }
            }
        }
        return f;
    }

    protected double computeNoiseFalloff(double depth, double scale, int y) {
        return 12 - (15 * depth);
    }

    // top interpolation start
    protected double method_16409() {
        return this.getNoiseSizeY() / 2.0;
    }

    // bottom interpolation start
    protected double method_16410() {
        return 4.0D;
    }

    public int getSpawnHeight() {
        return 130;
    }

    public int getSeaLevel() {
        return 0;
    }

    private double sampleExtraDepthNoise(int x, int y) {
        double d = this.depthNoiseSampler.sample((double)(x * 200), 10.0D, (double)(y * 200), 1.0D, 0.0D, true) * 65535.0D / 1000.0D;
        if (d < 0.0D) {
            d = -d * 0.3D;
        }

        d = d * 3.0D - 2.0D;
        if (d < 0.0D) {
            d /= 28.0D;
        } else {
            if (d > 1.0D) {
                d = 1.0D;
            }

            d /= 40.0D;
        }

        return d;
    }

    @Override
    protected void buildBedrock(Chunk chunk, Random random) {

    }
}
