package robosky.uplands.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.EntityContext;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class AwokenAzoteMushroomBlock extends PlantBlock {

    private static final VoxelShape SHAPE = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);

    public AwokenAzoteMushroomBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
        return SHAPE;
    }

    @Override
    protected boolean canPlantOnTop(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return blockState.matches(UplandsBlockTags.PLANTABLE_ON) || blockState.matches(UplandsBlockTags.AZOTE_MUSHROOM_SPREADABLE);
    }
}
