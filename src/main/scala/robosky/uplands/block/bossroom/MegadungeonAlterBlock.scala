package robosky.uplands.block.bossroom

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.tools.FabricToolTags

import net.minecraft.block.{Block, BlockState, Material}
import net.minecraft.entity.EntityContext
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.{VoxelShape, VoxelShapes}
import net.minecraft.world.BlockView

object MegadungeonAlterBlock extends AlterBlock(FabricBlockSettings.of(Material.STONE)
    .strength(1.5f, 6f).sounds(BlockSoundGroup.STONE)
    .breakByTool(FabricToolTags.PICKAXES, 1).build) {

  val SHAPE: VoxelShape = VoxelShapes.union(
    Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0),
    Block.createCuboidShape(2.0, 1.0, 2.0, 14.0, 2.0, 14.0),
    Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 4.0, 12.0),
    Block.createCuboidShape(5.0, 4.0, 5.0, 11.0, 14.0, 11.0),
    Block.createCuboidShape(3.0, 14.0, 3.0, 13.0, 15.0, 13.0),
    Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0)
  )

  override def getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, ctx: EntityContext): VoxelShape = SHAPE
}
