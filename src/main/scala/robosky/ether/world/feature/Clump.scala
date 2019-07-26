package robosky.ether.world.feature

import java.util.Random

import com.google.common.primitives.Floats
import net.minecraft.util.math.{BlockPos, Vec3i}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Clump {
  def of(r: Float): Clump = {
    val ir = Math.ceil(r).toInt
    val result = new Clump((for {z <- -ir to ir
                                 x <- -ir to ir
                                 y <- -ir to ir
                                 d = Math.sqrt(x * x + y * y + z * z).toFloat
                                 if d <= r
                                 } yield new Clump.Entry(x, y, z, d)).toBuffer)

    if (result.entries.isEmpty) result.entries += new Clump.Entry(0, 0, 0, 0)
    result.entries.sortWith((a: Clump.Entry, b: Clump.Entry) => Floats.compare(a.d, b.d) < 0)
    result
  }

  private[feature] class Entry(var x: Int, var y: Int, var z: Int, var d: Float) {
    def pos = new Vec3i(x, y, z)

    def blockPos(x: Int, y: Int, z: Int) = new BlockPos(this.x + x, this.y + y, this.z + z)
  }

}

class Clump(val entries: mutable.Buffer[Clump.Entry] = ArrayBuffer.empty) {
  def removeUniform(rand: Random, dx: Int, dy: Int, dz: Int): BlockPos = {
    val index = rand.nextInt(entries.size)
    entries.remove(index).blockPos(dx, dy, dz)
  }

  def removeGaussian(rand: Random, dx: Int, dy: Int, dz: Int): BlockPos = {
    var index = Math.abs(rand.nextGaussian * entries.size).asInstanceOf[Int]
    if (index >= entries.size) index = 0 //in unlikely scenarios, gaussian numbers can go way outside the bounds.
    if (index < 0) index = 0
    entries.remove(index).blockPos(dx, dy, dz)
  }

  def copy: Clump = new Clump(this.entries.clone())

  def size: Int = entries.size

  def isEmpty: Boolean = entries.isEmpty
}
