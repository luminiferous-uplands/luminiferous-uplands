package robosky.uplands.world.feature.minidungeons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

public final class TreehouseFeatureConfig implements FeatureConfig {

    public static final Codec<TreehouseFeatureConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Identifier.CODEC.fieldOf("template").forGetter(c -> c.template),
        Identifier.CODEC.fieldOf("loot").forGetter(c -> c.loot)
    ).apply(inst, TreehouseFeatureConfig::new));

    public final Identifier template;
    public final Identifier loot;

    public TreehouseFeatureConfig(Identifier template, Identifier loot) {
        this.template = template;
        this.loot = loot;
    }
}
