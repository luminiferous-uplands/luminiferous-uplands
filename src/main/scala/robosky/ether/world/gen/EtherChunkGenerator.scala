package robosky.ether.world.gen

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator
import robosky.ether.block.BlocksEther

class EtherChunkGenerator(world: World, biomeSource: BiomeSource)
  extends SurfaceChunkGenerator[EtherChunkGenConfig.type](world, biomeSource, 8, 4, 128, EtherChunkGenConfig, false) {

  import net.minecraft.block.Blocks

  def prepareHeights(x: Int, z: Int, primer: Chunk): Unit = {
    val buffer = new Array[Double](3366)
    sampleNoiseColumn(buffer, x * 2, z * 2)
    val pos = new BlockPos.Mutable
    for (i1 <- 0 until 2) {
      for (j1 <- 0 until 2) {
        for (k1 <- 0 until 2) {
          var d1 = buffer((i1 * 3 + j1) * 33 + k1)
          var d2 = buffer((i1 * 3 + j1 + 1) * 33 + k1)
          var d3 = buffer(((i1 + 1) * 3 + j1) * 33 + k1)
          var d4 = buffer(((i1 + 1) * 3 + j1 + 1) * 33 + k1)
          for (l1 <- 0 until 4) {
            var d10 = d1
            var d11 = d2
            for (i2 <- 0 until 8) {
              var d15 = d10
              for (k2 <- 0 until 8) {
                pos.set(i2 + i1 * 8, l1 + k1 * 4, k2 + j1 * 8)
                var filler = Blocks.AIR.getDefaultState
                if (d15 > 0.0D) filler = BlocksEther.ETHER_STONE.getDefaultState
                primer.setBlockState(pos, filler, false)
                d15 += (d11 - d10) * 0.125D
              }
              d10 += (d3 - d1) * 0.125D
              d11 += (d4 - d2) * 0.125D

            }
            d1 += (buffer((i1 * 3 + j1) * 33 + k1 + 1) - d1) * 0.25D
            d2 += (buffer((i1 * 3 + j1 + 1) * 33 + k1 + 1) - d2) * 0.25D
            d3 += (buffer(((i1 + 1) * 3 + j1) * 33 + k1 + 1) - d3) * 0.25D
            d4 += (buffer(((i1 + 1) * 3 + j1 + 1) * 33 + k1 + 1) - d4) * 0.25D
          }
        }
      }
    }
  }

  override protected def sampleNoiseColumn(doubles_1: Array[Double], int_1: Int, int_2: Int): Unit =
    this.sampleNoiseColumn(doubles_1, int_1, int_2, 1368.824D, 684.412D, 17.110300000000002D,
      4.277575000000001D, 64, -3000)

  override def getSpawnHeight: Int = 50

  override def getSeaLevel: Int = 0

  override protected def computeNoiseRange(int_1: Int, int_2: Int): Array[Double] =
    Array[Double](this.biomeSource.method_8757(int_1, int_2).toDouble, 0.0D)

  override protected def computeNoiseFalloff(double_1: Double, double_2: Double, int_1: Int): Double = 8.0D - double_1
}
