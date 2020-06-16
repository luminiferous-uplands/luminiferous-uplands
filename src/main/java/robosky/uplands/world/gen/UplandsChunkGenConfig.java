package robosky.uplands.world.gen;

import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import robosky.uplands.block.BlockRegistry;

public class UplandsChunkGenConfig extends ChunkGeneratorConfig {
    public UplandsChunkGenConfig() {
        setDefaultBlock(BlockRegistry.UPLANDER_STONE.getDefaultState());
    }
}
