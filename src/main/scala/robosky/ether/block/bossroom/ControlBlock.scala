package robosky.ether.block.bossroom

import net.fabricmc.fabric.api.block.FabricBlockSettings

import net.minecraft.block.{Block, BlockEntityProvider, BlockState, Material}
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.SpawnEggItem
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.world.{BlockView, World}

import robosky.ether.UplandsMod

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

  override def activate(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, ctx: BlockHitResult): Boolean = {
    val shouldEdit = player.isCreativeLevelTwoOp &&
      player.inventory.contains(UplandsMod.BOSSROOM_TECHNICAL_TAG)
    if (shouldEdit) {
      control(world, pos) foreach {
        ctrl =>
          player.getMainHandStack.getItem match {
            // set boss type
            case egg: SpawnEggItem =>
              ctrl.bossType = egg.getEntityType(null)
            // adjust boss room bounds
            case _ =>
              val hitSide = ctx.getSide
              val hitPos = ctx.getPos
              val blocks = if (player.isSneaking) -1 else 1
              hitSide match {
                // horizontal adjustment
                case Direction.DOWN | Direction.UP =>
                  val x = Math.abs(hitPos.getX - pos.getX) - 0.5
                  val z = Math.abs(hitPos.getZ - pos.getZ) - 0.5
                  val theta = Math.atan2(z, x) / Math.PI
                  if (-0.75 <= theta && theta < -0.25) {
                    ctrl.adjustBoundsNorth(blocks)
                  } else if (-0.25 <= theta && theta < 0.25) {
                    ctrl.adjustBoundsEast(blocks)
                  } else if (0.25 <= theta && theta < 0.75) {
                    ctrl.adjustBoundsSouth(blocks)
                  } else {
                    ctrl.adjustBoundsWest(blocks)
                  }
                // vertical adjustment
                case _ =>
                  val y = hitPos.getY - pos.getY
                  if (y <= 0.5) {
                    ctrl.adjustBoundsDown(blocks)
                  } else {
                    ctrl.adjustBoundsUp(blocks)
                  }
              }
          }
      }
    }
    shouldEdit
  }
}
