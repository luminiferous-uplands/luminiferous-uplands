package robosky.ether.block

import java.util.Random

import net.minecraft.block.{Block, BlockState, FallingBlock}
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.{IntProperty, Property}
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.world.{BlockView, IWorld, World}

object LodestoneBlock {
  val DISTANCE: Property[Integer] = IntProperty.of("distance", 0, 4)
}

class LodestoneBlock(settings: Block.Settings) extends Block(settings) {

  import LodestoneBlock.DISTANCE

  override protected def appendProperties(builder: StateFactory.Builder[Block, BlockState]): Unit = {
    builder.add(DISTANCE)
  }

  override def onScheduledTick(state: BlockState, world: World, pos: BlockPos, rand: Random): Unit = {
    if (!world.isClient) {
      if (state.get(DISTANCE) == 4) {
        if (FallingBlock.canFallThrough(world.getBlockState(pos.down)) && pos.getY >= 0) {
          val fallingEntity = new FallingBlockEntity(
            world,
            pos.getX + 0.5,
            pos.getY,
            pos.getZ + 0.5,
            state.`with`(DISTANCE, Int.box(0)))
          fallingEntity.setHurtEntities(true)
          world.spawnEntity(fallingEntity)
        }
      }
    }
  }

  override def getPlacementState(ctx: ItemPlacementContext): BlockState =
    this.getDefaultState.`with`(DISTANCE, Int.box(updatedDistance(ctx.getWorld, ctx.getBlockPos)))

  override def onBlockAdded(state: BlockState, world: World, pos: BlockPos, previous: BlockState,
    idk: Boolean): Unit = {
    world.getBlockTickScheduler.schedule(pos, this, 1)
  }

  override def getStateForNeighborUpdate(
    state: BlockState,
    dir: Direction,
    neighbor: BlockState,
    world: IWorld,
    pos: BlockPos,
    neighborPos: BlockPos): BlockState =
    state.`with`(DISTANCE, Int.box(updatedDistance(world, pos)))

  override def neighborUpdate(state: BlockState, world: World, pos: BlockPos, neighbor: Block,
    neighborPos: BlockPos, idk: Boolean): Unit = {
    world.getBlockTickScheduler.schedule(pos, this, 1)
  }

  private def updatedDistance(world: BlockView, pos: BlockPos): Int = {
    for {dir <- Direction.values} yield {
      val neighbor = world.getBlockState(pos.offset(dir))
      if (dir == Direction.DOWN) {
        // is supported from below?
        if (neighbor.getBlock == this) Int.unbox(neighbor.get(DISTANCE))
        else if (!FallingBlock.canFallThrough(neighbor)) 0
        else 4
      } else {
        // is supported by a like block
        if (neighbor.getBlock == this) neighbor.get(DISTANCE) + 1
        // is supported by a solid wall, or ceiling
        else if (this.isFullOpaque(neighbor, world, pos.offset(dir))) 1
        // unsupported from this side
        else 4
      }
    }
    }.min
}
