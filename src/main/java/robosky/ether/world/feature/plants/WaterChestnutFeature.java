package robosky.ether.world.feature.plants;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import robosky.ether.block.BlockRegistry;
import robosky.ether.block.WaterChestnutBlock;

import java.util.Random;
import java.util.function.Function;

public class WaterChestnutFeature {
    public boolean generate(IWorld iWorld_1, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator_1, Random random_1, BlockPos blockPos_1, DefaultFeatureConfig defaultFeatureConfig_1) {
        BlockPos blockPos_3;
        for(BlockPos blockPos_2 = blockPos_1; blockPos_2.getY() > 0; blockPos_2 = blockPos_3) {
            blockPos_3 = blockPos_2.down();
            if (!iWorld_1.isAir(blockPos_3)) {
                break;
            }
        }

        for(int int_1 = 0; int_1 < 10; ++int_1) {
            BlockPos blockPos_4 = blockPos_1.add(random_1.nextInt(8) - random_1.nextInt(8), random_1.nextInt(4) - random_1.nextInt(4), random_1.nextInt(8) - random_1.nextInt(8));
            BlockState blockState_1 = BlockRegistry.WATER_CHESTNUT_CROP_BLOCK().getDefaultState().with(WaterChestnutBlock.AGE, 7);
            if (iWorld_1.isAir(blockPos_4) && blockState_1.canPlaceAt(iWorld_1, blockPos_4)) {
                iWorld_1.setBlockState(blockPos_4, blockState_1, 2);
            }
        }

        return true;
    }
}
