package robosky.uplands.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
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

public class LodestoneBlock extends Block {

    private static final int MAX_DISTANCE = 4;
    private static final Property<Integer> DISTANCE = IntProperty.of("distance", 0, MAX_DISTANCE);

    public LodestoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE);
    }

    @Override
    public void scheduledTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        if (blockState.get(DISTANCE) == MAX_DISTANCE) {
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
        int dist = MAX_DISTANCE;
        for (Direction dir : Direction.values()) {
            int tmp;
            BlockState neighbor = world.getBlockState(pos.offset(dir));
            if (dir == Direction.DOWN) {
                if (neighbor.getBlock() == this) {
                    tmp = neighbor.get(DISTANCE);
                } else if (!FallingBlock.canFallThrough(neighbor)) {
                    tmp = 0;
                } else {
                    tmp = MAX_DISTANCE;
                }
            } else {
                if (neighbor.getBlock() == this) {
                    tmp = neighbor.get(DISTANCE) + 1;
                } else if (neighbor.isFullOpaque(world, pos.offset(dir))) {
                    tmp = 1;
                } else {
                    tmp = MAX_DISTANCE;
                }
            }
            dist = Math.min(dist, tmp);
        }
        return dist;
    }
}
