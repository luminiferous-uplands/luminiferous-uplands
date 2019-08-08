package robosky.ether

import java.util.Random

import com.google.common.collect.Sets
import net.minecraft.advancement.criterion.Criterion
import net.minecraft.block.Blocks
import net.minecraft.entity.{Entity, LivingEntity}
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.{BlockPos, ChunkPos, MutableIntBoundingBox}
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import robosky.ether.advancement.FlyIntoUplandsCriterion
import robosky.ether.iface.UplanderBeaconUser
import robosky.ether.item.ItemRegistry
import robosky.ether.world.WorldRegistry
import robosky.ether.world.feature.SpawnPlatformPiece

object UplandsTeleporter {

  override def getUplandsCriterion: Criterion[_] = FlyIntoUplandsCriterion

  override def usePortalHookTo(entity: Entity, world: World): Boolean = {
    val (pos, usedBeacon) = if (entity.asInstanceOf[UplanderBeaconUser].uplands_isUsingBeacon) {
      // teleport to the spawn platform
      val tag = world.getLevelProperties.getWorldData(WorldRegistry.UPLANDS_DIMENSION)
      if (tag.containsKey("SpawnPlatform")) {
        val ptag = tag.getIntArray("SpawnPlatform")
        (new BlockPos(ptag(0), ptag(1), ptag(2)), true)
      } else {
        val pos1 = getTopPos(world, 7, 7).up(2)
        createSpawnPlatform(world, pos1.north(6).west(4).down(6))
        tag.putIntArray("SpawnPlatform", Array(pos1.getX, pos1.getY, pos1.getZ))
        world.getLevelProperties
          .setWorldData(WorldRegistry.UPLANDS_DIMENSION, tag)
        (pos1, true)
      }
    } else {
      // teleport to the corresponding point in the Uplands void
      (new BlockPos(entity.x, -40.0, entity.z), false)
    }
    entity match {
      case se: ServerPlayerEntity =>
        se.networkHandler.teleportRequest(
          pos.getX,
          pos.getY,
          pos.getZ,
          0,
          0,
          Sets.newHashSet()
        )
        se.networkHandler.syncWithPlayerPosition()
        if (usedBeacon) {
          se.asInstanceOf[UplanderBeaconUser].uplands_setUsingBeacon(false)
        } else {
          FlyIntoUplandsCriterion.handle(se)
        }
      case _ => entity.setPositionAndAngles(pos.getX, pos.getY, pos.getZ, 0, 0)
    }

    true
  }

  def createSpawnPlatform(world: World, pos: BlockPos): Unit =
    if (!world.isClient) {
      val sw = world.asInstanceOf[ServerWorld]
      val structure = new SpawnPlatformPiece(sw.getStructureManager, pos)
      val cp = new ChunkPos(pos)
      structure.generate(
        world,
        new Random(),
        new MutableIntBoundingBox(
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

  def usePortalHookFrom(entity: Entity, world: World): Boolean = {
    val pos = new BlockPos(entity.x, 256, entity.z)
    entity match {
      case se: ServerPlayerEntity =>
        se.networkHandler.teleportRequest(
          pos.getX,
          pos.getY,
          pos.getZ,
          0,
          0,
          Sets.newHashSet()
        )
        se.networkHandler.syncWithPlayerPosition()
      case _ => entity.setPositionAndAngles(pos.getX, pos.getY, pos.getZ, 0, 0)
    }
    true
  }
}
