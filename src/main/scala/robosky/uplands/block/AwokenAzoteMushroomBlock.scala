package robosky.uplands.block

import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.{Block, BlockState, PlantBlock}
import net.minecraft.entity.EntityContext
import net.minecraft.tag.BlockTags
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import robosky.uplands.UplandsBlockTags

object AwokenAzoteMushroomBlock {
  val SHAPE: VoxelShape = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D)
}

class AwokenAzoteMushroomBlock(val settings: Block.Settings) extends PlantBlock(settings) {
  override def canPlantOnTop(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos): Boolean =
    blockState_1.matches(UplandsBlockTags.PlantableOn) ||
      blockState_1.matches(UplandsBlockTags.AzoteMushroomSpreadable)

  override def getOutlineShape(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos,
                               entityContext_1: EntityContext): VoxelShape = AwokenAzoteMushroomBlock.SHAPE
}
