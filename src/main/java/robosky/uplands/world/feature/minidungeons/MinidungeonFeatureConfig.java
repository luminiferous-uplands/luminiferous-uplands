package robosky.uplands.world.feature.minidungeons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

public final class MinidungeonFeatureConfig implements FeatureConfig {

    public static final Codec<MinidungeonFeatureConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Identifier.CODEC.fieldOf("template").forGetter(c -> c.template),
        Identifier.CODEC.fieldOf("loot").forGetter(c -> c.loot)
    ).apply(inst, MinidungeonFeatureConfig::new));

    public final Identifier template;
    public final Identifier loot;

    public MinidungeonFeatureConfig(Identifier template, Identifier loot) {
        this.template = template;
        this.loot = loot;
    }
}
