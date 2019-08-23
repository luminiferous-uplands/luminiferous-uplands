package robosky.ether.block

import net.minecraft.block.{Block, BlockState, StairsBlock}

class ModStairsBlock(state: BlockState, settings: Block.Settings) extends StairsBlock(state, settings) {
  def this(base: Block, settings: Block.Settings) {
    this(base.getDefaultState, settings)
  }
}
