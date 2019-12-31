package robosky.uplands.util

case class IntBox(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int) {
  if (minX > maxX || minY > maxY || minZ > maxZ) {
    throw new IllegalArgumentException(s"Invalid bounds: ${(minX, minY, minZ, maxX, maxY, maxZ)}")
  }
}

object IntBox {

  val Empty: IntBox = new IntBox(0, 0, 0, 0, 0, 0)
}
