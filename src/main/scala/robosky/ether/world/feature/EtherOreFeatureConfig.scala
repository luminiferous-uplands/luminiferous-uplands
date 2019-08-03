package robosky.ether.world.feature

import com.google.common.collect.ImmutableMap

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps

import net.minecraft.block.{Block, Blocks, BlockState}
import net.minecraft.predicate.block.BlockPredicate
import net.minecraft.world.gen.feature.FeatureConfig

object EtherOreFeatureConfig {
  def deserialize(dyn: Dynamic[_]): EtherOreFeatureConfig = {
    val size = dyn.get("size").asInt(0)
    val min = dyn.get("min").asInt(0)
    val max = dyn.get("max").asInt(0)
    val state = dyn.get("state") map[BlockState]
      { d => BlockState.deserialize(d) } orElse { Blocks.AIR.getDefaultState }
    new EtherOreFeatureConfig(size, min, max, state)
  }

  def apply(size: Int, minHeight: Int, maxHeight: Int, block: Block): EtherOreFeatureConfig =
    new EtherOreFeatureConfig(size, minHeight, maxHeight, block.getDefaultState)
}

case class EtherOreFeatureConfig(
    size: Int,
    minHeight: Int,
    maxHeight: Int,
    state: BlockState) extends FeatureConfig {

  override def serialize[T](ops: DynamicOps[T]): Dynamic[T] = {
    return new Dynamic(ops, ops.createMap(ImmutableMap.of(
      ops.createString("size"), ops.createInt(size),
      ops.createString("min"), ops.createInt(minHeight),
      ops.createString("max"), ops.createInt(maxHeight),
      ops.createString("state"), BlockState.serialize(ops, state).getValue
    )))
  }
}
