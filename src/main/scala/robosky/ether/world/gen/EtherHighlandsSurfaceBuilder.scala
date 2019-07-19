package robosky.ether.world.gen

import java.util.Random

import net.minecraft.block.{BlockState, Blocks}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.surfacebuilder.{SurfaceBuilder, TernarySurfaceConfig}

class EtherHighlandsSurfaceBuilder extends SurfaceBuilder[TernarySurfaceConfig](null) {
  def generate(rand: Random, chunk: Chunk, biome: Biome, absX: Int, absZ: Int, startHeight: Int, noise: Double,
               defaultBlock: BlockState, defaultFluid: BlockState, seaLevel: Int, seed: Long,
               config: TernarySurfaceConfig): Unit = {
    var topBlock = config.getTopMaterial
    var underBlock = config.getUnderMaterial
    val pos = new BlockPos.Mutable
    var int_5 = -1
    val int_6 = (noise / 3.0D + 3.0D + rand.nextDouble * 0.25D).toInt
    val x = absX & 15
    val z = absZ & 15
    for (y <- 128 to 0 by -1) {
      pos.set(x, y, z)
      val blockState_8 = chunk.getBlockState(pos)
      if (blockState_8.isAir) int_5 = -1
      else if (blockState_8.getBlock eq defaultBlock.getBlock) if (int_5 == -1) {
        if (int_6 <= 0) {
          topBlock = Blocks.AIR.getDefaultState
          underBlock = defaultBlock
        }
        int_5 = int_6
        chunk.setBlockState(pos, topBlock, false)
      }
      else if (int_5 > 0) {
        int_5 -= 1
        chunk.setBlockState(pos, underBlock, false)
      }
    }
  }
}
