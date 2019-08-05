package robosky.ether.world.gen

import net.minecraft.world.gen.chunk.ChunkGeneratorConfig
import robosky.ether.block.BlockRegistry

object EtherChunkGenConfig extends ChunkGeneratorConfig {
  setDefaultBlock(BlockRegistry.ETHER_STONE.getDefaultState)
}
