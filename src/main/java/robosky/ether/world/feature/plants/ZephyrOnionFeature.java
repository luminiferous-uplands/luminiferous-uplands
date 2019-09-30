package robosky.ether.world.feature.plants;

import com.mojang.datafixers.Dynamic;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FlowerFeature;
import robosky.ether.block.BlockRegistry;
import robosky.ether.block.ZephyrOnionBlock;

public class ZephyrOnionFeature extends FlowerFeature {
    public ZephyrOnionFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1) {
        super(function_1);
    }

    public BlockState getFlowerToPlace(Random random_1, BlockPos blockPos_1) {
        return BlockRegistry.ZEPHYR_ONION_CROP_BLOCK().getDefaultState().with(ZephyrOnionBlock.AGE, random_1.nextInt(5));
    }

    public boolean generate(IWorld iWorld_1, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator_1, Random random_1, BlockPos blockPos_1, DefaultFeatureConfig defaultFeatureConfig_1) {
        BlockState blockState_1 = this.getFlowerToPlace(random_1, blockPos_1);
        int int_1 = 0;

        for(int int_2 = 0; int_2 < 64; ++int_2) {
            BlockPos blockPos_2 = blockPos_1.add(random_1.nextInt(8) - random_1.nextInt(8), random_1.nextInt(4) - random_1.nextInt(4), random_1.nextInt(8) - random_1.nextInt(8));
            if (iWorld_1.isAir(blockPos_2) && blockPos_2.getY() < 255 && iWorld_1.getBlockState(blockPos_2.down()) == BlockRegistry.UPLANDER_GRASS().getDefaultState()) {
                iWorld_1.setBlockState(blockPos_2.down(), BlockRegistry.UPLANDER_FARMLAND().getDefaultState(), 2);
                iWorld_1.setBlockState(blockPos_2, blockState_1, 2);
                ++int_1;
            }
        }

        return int_1 > 0;
    }
}