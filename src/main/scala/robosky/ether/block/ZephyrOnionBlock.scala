package robosky.ether.block

import net.fabricmc.api.{EnvType, Environment}
import net.minecraft.block.{Block, BlockState, Blocks, CropBlock}
import net.minecraft.entity.EntityContext
import net.minecraft.item.ItemConvertible
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import robosky.ether.item.ItemRegistry

object ZephyrOnionBlock {
  private val AGE_TO_SHAPE = Array[VoxelShape](
    Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
    Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D),
    Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
    Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
    Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
    Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
    Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
    Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D)
  )
}

class ZephyrOnionBlock(val settings: Block.Settings) extends CropBlock(settings) {
  @Environment(EnvType.CLIENT) override protected def getSeedsItem: ItemConvertible = ItemRegistry.ZEPHYR_ONION_ITEM

  override def getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, context: EntityContext): VoxelShape =
    ZephyrOnionBlock.AGE_TO_SHAPE(state.get(this.getAgeProperty))

  override protected def canPlantOnTop(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos): Boolean = blockState_1.getBlock eq BlockRegistry.UPLANDER_FARMLAND
}
