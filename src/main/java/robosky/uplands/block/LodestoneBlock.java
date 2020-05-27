package robosky.uplands.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SandBlock;
import net.minecraft.client.network.DebugRendererInfoManager;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class LodestoneBlock extends Block {

    public LodestoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void scheduledTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        if (FallingBlock.canFallThrough(serverWorld.getBlockState(blockPos.down())) && blockPos.getY() >= 0) {
            FallingBlockEntity fallingEntity = new FallingBlockEntity(
                    serverWorld,
                    blockPos.getX() + 0.5,
                    blockPos.getY(),
                    blockPos.getZ() + 0.5,
                    serverWorld.getBlockState(blockPos));
            fallingEntity.setHurtEntities(true);
            serverWorld.spawnEntity(fallingEntity);
        }
    }

    @Override
    public void onBlockAdded(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean bl) {
        world.getBlockTickScheduler().schedule(blockPos, this, 1);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState blockState2, IWorld iWorld, BlockPos blockPos, BlockPos blockPos2) {
        iWorld.getBlockTickScheduler().schedule(blockPos, this, getTickRate(iWorld));
        return super.getStateForNeighborUpdate(blockState, direction, blockState2, iWorld, blockPos, blockPos2);
    }

    @Override
    public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        world.getBlockTickScheduler().schedule(blockPos, this, 1);
    }
}
