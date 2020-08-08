package robosky.uplands.world.feature.plants;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;

public abstract class UplandsSaplingGenerator {
    public boolean generate(IWorld world, BlockPos pos, BlockState state, Random rand) {
        AbstractUplandsTree<DefaultFeatureConfig> abstractTreeFeature_1 = this.createTreeFeature(rand);
        if(abstractTreeFeature_1 == null) {
            return false;
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
            if(abstractTreeFeature_1.generate(world, ((ServerChunkManager)world.getChunkManager()).getChunkGenerator(), rand, pos, FeatureConfig.DEFAULT)) {
                return true;
            } else {
                world.setBlockState(pos, state, 4);
                return false;
            }
        }
    }

    @Nullable
    protected abstract AbstractUplandsTree<DefaultFeatureConfig> createTreeFeature(Random rand);

    public static class SkyrootSaplingGenerator extends UplandsSaplingGenerator {
        @Nullable
        @Override
        protected SkyrootTreeFeature createTreeFeature(Random rand) {
            return new SkyrootTreeFeature(DefaultFeatureConfig::deserialize, true);
        }
    }
}
