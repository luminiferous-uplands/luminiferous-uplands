package robosky.uplands

import java.util.Random

import net.fabricmc.fabric.api.dimension.v1.EntityPlacer
import net.minecraft.advancement.criterion.Criterion
import net.minecraft.block.Blocks
import net.minecraft.block.pattern.BlockPattern
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.{ServerChunkManager, ServerWorld}
import net.minecraft.util.math._
import net.minecraft.world.World
import robosky.uplands.advancement.FlyIntoUplandsCriterion
import robosky.uplands.world.WorldRegistry
import robosky.uplands.world.feature.SpawnPlatformPiece

object UplandsTeleporter {

  def getUplandsCriterion: Criterion[_] = FlyIntoUplandsCriterion.INSTANCE

  object ToUplandsBeacon extends EntityPlacer {
    override def placeEntity(entity: Entity, world: ServerWorld, direction: Direction, v: Double, v1: Double): BlockPattern.TeleportTarget = {
      // teleport to the spawn platform
      val tag = world.getLevelProperties.getWorldData(WorldRegistry.UPLANDS_DIMENSION)
      val pos = if (tag.contains("SpawnPlatform")) {
        val ptag = tag.getIntArray("SpawnPlatform")
        new Vec3d(ptag(0), ptag(1), ptag(2))
      } else {
        val pos1 = getTopPos(world, 7, 7).up(2)
        createSpawnPlatform(world, pos1.north(6).west(4).down(6))
        tag.putIntArray("SpawnPlatform", Array(pos1.getX, pos1.getY, pos1.getZ))
        world.getLevelProperties
          .setWorldData(WorldRegistry.UPLANDS_DIMENSION, tag)
        new Vec3d(pos1.getX, pos1.getY, pos1.getZ)
      }
      new BlockPattern.TeleportTarget(pos, Vec3d.ZERO, 0)
    }

    def createSpawnPlatform(world: World, pos: BlockPos): Unit =
      if (!world.isClient) {
        val sw = world.asInstanceOf[ServerWorld]
        val structure = new SpawnPlatformPiece(sw.getStructureManager, pos)
        val cp = new ChunkPos(pos)
        structure.generate(
          world,
          (sw.getChunkManager: ServerChunkManager).getChunkGenerator,
          new Random(),
          new BlockBox(
            cp.getStartX,
            cp.getStartZ,
            cp.getEndX,
            cp.getEndZ
          ),
          cp
        )
      }

    private def getTopPos(world: World, x: Int, z: Int): BlockPos = {
      var returnPos = new BlockPos(x, 0, z)
      for (i <- 70 to 48 by -1) { // if block is not air, return that spot
        if (!(world.getBlockState(returnPos.up(i)) == Blocks.AIR.getDefaultState)) {
          returnPos = returnPos.up(i + 1)
          return returnPos
        }
      }
      returnPos.up(56)
    }
  }

  object ToUplandsFlying extends EntityPlacer {
    override def placeEntity(entity: Entity, serverWorld: ServerWorld, direction: Direction, v: Double, v1: Double): BlockPattern.TeleportTarget = {
      entity match {
        case se: ServerPlayerEntity =>
          FlyIntoUplandsCriterion.INSTANCE.handle(se)
      }
      new BlockPattern.TeleportTarget(new Vec3d(entity.getX, -40.0, entity.getZ), entity.getVelocity, entity.yaw.toInt)
    }
  }

  object FromUplands extends EntityPlacer {
    override def placeEntity(entity: Entity, serverWorld: ServerWorld, direction: Direction, v: Double, v1: Double): BlockPattern.TeleportTarget = {
      new BlockPattern.TeleportTarget(new Vec3d(entity.getX, 256, entity.getZ), entity.getVelocity, entity.yaw.toInt)
    }
  }

}
