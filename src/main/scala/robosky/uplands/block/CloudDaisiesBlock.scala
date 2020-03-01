package robosky.uplands.block

import net.minecraft.block.{Block, BlockState, FlowerBlock}
import net.minecraft.entity.EntityContext
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import robosky.uplands.UplandsBlockTags

object CloudDaisiesBlock {
    val SHAPE: VoxelShape =
        Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D)
}

class CloudDaisiesBlock (val settings: Block.Settings) extends FlowerBlock(StatusEffects.SLOW_FALLING, 15, settings) {
    override def canPlantOnTop(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos): Boolean =
        blockState_1.matches(UplandsBlockTags.PlantableOn)

    override def getOutlineShape(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos,
                                 entityContext_1: EntityContext): VoxelShape = CloudDaisiesBlock.SHAPE
}
