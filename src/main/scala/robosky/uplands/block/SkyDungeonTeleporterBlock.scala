package robosky.uplands.block

import java.util.{Collections, Random}
import java.util.function.Predicate

import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.{Entity, EntityType}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties
import net.minecraft.state.StateManager
import net.minecraft.world.World
import net.minecraft.util.math.{BlockPos, Box}

import scala.collection.mutable.ArrayBuffer

class SkyDungeonTeleporterBlock(destination: Block, settings: Block.Settings) extends Block(settings) {

  this.setDefaultState(this.getDefaultState.`with`(Properties.POWERED, Boolean.box(false)))

  override protected def appendProperties(builder: StateManager.Builder[Block, BlockState]): Unit = {
    builder.add(Properties.POWERED)
  }

  /**
   * Gathers a list of valid destination positions. Valid destination positions
   * are on top of a destination block and have unobstructed space above for
   * the player.
   */
  private def getDestinations(world: World, pos: BlockPos): Seq[BlockPos] = {
    val Radius = 32
    val mutablePos = new BlockPos.Mutable()
    val dests = ArrayBuffer.empty[BlockPos]
    for {
      x <- -Radius to Radius
      y <- -Radius to Radius
      z <- -Radius to Radius
      if !(x == 0 && y == 0 && z == 0)
    } {
      mutablePos.set(pos).setOffset(x, y + 2, z)
      if (world.getBlockState(mutablePos).isAir) {
        mutablePos.set(pos).setOffset(x, y + 1, z)
        if (!world.getBlockState(mutablePos).isFullCube(world, mutablePos)) {
          mutablePos.set(pos).setOffset(x, y, z)
          if (world.getBlockState(mutablePos).getBlock == destination) {
            dests += mutablePos.toImmutable
          }
        }
      }
    }
    dests.toSeq
  }

  /**
   * Teleports a player to a random destination.
   */
  private def teleport(world: World, pos: BlockPos, player: ServerPlayerEntity): Unit = {
      val destinations = getDestinations(world, pos)
      if (!destinations.isEmpty) {
        val dst = destinations(world.getRandom.nextInt(destinations.size))
        player.networkHandler
          .teleportRequest(dst.getX + 0.5, dst.getY + 1, dst.getZ + 0.5, player.yaw, player.pitch, Collections.emptySet())
      }
  }

  override def onSteppedOn(world: World, pos: BlockPos, entity: Entity): Unit = {
    if (!world.isClient && entity.isInstanceOf[ServerPlayerEntity]) {
      val state = world.getBlockState(pos)
      if (!state.get(Properties.POWERED)) {
        world.setBlockState(pos, state.`with`(Properties.POWERED, Boolean.box(true)))
        world.getBlockTickScheduler.schedule(pos, this, 20)
      }
    }
  }

  override def scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, rand: Random): Unit = {
    if (state.get(Properties.POWERED)) {
      // find a player standing on top, teleport if found
      val pred: Predicate[PlayerEntity] = p => true
      val players = world.getEntities(EntityType.PLAYER, new Box(pos.up()), pred)
      players.forEach {
        case player: ServerPlayerEntity => teleport(world, pos, player)
        case _ =>
      }
      world.setBlockState(pos, state.`with`(Properties.POWERED, Boolean.box(false)))
    }
  }
}
