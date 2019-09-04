package robosky.ether.block.bossroom

import net.minecraft.block.{Block, Blocks, BlockEntityProvider, BlockRenderType, BlockState}
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.state.StateFactory
import net.minecraft.state.property.{EnumProperty, Property}
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.{VoxelShape, VoxelShapes}
import net.minecraft.world.{BlockView, World}

import robosky.ether.block.BlockRegistry
import robosky.ether.iface.BossDoorwayContext

object DoorwayBlock {

  val STATE: Property[DoorwayState] = EnumProperty.of("state", classOf[DoorwayState])

  private val SHAPE = Block.createCuboidShape(4.0f, 4.0f, 4.0f, 12.0f, 12.0f, 12.0f)
}

class DoorwayBlock(settings: Block.Settings) extends Block(settings) with BlockEntityProvider {

  def doorway(world: BlockView, pos: BlockPos): Option[DoorwayBlockEntity] =
    Option(world.getBlockEntity(pos)) collect { case d: DoorwayBlockEntity => d }

  override def appendProperties(builder: StateFactory.Builder[Block, BlockState]): Unit = {
    builder.add(DoorwayBlock.STATE)
  }

  override def createBlockEntity(world: BlockView): BlockEntity = new DoorwayBlockEntity()

  override def getRenderType(state: BlockState): BlockRenderType = BlockRenderType.INVISIBLE

  override def getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, ctx: EntityContext): VoxelShape = {
    if (state.get(DoorwayBlock.STATE) == DoorwayState.OPEN) {
      if (ctx == EntityContext.absent || ctx.asInstanceOf[BossDoorwayContext].uplands_shouldSeeDoorwayOutlines)
        DoorwayBlock.SHAPE
      else
        VoxelShapes.empty
    } else {
      doorway(world, pos)
        .map(_.mimicState.getOutlineShape(world, pos, ctx))
        .getOrElse(VoxelShapes.empty)
    }
  }

  override def getCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, ctx: EntityContext): VoxelShape = {
    if (state.get(DoorwayBlock.STATE) == DoorwayState.OPEN) {
      VoxelShapes.empty
    } else {
      doorway(world, pos)
        .map(_.mimicState.getCollisionShape(world, pos, ctx))
        .getOrElse(VoxelShapes.fullCube)
    }
  }

  override def activate(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, ctx: BlockHitResult): Boolean = {
    val heldStack = player.getStackInHand(hand)
    val block = Block.getBlockFromItem(heldStack.getItem).getDefaultState
    val validMimic = (heldStack.isEmpty && hand == Hand.MAIN_HAND) ||
      block.getRenderType == BlockRenderType.MODEL
    if (validMimic && !world.isClient) {
      doorway(world, pos) foreach {
        be =>
          be.mimicState = block
          be.markDirty()
          world.updateListeners(pos, state, state, 3)
      }
    }
    validMimic
  }
}