package robosky.uplands.block.bossroom

import net.minecraft.block.{Block, BlockRenderLayer, BlockState, HorizontalFacingBlock}
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory

import org.apache.logging.log4j.{Logger, LogManager}

import robosky.uplands.block.{BlockRegistry, unbreakable}

class AlterBlock(settings: Block.Settings) extends HorizontalFacingBlock(settings) {

  override protected def appendProperties(builder: StateFactory.Builder[Block, BlockState]): Unit = {
    builder.add(HorizontalFacingBlock.FACING)
  }

  override def getPlacementState(ctx: ItemPlacementContext): BlockState = {
    this.getDefaultState.`with`(HorizontalFacingBlock.FACING, ctx.getPlayerFacing.getOpposite)
  }

  override def getRenderLayer: BlockRenderLayer = BlockRenderLayer.CUTOUT_MIPPED
}
