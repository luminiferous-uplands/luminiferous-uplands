package robosky.uplands.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class TallUplandsGrassBlock extends FernBlock {

    public TallUplandsGrassBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return blockState.matches(UplandsBlockTags.PLANTABLE_ON);
    }
}
