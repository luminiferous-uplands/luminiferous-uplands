package robosky.ether.block.bossroom

import net.fabricmc.fabric.api.block.FabricBlockSettings

import net.minecraft.block.{Block, BlockEntityProvider, Material}
import net.minecraft.block.entity.BlockEntity
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{BlockView, World}

/**
 * Block for the boss controller. The important bits are on the block entity.
 */
object ControlBlock extends Block(
  FabricBlockSettings.of(Material.STONE)
    .sounds(BlockSoundGroup.STONE)
    .strength(-1.0f, 3600000.0f).build()) with BlockEntityProvider {

  override def createBlockEntity(world: BlockView): BlockEntity = new ControlBlockEntity()

  /**
   * Get the control block entity associated with the given position.
   */
  def control(world: World, pos: BlockPos): Option[ControlBlockEntity] =
    Option(world.getBlockEntity(pos)) collect { case ctrl: ControlBlockEntity => ctrl }
}
