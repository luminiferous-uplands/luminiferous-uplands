package robosky.ether.block.bossroom

import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.world.World

import org.apache.logging.log4j.{Logger, LogManager}

import robosky.ether.block.{BlockRegistry, unbreakable}

class ActiveAlterBlock extends unbreakable.Block(BlockRegistry.BOSS_ALTER) {

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
}
