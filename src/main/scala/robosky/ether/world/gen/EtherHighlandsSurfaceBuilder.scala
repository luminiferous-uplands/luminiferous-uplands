package robosky.ether.world.gen

import java.util.Random

import net.minecraft.block.{BlockState, Blocks}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.surfacebuilder.{SurfaceBuilder, SurfaceConfig}
import robosky.ether.block.BlockRegistry

class EtherHighlandsSurfaceBuilder extends SurfaceBuilder[SurfaceConfig](null) {
  def generate(rand: Random, chunk: Chunk, biome: Biome, absX: Int, absZ: Int, startHeight: Int, noise: Double,
    defaultBlock: BlockState, defaultFluid: BlockState, seaLevel: Int, seed: Long, config: SurfaceConfig): Unit = {
    var topBlock = BlockRegistry.ETHER_GRASS.getDefaultState
    var underBlock = BlockRegistry.ETHER_DIRT.getDefaultState
    val pos = new BlockPos.Mutable
    var depth = -1
    val maxDepth = (noise / 3.0D + 3.0D + rand.nextDouble * 0.25D).toInt
    val x = absX & 15
    val z = absZ & 15
    for (y <- startHeight to 0 by -1) {
      pos.set(x, y, z)
      val blockState_8 = chunk.getBlockState(pos)
      if (blockState_8.isAir) depth = -1
      else if (blockState_8.getBlock == defaultBlock.getBlock)
        if (depth == -1) {
          if (maxDepth <= 0) {
            topBlock = Blocks.AIR.getDefaultState
            underBlock = topBlock
          }
          chunk.setBlockState(pos, topBlock, false)
          depth = maxDepth
        } else if (depth > 0) {
          depth -= 1
          chunk.setBlockState(pos, underBlock, false)
        }
    }
  }
}
