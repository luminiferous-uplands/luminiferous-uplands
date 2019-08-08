package robosky.ether

import net.minecraft.block.{Block, BlockState}
import net.minecraft.item.{HoeItem, Item, ToolMaterials}

object HoeHacks extends HoeItem(ToolMaterials.WOOD, 0f, new Item.Settings()) {
  def addHoeable(source: Block, tgt: BlockState): BlockState = HoeItem.TILLED_BLOCKS.put(source, tgt)
}
