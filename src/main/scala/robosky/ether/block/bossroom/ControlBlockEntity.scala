package robosky.ether.block.bossroom

import java.util.UUID
import java.util.stream.Collectors.toList

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable

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
import robosky.ether.util.IntBox

import scala.collection.JavaConverters._

object ControlBlockEntity {
  val TYPE = BlockEntityType.Builder.create(() => new ControlBlockEntity(), ControlBlock).build(null)
}

/**
 * Controls boss room mechanics.
 */
class ControlBlockEntity extends BlockEntity(ControlBlockEntity.TYPE) with BlockEntityClientSerializable {

  /**
   * The bounds of the boss room, relative to the position of this instance.
   */
  def bounds: IntBox = _bounds

  private def bounds_=(value: IntBox) = { _bounds = value }

  private[this] var _bounds: IntBox = IntBox.Empty

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
    val ls = BlockPos.iterate(
        this.pos.getX + bounds.minX,
        this.pos.getY + bounds.minY,
        this.pos.getZ + bounds.minZ,
        this.pos.getX + bounds.maxX,
        this.pos.getY + bounds.maxY,
        this.pos.getZ + bounds.maxZ).asScala
    for {
      pos <- ls
      state = this.world.getBlockState(pos)
      block <- Some(state.getBlock) collect { case b: Unbreakable => b }
    } this.world.setBlockState(pos, block.toBreakable(state))
  }

  override def applyRotation(rot: BlockRotation): Unit = {
    import net.minecraft.util.BlockRotation._
    bounds = rot match {
      case NONE => bounds
      case COUNTERCLOCKWISE_90 => bounds.copy(
        minX = bounds.minZ,
        minZ = -bounds.minX + 1,
        maxX = bounds.maxZ,
        maxZ = -bounds.maxX + 1)
      case CLOCKWISE_180 => bounds.copy(
        minX = -bounds.minX + 1,
        minZ = -bounds.minZ + 1,
        maxX = -bounds.maxX + 1,
        maxZ = -bounds.maxZ + 1)
      case CLOCKWISE_90 => bounds.copy(
        minX = -bounds.minZ + 1,
        minZ = bounds.minX,
        maxX = -bounds.maxZ + 1,
        maxZ = bounds.maxX)
    }
  }

  override def toTag(_tag: CompoundTag): CompoundTag = {
    val tag = super.toTag(_tag)
    toClientTag(tag)
  }

  override def toClientTag(tag: CompoundTag): CompoundTag = {
    tag.putIntArray("Bounds", Array(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ))
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
    fromClientTag(tag)
  }

  override def fromClientTag(tag: CompoundTag): Unit = {
    bounds = tag.getIntArray("Bounds") match {
      case Array(x0, y0, z0, x1, y1, z1) => IntBox(x0, y0, z0, x1, y1, z1)
      case _ => IntBox.Empty
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
