package robosky.uplands.block.bossroom

import net.minecraft.block.{Block, BlockState, HorizontalFacingBlock}
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager

import org.apache.logging.log4j.{Logger, LogManager}

import robosky.uplands.block.unbreakable

class AltarBlock(settings: Block.Settings) extends HorizontalFacingBlock(settings) {

  override protected def appendProperties(builder: StateManager.Builder[Block, BlockState]): Unit = {
    builder.add(HorizontalFacingBlock.FACING)
  }

  override def getPlacementState(ctx: ItemPlacementContext): BlockState = {
    this.getDefaultState.`with`(HorizontalFacingBlock.FACING, ctx.getPlayerFacing.getOpposite)
  }
}
