package robosky.uplands.world.gen

import java.util.Random

import net.minecraft.util.SystemUtil
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator

class UplandsChunkGenerator(world: World, biomeSource: BiomeSource)
  extends SurfaceChunkGenerator[UplandsChunkGenConfig.type](world, biomeSource, 4, 8, 128,
    UplandsChunkGenConfig, true) {
  private val BIOME_WEIGHT_TABLE = SystemUtil.consume(
    new Array[Float](25),
    (values: Array[Float]) =>
      for {
        dx <- -2 to 2
        dz <- -2 to 2
      } {
        val weight = 10.0F / MathHelper.sqrt(dx * dx + dz * dz + 0.2F)
        values(dx + 2 + (dz + 2) * 5) = weight
      }
  )

  override def getSpawnHeight: Int = 130

  override def getSeaLevel: Int = 0

  override def buildBedrock(chunk_1: Chunk, random_1: Random): Unit = Unit

  override protected def sampleNoiseColumn(doubles_1: Array[Double], int_1: Int, int_2: Int): Unit =
    this.sampleNoiseColumn(doubles_1, int_1, int_2, 684.412D, 684.412D * 4,
      684.412D / 80, 684.412D / 160, 3, -10)

  override protected def method_16409: Double = (this.getNoiseSizeY - 4) / 2

  override protected def method_16410 = 8.0D

  protected def computeNoiseRange(x: Int, z: Int): Array[Double] = {
    var scaleTotal = 0.0F
    var depthTotal = 0.0F
    var weightTotal = 0.0F
    val centerDepth = this.biomeSource.getBiomeForNoiseGen(x, z).getDepth
    for {
      dx <- -2 to 2
      dz <- -2 to 2
    } {
      val biome = this.biomeSource.getBiomeForNoiseGen(x + dx, z + dz)
      val depth = biome.getDepth
      val scale = biome.getScale
      var weight = BIOME_WEIGHT_TABLE(dx + 2 + (dz + 2) * 5)
      if (biome.getDepth > centerDepth) weight /= 2.0F
      scaleTotal += scale * weight
      depthTotal += depth * weight
      weightTotal += weight
    }

    scaleTotal /= weightTotal
    depthTotal /= weightTotal
    Array[Double](depthTotal, scaleTotal)
  }

  override protected def computeNoiseFalloff(double_1: Double, double_2: Double, int_1: Int): Double =
    12 - 15 * double_1
}
