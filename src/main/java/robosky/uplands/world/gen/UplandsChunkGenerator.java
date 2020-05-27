package robosky.uplands.world.gen;

import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import robosky.uplands.noise.OpenSimplexNoise;
import robosky.uplands.world.biome.UplandsBiome;

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
    private final OpenSimplexNoise falloffNoise;

    public UplandsChunkGenerator(IWorld world, BiomeSource biomeSource) {
        super(world, biomeSource, 8, 8, 256, new UplandsChunkGenConfig(), true);
        this.random.consume(2620);
        this.depthNoiseSampler = new OctavePerlinNoiseSampler(this.random, 15, 0);
        falloffNoise = new OpenSimplexNoise(random.nextLong());
    }

    protected void sampleNoiseColumn(double[] buffer, int x, int z) {
        //TODO: improve these values
        this.sampleNoiseColumn(buffer, x, z, 684.412D, 684.412D, 684.412D / 80, 684.412D / 160, 3, -10);
    }

    protected double[] computeNoiseRange(int x, int z) {
        double[] ds = new double[2];
        float scaleTotal = 0.0F;
        float depthTotal = 0.0F;
        double islandSizeTotal = 0.0;
        float totalWeight = 0.0F;
        int seaLevel = this.getSeaLevel();
        float centerDepth = this.biomeSource.getBiomeForNoiseGen(x, seaLevel, z).getDepth();

        for(int x0 = -2; x0 <= 2; ++x0) {
            for(int z0 = -2; z0 <= 2; ++z0) {
                Biome biome = this.biomeSource.getBiomeForNoiseGen(x + x0, seaLevel, z + z0);
                float depth = biome.getDepth();
                float scale = biome.getScale();
                double islandSize;
                if (biome instanceof UplandsBiome) {
                    islandSize = ((UplandsBiome)biome).getIslandSize();
                } else {
                    islandSize = 50;
                }

                float weight = BIOME_WEIGHT_TABLE[x0 + 2 + (z0 + 2) * 5]  / (depth + 2.0F);
                if (biome.getDepth() > centerDepth) {
                    weight /= 2.0F;
                }

                scaleTotal += scale * weight;
                depthTotal += depth * weight;
                islandSizeTotal += islandSize * weight;
                totalWeight += weight;
            }
        }

        scaleTotal /= totalWeight;
        depthTotal /= totalWeight;
        islandSizeTotal /= totalWeight;
        ds[0] = sampleExtraDepthNoise(x / islandSizeTotal, z / islandSizeTotal) * 5;
        ds[1] = falloffNoise.sample(x / 64.0, z / 64.0) * 4;
        return ds;
    }

    protected double computeNoiseFalloff(double depth, double scale, int y) {
        return (12 - (15 * depth)) + (y / 4.0) - 4;
    }

    // top interpolation start
    protected double method_16409() {
        return this.getNoiseSizeY() / 4.0;
    }

    // bottom interpolation start
    protected double method_16410() {
        return 8.0D;
    }

    public int getSpawnHeight() {
        return 130;
    }

    public int getSeaLevel() {
        return 0;
    }

    private double sampleExtraDepthNoise(double x, double z) {
        double d = this.depthNoiseSampler.sample((double)(x), 10.0D, (double)(z), 1.0D, 0.0D, true) * 65535.0D / 1000.0D;
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
