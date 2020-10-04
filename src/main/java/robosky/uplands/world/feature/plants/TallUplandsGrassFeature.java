package robosky.uplands.world.feature.plants;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;

public class TallUplandsGrassFeature extends Feature<RandomPatchFeatureConfig> {
    public TallUplandsGrassFeature(Codec<RandomPatchFeatureConfig> function_1) {
        super(function_1);
    }

    public boolean generate(StructureWorldAccess iWorld_1, ChunkGenerator chunkGenerator_1, Random random_1, BlockPos blockPos_1, RandomPatchFeatureConfig grassFeatureConfig_1) {
        for(BlockState blockState_1 = iWorld_1.getBlockState(blockPos_1); (blockState_1.isAir() || blockState_1.isIn(BlockTags.LEAVES)) && blockPos_1.getY() > 0; blockState_1 = iWorld_1.getBlockState(blockPos_1)) {
            blockPos_1 = blockPos_1.down();
        }

        int int_1 = 0;

        for(int int_2 = 0; int_2 < 128; ++int_2) {
            BlockPos blockPos_2 = blockPos_1.add(random_1.nextInt(8) - random_1.nextInt(8), random_1.nextInt(4) - random_1.nextInt(4), random_1.nextInt(8) - random_1.nextInt(8));
            BlockState state = grassFeatureConfig_1.stateProvider.getBlockState(random_1, blockPos_2);
            if(iWorld_1.isAir(blockPos_2) && state.canPlaceAt(iWorld_1, blockPos_2)) {
                iWorld_1.setBlockState(blockPos_2, state, 2);
                ++int_1;
            }
        }

        return int_1 > 0;
    }
}
