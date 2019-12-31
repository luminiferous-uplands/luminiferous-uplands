package robosky.uplands.block.unbreakable

import net.minecraft.block.{Block => McBlock, BlockState, StairsBlock, SlabBlock, WallBlock}

class Block(val base: McBlock)
  extends McBlock(McBlock.Settings.copy(base)) with Unbreakable

class Stairs(val base: StairsBlock, baseState: BlockState)
  extends StairsBlock(baseState, McBlock.Settings.copy(base)) with Unbreakable

class Slab(val base: SlabBlock)
  extends SlabBlock(McBlock.Settings.copy(base)) with Unbreakable

class Wall(val base: WallBlock)
  extends WallBlock(McBlock.Settings.copy(base)) with Unbreakable
