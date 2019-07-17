package robosky.ether.world.gen

import java.util.Random

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.surfacebuilder.{SurfaceBuilder, TernarySurfaceConfig}

class EtherHighlandsSurfaceBuilder extends SurfaceBuilder[TernarySurfaceConfig](null) {
  override def generate(random: Random, chunk: Chunk, biome: Biome, x: Int, z: Int, startHeight: Int, v: Double,
                        defaultBlock: BlockState, defaultFluid: BlockState, seaLevel: Int, seed: Long, config: TernarySurfaceConfig): Unit = {
    import net.minecraft.block.Blocks
    var topBlock = config.getTopMaterial
    var fillerBlock = config.getUnderMaterial
    val pos = new BlockPos.Mutable()

    var int_5 = -1
    val int_6 = (3.0D + random.nextDouble * 0.25D).asInstanceOf[Int]

    for (y <- 128 to 0 by -1) {
      pos.set(x, y, z)
      val state = chunk.getBlockState(pos)
      if (state.isAir) int_5 = -1
      else if (state.getBlock eq defaultBlock.getBlock) if (int_5 == -1) {
        if (int_6 <= 0) {
          topBlock = Blocks.AIR.getDefaultState
          fillerBlock = defaultBlock
        }
        int_5 = int_6
        if (y >= 0) chunk.setBlockState(pos, topBlock, false)
        else chunk.setBlockState(pos, fillerBlock, false)
      }
      else if (int_5 > 0) {
        int_5 -= 1
        chunk.setBlockState(pos, fillerBlock, false)
      }
    }
  }
}
