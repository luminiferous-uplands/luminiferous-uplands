package robosky.uplands.block

import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.{Block, BlockState, FernBlock}
import net.minecraft.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

class TallUplandsGrassBlock(val settings: Block.Settings) extends FernBlock(settings){
  override def canPlantOnTop(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos): Boolean = blockState_1.matches(UplandsBlockTags.PLANTABLE_ON)
}
