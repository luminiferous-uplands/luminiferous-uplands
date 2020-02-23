package robosky.uplands

import com.mojang.brigadier.StringReader

import net.minecraft.block.{Blocks, BlockState}
import net.minecraft.command.arguments.{BlockStateArgument, BlockStateArgumentType}
import net.minecraft.state.property.Property
import net.minecraft.util.registry.Registry

import scala.util.Try

/**
 * Utility functions for the block package with no suitable class.
 */
package object block {

  private val blockStateParser: BlockStateArgumentType = BlockStateArgumentType.blockState

  def parseState(str: String): BlockState = {
    val rd = new StringReader(str)
    Try(blockStateParser.parse(rd): BlockStateArgument)
      .map(_.getBlockState)
      .getOrElse(Blocks.AIR.getDefaultState)
  }

  /**
   * Stringifies a block state into a form parsable by commands.
   */
  def stringifyState(state: BlockState) = {
    def stringifyEntry[T <: Comparable[T]](p: Property[T], v: Comparable[_]) =
      s"${p.getName}=${p.name(p.getType.cast(v))}"
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
}
