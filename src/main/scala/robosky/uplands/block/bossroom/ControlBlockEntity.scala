package robosky.uplands.block.bossroom

import java.util.UUID

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.{BlockState, Blocks}
import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.entity.{Entity, EntityType, SpawnType}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.{BlockRotation, Identifier, Tickable}
import net.minecraft.util.math.{BlockPos, Box, Direction}
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import org.apache.logging.log4j.{LogManager, Logger}
import robosky.uplands.block
import robosky.uplands.block.BlockRegistry
import robosky.uplands.block.unbreakable.Unbreakable
import robosky.uplands.util.IntBox

import scala.collection.JavaConverters._

object ControlBlockEntity {

  val TYPE = BlockEntityType.Builder.create(() => new ControlBlockEntity(), BlockRegistry.BOSS_CONTROL).build(null)

  private val logger: Logger = LogManager.getLogger
}

/**
 * Controls boss room mechanics.
 */
class ControlBlockEntity extends BlockEntity(ControlBlockEntity.TYPE)
    with BlockEntityClientSerializable
    with Tickable {

  import ControlBlockEntity.logger

  /**
   * The bounds of the boss room, relative to the position of this instance.
   */
  def bounds: IntBox = _bounds

  private def bounds_=(value: IntBox) = {
    _bounds = value
    worldBoundsBox = new Box(
      this.pos.getX + value.minX,
      this.pos.getY + value.minY,
      this.pos.getZ + value.minZ,
      this.pos.getX + value.maxX,
      this.pos.getY + value.maxY,
      this.pos.getZ + value.maxZ)
  }

  private[this] var _bounds: IntBox = IntBox.Empty

  /**
   * The absolute world position of the boss room bounds. Assumes that the
   * position of the block entity does not change between ticks (a reasonable
   * assumption). This field is kept in sync wuth `bounds`.
   */
  @transient
  private var worldBoundsBox: Box = new Box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

  /**
   * The boss entity to spawn on command.
   */
  var bossType: EntityType[_] = EntityType.PIG

  /**
   * The boss entity UUID.
   */
  private var bossUuid: Option[UUID] = None

  /**
   * The boss entity. Late-initialized on the first tick.
   */
  @transient
  private var bossEntity: Option[Entity] = None

  var replacement: BlockState = Blocks.AIR.getDefaultState

  def adjustBounds(dir: Direction, blocks: Int): Unit = {
    dir match {
      case Direction.DOWN =>
        if (bounds.minY - blocks <= bounds.maxY) {
          bounds = bounds.copy(minY = bounds.minY - blocks)
        }
      case Direction.UP =>
        if (bounds.maxY + blocks >= bounds.minY) {
          bounds = bounds.copy(maxY = bounds.maxY + blocks)
        }
      case Direction.NORTH =>
        if (bounds.minZ - blocks <= bounds.maxZ) {
          bounds = bounds.copy(minZ = bounds.minZ - blocks)
        }
      case Direction.SOUTH =>
        if (bounds.maxZ + blocks >= bounds.minZ) {
          bounds = bounds.copy(maxZ = bounds.maxZ + blocks)
        }
      case Direction.WEST =>
        if (bounds.minX - blocks <= bounds.maxX) {
          bounds = bounds.copy(minX = bounds.minX - blocks)
        }
      case Direction.EAST =>
        if (bounds.maxX + blocks >= bounds.minX) {
          bounds = bounds.copy(maxX = bounds.maxX + blocks)
        }
    }
  }

  // this is an Iterator (rather than an Iterable) to preserve
  // the behavior of BlockPos.iterate. BlockPos.iterate internally
  // (re)uses a BlockPos.Mutable instance for each value, which
  // means that we can't store multiple values for later computation.
  private def bossRoomPositions: Iterator[BlockPos] =
    BlockPos.iterate(
      this.pos.getX + bounds.minX,
      this.pos.getY + bounds.minY,
      this.pos.getZ + bounds.minZ,
      this.pos.getX + bounds.maxX,
      this.pos.getY + bounds.maxY,
      this.pos.getZ + bounds.maxZ).iterator.asScala

  /**
   * Spawns the boss at the specified BlockPos.
   */
  def activateBoss(spawn: BlockPos): Unit = {
    if (!this.world.isClient && !bossUuid.isDefined) {
      for {
        pos <- bossRoomPositions
        state = this.world.getBlockState(pos)
        if state.getBlock == BlockRegistry.BOSS_DOORWAY
      } this.world.setBlockState(pos, state.`with`(DoorwayBlock.STATE, DoorwayState.BLOCKED))
      val boss = bossType.spawn(this.world, null, null, null, spawn, SpawnType.SPAWNER, false, false)
      bossEntity = Some(boss)
      bossUuid = Some(boss.getUuid)
      logger.info("Activated boss at {}", spawn)
    }
  }

  /**
   * Despawns the boss (e.g. if the player died while fighting)
   */
  def deactivateBoss(): Unit = {
    if (!this.world.isClient) {
      bossEntity foreach {
        boss => this.world match {
          case server: ServerWorld => server.removeEntity(boss)
          case _ =>
        }
      }
      bossUuid = None
      for {
        pos <- bossRoomPositions
        state = this.world.getBlockState(pos)
        if state.getBlock == BlockRegistry.BOSS_DOORWAY
      } this.world.setBlockState(pos, state.`with`(DoorwayBlock.STATE, DoorwayState.OPEN))
      logger.info("Deactivated boss")
    }
  }

  /**
   * Cleans up the boss room upon the boss's defeat. This includes replacing
   * unbreakable blocks with their breakable counterparts.
   */
  def onBossDefeat(): Unit = {
    bossUuid = None
    if (!this.world.isClient) {
      for {
        pos <- bossRoomPositions
        state = this.world.getBlockState(pos)
      } {
        state.getBlock match {
          case BlockRegistry.BOSS_DOORWAY =>
            this.world.setBlockState(pos, Blocks.AIR.getDefaultState)
          case u: Unbreakable =>
            this.world.setBlockState(pos, u.toBreakable(state))
          case _ =>
        }
      }
      this.world.setBlockState(this.pos, replacement)
      logger.info("Detected boss defeat")
    }
  }

  override def tick(): Unit = {
    if (!this.world.isClient) {
      // initialize bossEntity if it is not already
      // this is done in tick() to ensure that the entity
      // is loaded
      if (bossUuid.nonEmpty) {
        if (bossEntity.isEmpty) {
          this.world match {
            case server: ServerWorld =>
              bossEntity = bossUuid.map(server.getEntity)
            case _ =>
          }
        }
      } else {
        bossEntity = None
      }
      bossEntity foreach {
        boss =>
          if (!boss.isAlive) {
            onBossDefeat()
          } else {
            val players = this.world.getEntities(classOf[PlayerEntity], worldBoundsBox, null)
            if (players.isEmpty) {
              // everyone died (or escaped)
              deactivateBoss()
            }
          }
      }
    }
  }

  override def applyRotation(rot: BlockRotation): Unit = {
    import net.minecraft.util.BlockRotation._
    bounds = rot match {
      case NONE => bounds
      case COUNTERCLOCKWISE_90 => bounds.copy(
        minX = bounds.minZ,
        minZ = -bounds.maxX + 1,
        maxX = bounds.maxZ,
        maxZ = -bounds.minX + 1)
      case CLOCKWISE_180 => bounds.copy(
        minX = -bounds.maxX + 1,
        minZ = -bounds.maxZ + 1,
        maxX = -bounds.minX + 1,
        maxZ = -bounds.minZ + 1)
      case CLOCKWISE_90 => bounds.copy(
        minX = -bounds.maxZ + 1,
        minZ = bounds.minX,
        maxX = -bounds.minZ + 1,
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
    tag.putString("Replacement", block.stringifyState(replacement))
    tag
  }

  override def fromTag(tag: CompoundTag): Unit = {
    super.fromTag(tag)
    fromClientTag(tag)
  }

  override def fromClientTag(tag: CompoundTag): Unit = {
    bounds = tag.getIntArray("Bounds") match {
      case Array(x0, y0, z0, x1, y1, z1)
          if x0 <= x1 && y0 <= y1 && z0 <= z1 =>
        IntBox(x0, y0, z0, x1, y1, z1)
      case _ => IntBox.Empty
    }
    bossType = {
      for {
        id <- Option(Identifier.tryParse(tag.getString("Boss")))
        etyp <- Option[EntityType[_]](Registry.ENTITY_TYPE.get(id))
      } yield etyp
    } getOrElse EntityType.PIG
    bossUuid = {
      if (tag.contains("BossUUIDMost") && tag.contains("BossUUIDLeast"))
        Some(new UUID(tag.getLong("BossUUIDMost"), tag.getLong("BossUUIDLeast")))
      else
        None
    }
    replacement = block.parseState(tag.getString("Replacement"))
  }
}
