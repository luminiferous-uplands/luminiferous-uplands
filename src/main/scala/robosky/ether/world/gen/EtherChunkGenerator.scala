package robosky.ether.world.gen

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator
import robosky.ether.block.BlocksEther
import net.minecraft.block.Blocks

class EtherChunkGenerator(world: World, biomeSource: BiomeSource)
  extends SurfaceChunkGenerator[EtherChunkGenConfig.type](world, biomeSource, 8, 4, 128,
    EtherChunkGenConfig, false) {

  override protected def sampleNoiseColumn(doubles_1: Array[Double], int_1: Int, int_2: Int): Unit =
    this.sampleNoiseColumn(doubles_1, int_1, int_2, 1368.824D, 684.412D, 17.110300000000002D,
      4.277575000000001D, 64, -3000)

  override def getSpawnHeight: Int = 50

  override def getSeaLevel: Int = 0

  override protected def computeNoiseRange(int_1: Int, int_2: Int): Array[Double] =
    Array[Double](this.biomeSource.method_8757(int_1, int_2).toDouble, 0.0D)

  override protected def computeNoiseFalloff(double_1: Double, double_2: Double, int_1: Int): Double = 8.0D - double_1
}
