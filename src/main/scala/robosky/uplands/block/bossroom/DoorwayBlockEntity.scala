package robosky.uplands.block.bossroom

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable

import net.minecraft.block.{Blocks, BlockState}
import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.BlockRotation

import robosky.uplands.block
import robosky.uplands.block.BlockRegistry

object DoorwayBlockEntity {

  val TYPE = BlockEntityType.Builder.create(() => new DoorwayBlockEntity(), BlockRegistry.BOSS_DOORWAY).build(null)
  //
  // private val blockStateParser: BlockStateArgumentType = BlockStateArgumentType.blockState
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
    tag.putString("Mimic", block.stringifyState(mimicState))
    tag
  }

  override def toTag(tag: CompoundTag): CompoundTag = {
    toClientTag(super.toTag(tag))
  }

  override def fromClientTag(tag: CompoundTag): Unit = {
    mimicState = block.parseState(tag.getString("Mimic"))
  }

  override def fromTag(tag: CompoundTag): Unit = {
    super.fromTag(tag)
    fromClientTag(tag)
  }
}
