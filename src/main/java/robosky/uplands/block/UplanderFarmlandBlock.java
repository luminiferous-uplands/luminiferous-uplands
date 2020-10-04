package robosky.uplands.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class UplanderFarmlandBlock extends Block {
    
    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    
    public UplanderFarmlandBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext entityContext) {
        return SHAPE;
    }

    @Override
    public boolean canPathfindThrough(BlockState blockState, BlockView blockView, BlockPos blockPos, NavigationType blockPlacementEnvironment) {
        return false;
    }
    
    @Override
    public void onLandedUpon(World world, BlockPos blockPos, Entity entity, float f) {
        if (!world.isClient && world.random.nextFloat() < f - 0.5F && entity instanceof LivingEntity
                && (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
                && entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F) {

            setToDirt(world.getBlockState(blockPos), world, blockPos);
        }

        super.onLandedUpon(world, blockPos, entity, f);
    }

    private void setToDirt(BlockState blockState, World world, BlockPos blockPos) {
        world.setBlockState(blockPos, Block.pushEntitiesUpBeforeBlockChange(blockState, BlockRegistry.UPLANDER_DIRT.getDefaultState(), world, blockPos));
    }
}
