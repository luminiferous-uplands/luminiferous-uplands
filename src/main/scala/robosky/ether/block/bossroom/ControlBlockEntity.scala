package robosky.ether.block.bossroom

import java.util.UUID
import java.util.stream.Collectors.toList

import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.entity.{Entity, EntityType, SpawnType}
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.{BlockRotation, Identifier}
import net.minecraft.util.math.{BlockPos, MutableIntBoundingBox}
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

import robosky.ether.UplandsMod
import robosky.ether.block.unbreakable.Unbreakable

import scala.collection.JavaConverters._

object ControlBlockEntity {
  val TYPE = BlockEntityType.Builder.create(() => new ControlBlockEntity(), ControlBlock).build(null)
}

/**
 * Controls boss room mechanics.
 */
class ControlBlockEntity extends BlockEntity(ControlBlockEntity.TYPE) {

  /**
   * The bounds of the boss room, relative to the position of this instance.
   */
  private val room: MutableIntBoundingBox = new MutableIntBoundingBox()

  /**
   * The rotation of the boss room.
   */
  // is this needed?
  private var rotation: BlockRotation = BlockRotation.NONE

  /**
   * The boss entity to spawn on command.
   */
  private var bossType: EntityType[_] = EntityType.PIG

  /**
   * The unique ID of the boss entity.
   */
  private var bossUuid: Option[UUID] = None

  /**
   * Spawns the boss at the specified BlockPos.
   */
  def activateBoss(spawn: BlockPos): Unit = {
    if (!this.world.isClient) {
      val boss = bossType.spawn(this.world, null, null, null, spawn, SpawnType.SPAWNER, false, false)
      bossUuid = Some(boss.getUuid)
    }
  }

  /**
   * Despawns the boss (e.g. if the player died while fighting)
   */
  def deactivateBoss(): Unit = {
    if (!this.world.isClient) {
      bossUuid foreach {
        uuid =>
          val server = this.world.asInstanceOf[ServerWorld]
          val boss = server.getEntity(uuid)
          server.removeEntity(boss)
      }
      bossUuid = None
    }
  }

  /**
   * Cleans up the boss room upon the boss's defeat. This includes replacing
   * unbreakable blocks with their breakable counterparts.
   */
  def onBossDefeat(): Unit = {
    // TODO: rotation
    val ls = BlockPos.iterate(
        this.pos.getX + room.minX,
        this.pos.getY + room.minY,
        this.pos.getZ + room.minZ,
        this.pos.getX + room.maxX,
        this.pos.getY + room.maxY,
        this.pos.getZ + room.maxZ).asScala
    for {
      pos <- ls
      state = this.world.getBlockState(pos)
      block <- Some(state.getBlock) collect { case b: Unbreakable => b }
    } this.world.setBlockState(pos, block.toBreakable(state))
  }

  override def toTag(_tag: CompoundTag): CompoundTag = {
    val tag = super.toTag(_tag)
    tag.putIntArray("Bounds", Array(room.minX, room.minY, room.minZ, room.maxX, room.maxY, room.maxZ))
    tag.putString("Rotation", rotation.name)
    tag.putString("Boss", Registry.ENTITY_TYPE.getId(bossType).toString)
    bossUuid foreach {
      uuid =>
        tag.putLong("BossUUIDMost", uuid.getMostSignificantBits)
        tag.putLong("BossUUIDLeast", uuid.getLeastSignificantBits)
    }
    tag
  }

  override def fromTag(tag: CompoundTag): Unit = {
    super.fromTag(tag)
    val bounds = tag.getIntArray("Bounds")
    if (bounds.length == 6) {
      room.minX = bounds(0)
      room.minY = bounds(1)
      room.minZ = bounds(2)
      room.maxX = bounds(3)
      room.maxY = bounds(4)
      room.maxZ = bounds(5)
    } else {
      room.minX = 0
      room.minY = 0
      room.minZ = 0
      room.maxX = 0
      room.maxY = 0
      room.maxZ = 0
    }
    rotation = {
      val name = tag.getString("Rotation")
      try {
        BlockRotation.valueOf(name)
      } catch {
        case _: IllegalArgumentException => BlockRotation.NONE
      }
    }
    bossType = {
      for {
        id <- Option(Identifier.tryParse(tag.getString("Boss")))
        etyp <- Option[EntityType[_]](Registry.ENTITY_TYPE.get(id))
      } yield etyp
    } getOrElse EntityType.PIG
    bossUuid = {
      if (tag.containsKey("BossUUIDMost") && tag.containsKey("BossUUIDLeast"))
        Some(new UUID(tag.getLong("BossUUIDMost"), tag.getLong("BossUUIDLeast")))
      else
        None
    }
  }
}
