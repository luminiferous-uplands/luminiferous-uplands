package robosky.uplands.block

import java.util.Collections

import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.World
import net.minecraft.util.math.BlockPos

import scala.collection.mutable.ArrayBuffer

class SkyDungeonTeleporterBlock(destination: Block, settings: Block.Settings) extends Block(settings) {

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

  override def onSteppedOn(world: World, pos: BlockPos, entity: Entity): Unit = {
    if (!world.isClient && entity.isInstanceOf[ServerPlayerEntity]) {
      val destinations = getDestinations(world, pos)
      if (!destinations.isEmpty) {
        val dst = destinations(world.getRandom.nextInt(destinations.size))
        entity.asInstanceOf[ServerPlayerEntity].networkHandler
          .teleportRequest(dst.getX + 0.5, dst.getY + 1, dst.getZ + 0.5, entity.yaw, entity.pitch, Collections.emptySet())
      }
    }
  }
}
