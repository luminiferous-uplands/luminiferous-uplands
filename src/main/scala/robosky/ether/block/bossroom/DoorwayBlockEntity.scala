package robosky.ether.block.bossroom

import com.mojang.brigadier.StringReader

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable

import net.minecraft.block.{Blocks, BlockState}
import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.command.arguments.BlockStateArgumentType
import net.minecraft.nbt.CompoundTag
import net.minecraft.state.property.Property
import net.minecraft.util.BlockRotation
import net.minecraft.util.registry.Registry

import robosky.ether.block.BlockRegistry

import scala.util.Try

object DoorwayBlockEntity {

  val TYPE = BlockEntityType.Builder.create(() => new DoorwayBlockEntity(), BlockRegistry.BOSS_DOORWAY).build(null)

  private val blockStateParser: BlockStateArgumentType = BlockStateArgumentType.blockState
}

class DoorwayBlockEntity extends BlockEntity(DoorwayBlockEntity.TYPE) with BlockEntityClientSerializable {

  /**
   * When blocked, the block state this block entity appears as.
   */
  def mimicState: BlockState = _mimicState

  def mimicState_=(value: BlockState): Unit = {
    _mimicState = value
    if (this.hasWorld) {
      _lastMimicUpdate = this.world.getTime
    }
  }

  private[this] var _mimicState: BlockState = Blocks.AIR.getDefaultState

  /**
   * The last time the mimic state was updated.
   */
  def lastMimicUpdate: Long = _lastMimicUpdate

  @transient
  private[this] var _lastMimicUpdate: Long = 0L

  override def applyRotation(rot: BlockRotation): Unit = {
    // silent update
    _mimicState = mimicState.rotate(rot)
  }

  override def toClientTag(tag: CompoundTag): CompoundTag = {
    def stringifyEntry[T <: Comparable[T]](p: Property[T], v: Comparable[_]) =
      s"${p.getName}=${p.getName(p.getValueType.cast(v))}"
    def stringify(state: BlockState) = {
      import scala.collection.JavaConverters._
      val id = Registry.BLOCK.getId(state.getBlock)
      val props = state.getEntries
      val propValues = {
        if (!props.isEmpty) {
          props.asScala.map {
            case (p, v) => stringifyEntry(p, v)
          }.mkString("[", ",", "]")
        } else ""
      }
      id + propValues
    }
    tag.putString("Mimic", stringify(mimicState))
    tag
  }

  override def toTag(tag: CompoundTag): CompoundTag = {
    toClientTag(super.toTag(tag))
  }

  override def fromClientTag(tag: CompoundTag): Unit = {
    mimicState = {
      val rd = new StringReader(tag.getString("Mimic"))
      Try(DoorwayBlockEntity.blockStateParser.method_9654(rd))
        .map(_.getBlockState)
        .getOrElse(Blocks.AIR.getDefaultState)
    }
  }

  override def fromTag(tag: CompoundTag): Unit = {
    super.fromTag(tag)
    fromClientTag(tag)
  }
}
