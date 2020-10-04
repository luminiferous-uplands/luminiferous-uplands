package robosky.uplands.world.feature.plants;

import java.util.Random;

import com.mojang.serialization.Codec;
import robosky.uplands.block.BlockRegistry;
import robosky.uplands.block.WaterChestnutBlock;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class WaterChestnutFeature extends Feature<DefaultFeatureConfig> {
    public WaterChestnutFeature(Codec<DefaultFeatureConfig> function_1) {
        super(function_1);
    }

    public boolean generate(StructureWorldAccess iWorld_1, ChunkGenerator chunkGenerator_1, Random random_1, BlockPos blockPos_1, DefaultFeatureConfig defaultFeatureConfig_1) {
        BlockPos blockPos_3;
        for(BlockPos blockPos_2 = blockPos_1; blockPos_2.getY() > 0; blockPos_2 = blockPos_3) {
            blockPos_3 = blockPos_2.down();
            if(!iWorld_1.isAir(blockPos_3)) {
                break;
            }
        }

        for(int i = 0; i < 15; ++i) {
            BlockPos blockPos_4 = blockPos_1.add(random_1.nextInt(8) - random_1.nextInt(8), random_1.nextInt(4) - random_1.nextInt(4), random_1.nextInt(8) - random_1.nextInt(8));
            BlockState blockState_1 = BlockRegistry.WATER_CHESTNUT_CROP_BLOCK.getDefaultState().with(WaterChestnutBlock.AGE, 7);
            if(iWorld_1.isAir(blockPos_4) && blockState_1.canPlaceAt(iWorld_1, blockPos_4)) {
                iWorld_1.setBlockState(blockPos_4, blockState_1, 2);
            }
        }

        return true;
    }
}
