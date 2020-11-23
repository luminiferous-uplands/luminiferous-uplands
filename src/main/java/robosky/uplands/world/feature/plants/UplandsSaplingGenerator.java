package robosky.uplands.world.feature.plants;

import java.util.Random;
import javax.annotation.Nullable;

import robosky.uplands.block.BlockRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;

public abstract class UplandsSaplingGenerator {
    public boolean generate(StructureWorldAccess world, BlockPos pos, BlockState state, Random rand, ConfiguredFeature<?, ?> feature) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
        if(feature.generate(world, ((ServerChunkManager)world.getChunkManager()).getChunkGenerator(),
            rand, pos)) {
            return true;
        } else {
            world.setBlockState(pos, state, 4);
            return false;
        }
    }

    @Nullable
    protected abstract AbstractUplandsTree<SkyrootTreeFeatureConfig> createTreeFeature(Random rand);

    public static class SkyrootSaplingGenerator extends UplandsSaplingGenerator {
        @Nullable
        @Override
        protected SkyrootTreeFeature createTreeFeature(Random rand) {
            return new SkyrootTreeFeature(SkyrootTreeFeatureConfig.CODEC, true);
        }
    }
}
