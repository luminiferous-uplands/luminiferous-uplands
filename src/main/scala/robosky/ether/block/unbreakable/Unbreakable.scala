package robosky.ether.block.unbreakable

import com.google.common.collect.ImmutableMap

import net.minecraft.block.{Block => McBlock, BlockState}
import net.minecraft.state.property.Property
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

/**
 * A mixin trait for unbreakable blocks.
 */
trait Unbreakable extends McBlock {

  /**
   * The block this ubreakable block is imitating.
   */
  val base: McBlock

  /**
   * Returns the breakable state equivalent of this unbreakable state.
   */
  def toBreakable(state: BlockState): BlockState = {
    import scala.language.existentials
    type Entries = ImmutableMap[Property[T], T] forSome { type T <: Comparable[T] }
    val propValues = state.getEntries.asInstanceOf[Entries]
    var res = base.getDefaultState
    propValues forEach { (p, v) => res = res.`with`(p, v) }
    res
  }

  override def getHardness(state: BlockState, world: BlockView, pos: BlockPos): Float = -1.0f

  override def getBlastResistance: Float = 3600000.0f
}
