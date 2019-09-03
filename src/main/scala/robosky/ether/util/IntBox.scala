package robosky.ether.util

case class IntBox(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int)

object IntBox {

  val Empty: IntBox = new IntBox(0, 0, 0, 0, 0, 0)
}
