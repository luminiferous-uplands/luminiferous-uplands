package robosky.uplands.world.feature.plants;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public final class UplandsSaplingGenerator {

    private UplandsSaplingGenerator() {
    }

    public static boolean generate(StructureWorldAccess world, BlockPos pos, BlockState state, Random rand, ConfiguredFeature<?, ?> feature) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
        if(feature.generate(world, ((ServerChunkManager)world.getChunkManager()).getChunkGenerator(),
            rand, pos)) {
            return true;
        } else {
            world.setBlockState(pos, state, 4);
            return false;
        }
    }
}
