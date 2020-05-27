package robosky.uplands.block;

import net.minecraft.block.*;
import net.minecraft.entity.EntityContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import robosky.uplands.world.feature.plants.UplandsSaplingGenerator;

import java.util.Random;

public class UplandsSaplingBlock extends PlantBlock implements Fertilizable {

    private final UplandsSaplingGenerator generator;
    private static final IntProperty STAGE = Properties.STAGE;
    private static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

    protected UplandsSaplingBlock(UplandsSaplingGenerator generator, Settings settings) {
        super(settings);
        this.generator = generator;
        setDefaultState(this.stateManager.getDefaultState().with(UplandsSaplingBlock.STAGE, 0));
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UplandsSaplingBlock.STAGE);
    }
    
    @Override
    protected boolean canPlantOnTop(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return blockState.matches(UplandsBlockTags.PLANTABLE_ON);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
        return SHAPE;
    }
    
    @Override
    public void scheduledTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        if (serverWorld.getLightLevel(blockPos.up()) >= 9 && random.nextInt(7) == 0)
            this.generate(serverWorld, blockPos, blockState, random);
    }

    @Override
    public boolean isFertilizable(BlockView blockView, BlockPos blockPos, BlockState blockState, boolean bl) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos blockPos, BlockState blockState) {
        return world.random.nextDouble() < 0.45D;
    }

    @Override
    public void grow(ServerWorld serverWorld, Random random, BlockPos blockPos, BlockState blockState) {
        this.generate(serverWorld, blockPos, blockState, random);
    }

    private void generate(IWorld iWorld, BlockPos blockPos, BlockState blockState, Random random) {
        if (blockState.get(UplandsSaplingBlock.STAGE) == 0)
            iWorld.setBlockState(blockPos, blockState.cycle(UplandsSaplingBlock.STAGE), 4);
        else
            this.generator.generate(iWorld, blockPos, blockState, random);
    }
}
