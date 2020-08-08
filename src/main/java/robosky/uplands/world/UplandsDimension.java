package robosky.uplands.world;

import robosky.uplands.world.gen.UplandsBiomeSource;
import robosky.uplands.world.gen.UplandsChunkGenerator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class UplandsDimension extends Dimension {

    private final DimensionType dimensionType;

    public UplandsDimension(World world, DimensionType dimensionType) {
        super(world, dimensionType, 0.0f);
        this.dimensionType = dimensionType;
    }

    @Override
    public BlockPos getForcedSpawnPoint() {
        return new BlockPos(0, 130, 0);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        return new UplandsChunkGenerator(world, new UplandsBiomeSource(world.getSeed()));
    }


    @Override
    public BlockPos getSpawningBlockInChunk(ChunkPos pos, boolean b) {
        return null;
    }

    @Override
    public BlockPos getTopSpawningBlockPosition(int x, int z, boolean b) {
        return null;
    }

    @Override
    public float getSkyAngle(long worldTime, float partialTicks) {
        double a = MathHelper.fractionalPart(worldTime / 24000.0D - 0.25D);
        return (float)(a * 2.0D + (0.5D - Math.cos(a * 3.141592653589793D) / 2.0D)) / 3.0F;
    }

    @Override
    public float getCloudHeight() {
        return 8;
    }

    @Override
    public boolean hasVisibleSky() {
        return true;
    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        float multiplier = MathHelper.clamp(MathHelper.cos(celestialAngle * 6.2831855F) * 2.0F + 0.5F, 0.0F, 1.0F);

        float r = 0.6019608F;
        float g = 0.6019608F;
        float b = 0.627451F;

        r *= multiplier * 0.94F + 0.06F;
        g *= multiplier * 0.94F + 0.06F;
        b *= multiplier * 0.91F + 0.09F;

        return new Vec3d(r, g, b);
    }

    @Override
    public boolean canPlayersSleep() {
        return true;
    }

    @Override
    public boolean isFogThick(int x, int z) {
        return false;
    }

    @Override
    public double getHorizonShadingRatio() {
        return 1;
    }

    @Override
    public DimensionType getType() {
        return dimensionType;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public float[] getBackgroundColor(float celestialAngle, float partialTicks) {
        return null;
    }
}
