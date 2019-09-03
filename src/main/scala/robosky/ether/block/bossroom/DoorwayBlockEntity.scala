package robosky.ether.block.bossroom

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable

import net.minecraft.block.{Blocks, BlockState}
import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

import robosky.ether.block.BlockRegistry

object DoorwayBlockEntity {

  val TYPE = BlockEntityType.Builder.create(() => new DoorwayBlockEntity(), BlockRegistry.BOSS_DOORWAY).build(null)
}

class DoorwayBlockEntity extends BlockEntity(DoorwayBlockEntity.TYPE) with BlockEntityClientSerializable {

  /**
   * When blocked, the block state this block entity appears as.
   */
  def mimicState: BlockState = _mimicState

  private[this] var _mimicState: BlockState = Blocks.AIR.getDefaultState

  override def toClientTag(tag: CompoundTag): CompoundTag = {
    tag.putString("Mimic", Registry.BLOCK.getId(mimicState.getBlock).toString)
    tag
  }

  override def toTag(tag: CompoundTag): CompoundTag = {
    toClientTag(super.toTag(tag))
  }

  override def fromClientTag(tag: CompoundTag): Unit = {
    _mimicState = {
      for {
        id <- Option(Identifier.tryParse(tag.getString("Mimic")))
        block <- Option(Registry.BLOCK.get(id))
      } yield block.getDefaultState
    } getOrElse Blocks.AIR.getDefaultState
  }

  override def fromTag(tag: CompoundTag): Unit = {
    super.fromTag(tag)
    fromClientTag(tag)
  }
}
