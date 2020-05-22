package robosky.uplands.block.bossroom

import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.{ActionResult, Hand}
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.{BlockView, World}
import org.apache.logging.log4j.{LogManager, Logger}
import robosky.uplands.block.unbreakable.Unbreakable

class ActiveAltarBlock(val base: Block)
    extends AltarBlock(Block.Settings.copy(base).strength(-1.0f, 3600000.0f)) with Unbreakable {

  private val logger: Logger = LogManager.getLogger

  override def onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, ctx: BlockHitResult): ActionResult = {
    if (!world.isClient) {
      val be = world.getBlockEntity(pos.offset(Direction.DOWN, 2))
      be match {
        case ctrl: ControlBlockEntity =>
          ctrl.activateBoss(pos.up)
        case _ =>
          logger.warn("Unable to activate boss: no boss control at {}", pos)
      }
    }
    ActionResult.SUCCESS
  }

  override def getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, ctx: EntityContext): VoxelShape =
    base.getOutlineShape(state, world, pos, ctx)
}
