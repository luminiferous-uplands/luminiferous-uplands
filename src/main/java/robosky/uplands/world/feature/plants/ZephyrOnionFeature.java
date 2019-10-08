package robosky.uplands.world.feature.plants;

import com.mojang.datafixers.Dynamic;
import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FlowerFeature;
import robosky.uplands.block.BlockRegistry;
import robosky.uplands.block.ZephyrOnionBlock;

public class ZephyrOnionFeature extends FlowerFeature {
    public ZephyrOnionFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1) {
        super(function_1);
    }

    public BlockState getFlowerToPlace(Random random_1, BlockPos blockPos_1) {
        return BlockRegistry.ZEPHYR_ONION_CROP_BLOCK().getDefaultState().with(ZephyrOnionBlock.AGE, random_1.nextInt(5));
    }

    public boolean generate(IWorld iWorld_1, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator_1, Random random_1, BlockPos blockPos_1, DefaultFeatureConfig defaultFeatureConfig_1) {
        for(BlockState blockState_1 = iWorld_1.getBlockState(blockPos_1); (blockState_1.isAir() || blockState_1.matches(BlockTags.LEAVES)) && blockPos_1.getY() > 0; blockState_1 = iWorld_1.getBlockState(blockPos_1)) {
            blockPos_1 = blockPos_1.down();
        }

        int int_1 = 0;

        System.out.println("Trying to put something down at " + blockPos_1);
        for(int int_2 = 0; int_2 < 8; ++int_2) {
            BlockPos blockPos_2 = blockPos_1.add(random_1.nextInt(8) - random_1.nextInt(8), random_1.nextInt(4) - random_1.nextInt(4), random_1.nextInt(8) - random_1.nextInt(8));

            if (iWorld_1.isAir(blockPos_2) && iWorld_1.getBlockState(blockPos_2.down()) == BlockRegistry.UPLANDER_GRASS().getDefaultState()) {
                System.out.println("Putting something down!");
                iWorld_1.setBlockState(blockPos_2.down(), BlockRegistry.UPLANDER_FARMLAND().getDefaultState(), 2);
                iWorld_1.setBlockState(blockPos_2, BlockRegistry.ZEPHYR_ONION_CROP_BLOCK().getDefaultState(), 2);
                ++int_1;
            }
        }

        return int_1 > 0;
    }
}