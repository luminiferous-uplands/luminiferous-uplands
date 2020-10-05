package robosky.uplands.world.gen;

import java.util.Random;

import robosky.uplands.block.BlockRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class UplandsAutumnSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {

    public UplandsAutumnSurfaceBuilder() {
        super(TernarySurfaceConfig.CODEC);
    }

    @Override
    public void generate(Random rand, Chunk chunk, Biome biome, int absX, int absZ, int startHeight, double noise,
                         BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, TernarySurfaceConfig config) {
        BlockState topBlock = BlockRegistry.UPLANDER_GRASS.getDefaultState();
        BlockState underBlock = BlockRegistry.UPLANDER_DIRT.getDefaultState();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        int depth = -1;
        int maxDepth = (int)(noise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int x = absX & 15;
        int z = absZ & 15;
        for(int y = startHeight; y >= 0; y--) {
            pos.set(x, y, z);
            BlockState blockState_8 = chunk.getBlockState(pos);
            if(blockState_8.isAir()) {
                depth = -1;
            } else if(blockState_8.getBlock() == defaultBlock.getBlock()) {
                if(depth == -1) {
                    if(maxDepth <= 0) {
                        topBlock = Blocks.AIR.getDefaultState();
                        underBlock = topBlock;
                    }
                    chunk.setBlockState(pos, topBlock, false);
                    depth = maxDepth;
                } else if(depth > 0) {
                    depth -= 1;
                    chunk.setBlockState(pos, underBlock, false);
                }
            }
        }
    }
}
