package robosky.uplands.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
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

    private static final Property<Integer> DISTANCE = IntProperty.of("distance", 0, 4);

    public LodestoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE);
    }

    @Override
    public void scheduledTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        if (blockState.get(DISTANCE) == 4) {
            if (FallingBlock.canFallThrough(serverWorld.getBlockState(blockPos.down())) && blockPos.getY() >= 0) {
                FallingBlockEntity fallingEntity = new FallingBlockEntity(
                        serverWorld,
                        blockPos.getX() + 0.5,
                        blockPos.getY(),
                        blockPos.getZ() + 0.5,
                        blockState.with(DISTANCE, 0));
                fallingEntity.setHurtEntities(true);
                serverWorld.spawnEntity(fallingEntity);
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
         return this.getDefaultState().with(DISTANCE, this.updatedDistance(context.getWorld(), context.getBlockPos()));
    }

    @Override
    public void onBlockAdded(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean bl) {
        world.getBlockTickScheduler().schedule(blockPos, this, 1);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState blockState2, IWorld iWorld, BlockPos blockPos, BlockPos blockPos2) {
        return blockState.with(DISTANCE, this.updatedDistance(iWorld, blockPos));
    }

    @Override
    public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        world.getBlockTickScheduler().schedule(blockPos, this, 1);
    }

    private int updatedDistance(BlockView world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockState neighbor = world.getBlockState(pos.offset(dir));
            if (dir == Direction.DOWN) {
                if (neighbor.getBlock() == this) neighbor.get(DISTANCE);
                else if (!FallingBlock.canFallThrough(neighbor))
                    return 0;
                else
                    return 4;
            } else {
                if (neighbor.getBlock() == this)
                    return neighbor.get(DISTANCE) + 1;
                else if (this.isFullOpaque(neighbor, world, pos.offset(dir)))
                    return 1;
                else
                    return 4;
            }
        }

        return 4;
    }
}
