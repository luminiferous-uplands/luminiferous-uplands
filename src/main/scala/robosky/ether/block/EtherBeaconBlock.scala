package robosky.ether.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block.{Block, BlockState, Material}
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.{BlockView, World}
import robosky.ether.world.WorldRegistry

object EtherBeaconBlock extends Block(FabricBlockSettings.of(Material.STONE).strength(3, 3)
  .breakByTool(FabricToolTags.PICKAXES, 2).build()) {

  override def getOutlineShape(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos,
    entityContext_1: EntityContext): VoxelShape =
    Block.createCuboidShape(2, 0, 2, 14, 16, 14)

  override def activate(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand,
    result: BlockHitResult): Boolean = {
    if (player.world.dimension.getType == DimensionType.OVERWORLD) {
      player.changeDimension(WorldRegistry.UPLANDS_DIMENSION)
      true
    } else {
      false
    }
  }
}
