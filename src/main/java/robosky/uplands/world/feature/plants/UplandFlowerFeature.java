package robosky.uplands.world.feature.plants;

import com.mojang.datafixers.Dynamic;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FlowerFeature;
import robosky.uplands.block.BlockRegistry;

public class UplandFlowerFeature extends FlowerFeature {
    private static final Block[] FLOWERS;

    public UplandFlowerFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1) {
        super(function_1);
    }

    public BlockState getFlowerToPlace(Random random_1, BlockPos blockPos_1) {
        double double_1 = MathHelper.clamp((1.0D + Biome.FOLIAGE_NOISE.sample((double)blockPos_1.getX() / 48.0D, (double)blockPos_1.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
        Block block_1 = FLOWERS[(int)(double_1 * (double)FLOWERS.length)];
        return block_1.getDefaultState();
    }

    public boolean generate(IWorld iWorld_1, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator_1, Random random_1, BlockPos blockPos_1, DefaultFeatureConfig defaultFeatureConfig_1) {
        BlockState blockState_1 = this.getFlowerToPlace(random_1, blockPos_1);
        int int_1 = 0;

        for(int int_2 = 0; int_2 < 64; ++int_2) {
            BlockPos blockPos_2 = blockPos_1.add(random_1.nextInt(8) - random_1.nextInt(8), random_1.nextInt(4) - random_1.nextInt(4), random_1.nextInt(8) - random_1.nextInt(8));
            if (iWorld_1.isAir(blockPos_2) && blockPos_2.getY() < 255 && blockState_1.canPlaceAt(iWorld_1, blockPos_2)) {
                iWorld_1.setBlockState(blockPos_2, blockState_1, 2);
                ++int_1;
            }
        }

        return int_1 > 0;
    }

    static {
        FLOWERS = new Block[]{BlockRegistry.CLOUD_DAISIES()};
    }
}