package robosky.uplands.world.gen

import net.minecraft.world.gen.chunk.ChunkGeneratorConfig
import robosky.uplands.block.BlockRegistry

object UplandsChunkGenConfig extends ChunkGeneratorConfig {
  setDefaultBlock(BlockRegistry.UPLANDER_STONE.getDefaultState)
}
