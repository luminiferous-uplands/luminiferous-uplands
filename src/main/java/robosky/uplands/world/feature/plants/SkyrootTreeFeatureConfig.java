package robosky.uplands.world.feature.plants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

public final class SkyrootTreeFeatureConfig implements FeatureConfig {

    public static final Codec<SkyrootTreeFeatureConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        BlockState.CODEC.fieldOf("bark").forGetter(c -> c.bark),
        BlockState.CODEC.fieldOf("log").forGetter(c -> c.log),
        BlockState.CODEC.fieldOf("leaves").forGetter(c -> c.leaves),
        BlockState.CODEC.fieldOf("mushroom").forGetter(c -> c.mushroom),
        Codec.intRange(0, 65535).fieldOf("height").forGetter(c -> c.height)
    ).apply(inst, SkyrootTreeFeatureConfig::new));

    public final BlockState bark;
    public final BlockState log;
    public final BlockState leaves;
    public final BlockState mushroom;
    public final int height;

    public SkyrootTreeFeatureConfig(BlockState bark, BlockState log, BlockState leaves, BlockState mushroom, int height) {
        if(height < 0 || height > 65535) {
            throw new IllegalArgumentException("height");
        }
        this.bark = bark;
        this.log = log;
        this.leaves = leaves;
        this.mushroom = mushroom;
        this.height = height;
    }
}
