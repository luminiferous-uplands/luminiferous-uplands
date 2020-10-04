package robosky.uplands.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

public final class UplandsOreFeatureConfig implements FeatureConfig {

    public static final Codec<UplandsOreFeatureConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Codec.INT.fieldOf("size").forGetter(c -> c.size),
        Codec.INT.fieldOf("min").forGetter(c -> c.minHeight),
        Codec.INT.fieldOf("max").forGetter(c -> c.maxHeight),
        BlockState.CODEC.fieldOf("state").forGetter(c -> c.state)
    ).apply(inst, UplandsOreFeatureConfig::new));

    private final int size;
    private final int minHeight;
    private final int maxHeight;
    private final BlockState state;

    public UplandsOreFeatureConfig(int size, int minHeight, int maxHeight, BlockState state) {
        this.size = size;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.state = state;
    }

    public int getSize() {
        return size;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public BlockState getState() {
        return state;
    }
}
