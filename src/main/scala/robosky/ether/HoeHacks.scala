package robosky.ether

import net.minecraft.block.{Block, BlockState}
import net.minecraft.item.HoeItem

object HoeHacks extends HoeItem(null, 0f, null) {
  def addHoeable(source: Block, tgt: BlockState): BlockState = HoeItem.TILLED_BLOCKS.put(source, tgt)
}
