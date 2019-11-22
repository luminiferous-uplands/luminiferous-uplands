package robosky.uplands.block

import net.minecraft.state.property.{Property, IntProperty}

object UplandsWaterBlock {

  val MAX_FALL: Int = 20

  val FALL: Property[Integer] = IntProperty.of("fall", 0, MAX_FALL)
}
