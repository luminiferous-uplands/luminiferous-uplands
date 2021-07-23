package robosky.uplands.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

public class CloudFeatureConfig implements FeatureConfig {

    public static final Codec<CloudFeatureConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        BlockState.CODEC.fieldOf("state").forGetter(c -> c.state),
        Codec.intRange(1, 8).fieldOf("half_length").forGetter(c -> c.halfLength),
        Codec.intRange(1, 8).fieldOf("radius").forGetter(c -> c.radius),
        Codec.intRange(1, 8).fieldOf("height").forGetter(c -> c.height)
    ).apply(inst, CloudFeatureConfig::new));

    /**
     * The block state to place.
     */
    public final BlockState state;

    /**
     * Half the length of the cloud.
     */
    public final int halfLength;

    /**
     * The radius around which to place the cloud.
     */
    public final int radius;

    /**
     * The height of the cloud.
     */
    public final int height;

    public CloudFeatureConfig(BlockState state, int halfLength, int radius, int height) {
        this.state = state;
        this.halfLength = halfLength;
        this.radius = radius;
        this.height = height;
    }
}
