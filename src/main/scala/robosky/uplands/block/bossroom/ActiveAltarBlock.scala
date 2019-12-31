package robosky.uplands.block.bossroom

import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.{BlockView, World}

import org.apache.logging.log4j.{Logger, LogManager}

import robosky.uplands.block.BlockRegistry
import robosky.uplands.block.unbreakable.Unbreakable

class ActiveAltarBlock(val base: Block)
    extends AltarBlock(Block.Settings.copy(base)) with Unbreakable {

  private val logger: Logger = LogManager.getLogger

  override def activate(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, ctx: BlockHitResult): Boolean = {
    if (!world.isClient) {
      val be = world.getBlockEntity(pos.offset(Direction.DOWN, 2))
      be match {
        case ctrl: ControlBlockEntity =>
          ctrl.activateBoss(pos.up)
        case _ =>
          logger.warn("Unable to activate boss: no boss control at {}", pos)
      }
    }
    true
  }

  override def getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, ctx: EntityContext): VoxelShape =
    base.getOutlineShape(state, world, pos, ctx)
}
