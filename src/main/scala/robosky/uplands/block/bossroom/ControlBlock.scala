package robosky.uplands.block.bossroom

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.{Block, BlockEntityProvider, BlockState, Material}
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.SpawnEggItem
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.state.property.{EnumProperty, Property}
import net.minecraft.util.{ActionResult, Hand}
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.world.{BlockView, World}
import robosky.uplands.UplandsMod
import robosky.uplands.item.UplandsItemTags

object ControlBlock {
  val ADJUST: Property[ControlAdjustment] = EnumProperty.of("adjust", classOf[ControlAdjustment])
}

/**
 * Block for the boss controller. The important bits are on the block entity.
 */
class ControlBlock extends Block(
  FabricBlockSettings.of(Material.STONE)
    .sounds(BlockSoundGroup.STONE)
    .strength(-1.0f, 3600000.0f).build()) with BlockEntityProvider {

  override def createBlockEntity(world: BlockView): BlockEntity = new ControlBlockEntity()

  /**
   * Get the control block entity associated with the given position.
   */
  def control(world: World, pos: BlockPos): Option[ControlBlockEntity] =
    Option(world.getBlockEntity(pos)) collect { case ctrl: ControlBlockEntity => ctrl }

  override def appendProperties(builder: StateManager.Builder[Block, BlockState]): Unit = {
    builder.add(ControlBlock.ADJUST)
  }

  private val adjustmentMapping = {
    import net.minecraft.util.math.Direction._
    Array(
      Array(WEST, NORTH, EAST, SOUTH), // DOWN/UP
      Array(WEST, DOWN, EAST, UP), // NORTH/SOUTH
      Array(NORTH, DOWN, SOUTH, UP) // WEST/EAST
    )
  }

  override def onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, ctx: BlockHitResult): ActionResult = {
    val shouldEdit = player.isCreativeLevelTwoOp &&
      player.inventory.contains(UplandsItemTags.BOSSROOM_TECHNICAL)
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
              val u = hitSide match {
                case Direction.WEST | Direction.EAST =>
                  (hitPos.getZ - pos.getZ).abs - 0.5
                case _ =>
                  (hitPos.getX - pos.getX).abs - 0.5
              }
              val v = hitSide match {
                case Direction.DOWN | Direction.UP =>
                  (hitPos.getZ - pos.getZ).abs - 0.5
                case _ =>
                  (hitPos.getY - pos.getY).abs - 0.5
              }
              val rSq = u * u + v * v
              // u and v range from [-0.5, 0.5], this is the inner third
              // of that range, i.e. < 1/6 ^ 2
              val centerThird = 0.17 * 0.17
              if (rSq < centerThird) {
                world.setBlockState(pos, state.cycle(ControlBlock.ADJUST))
              } else {
                val theta = Math.atan2(v, u) / Math.PI
                val dir = adjustmentMapping(hitSide.getId / 2)(((theta + 1.25) * 2).toInt % 4)
                val adj = state.get(ControlBlock.ADJUST)
                val blocks = if (adj == ControlAdjustment.IN) -1 else 1
                ctrl.adjustBounds(dir, blocks)
              }
          }
      }
    }
    if (shouldEdit) ActionResult.SUCCESS else ActionResult.PASS
  }
}
