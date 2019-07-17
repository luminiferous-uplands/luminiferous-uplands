package robosky.ether.world.gen

import net.minecraft.world.gen.chunk.ChunkGeneratorConfig
import robosky.ether.block.BlocksEther

object EtherChunkGenConfig extends ChunkGeneratorConfig {
  setDefaultBlock(BlocksEther.ETHER_STONE.getDefaultState)
}
