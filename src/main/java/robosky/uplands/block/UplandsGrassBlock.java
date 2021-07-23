package robosky.uplands.block;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

public class UplandsGrassBlock extends Block {

    public UplandsGrassBlock() {
        super(FabricBlockSettings
            .of(Material.SOLID_ORGANIC)
            .ticksRandomly()
            .strength(0.6f, 0.6f)
            .breakByTool(FabricToolTags.SHOVELS)
            .sounds(BlockSoundGroup.GRASS));
    }

    @Override
    public void scheduledTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        if(!serverWorld.isClient) {
            if(!canSurvive(blockState, serverWorld, blockPos)) {
                serverWorld.setBlockState(blockPos, BlockRegistry.UPLANDER_DIRT.getDefaultState());
            } else if(serverWorld.getLightLevel(blockPos.up()) >= 4) {
                if(serverWorld.getLightLevel(blockPos.up()) >= 9) {
                    for(int i = 0; i < 4; i++) {
                        BlockPos pos = blockPos.add(random.nextInt(3) - 1, random.nextInt(5) - 3,
                            random.nextInt(3) - 1);

                        if((serverWorld.getBlockState(pos).getBlock() == BlockRegistry.UPLANDER_DIRT) && canSpread(this.getDefaultState(),
                            serverWorld, pos)) {
                            serverWorld.setBlockState(pos, this.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    private boolean canSpread(BlockState blockState, WorldView worldView, BlockPos blockPos) {
        BlockPos blockUpper = blockPos.up();
        return canSurvive(blockState, worldView, blockPos) && !worldView.getFluidState(blockUpper).isIn(FluidTags.WATER);
    }

    private boolean canSurvive(BlockState blockState, WorldView worldView, BlockPos blockPos) {
        BlockPos blockUpper = blockPos.up();
        BlockState blockPosState = worldView.getBlockState(blockUpper);
        int lightLevel = ChunkLightProvider.getRealisticOpacity(worldView, blockState, blockPos, blockPosState, blockUpper, Direction.UP,
            blockPosState.getOpacity(worldView, blockUpper));
        return lightLevel < worldView.getMaxLightLevel();
    }
}
