package robosky.uplands.world.feature.minidungeons;

import java.util.Optional;

import net.minecraft.util.Identifier;

// Does not extend FeatureConfig so we can get it into the StructureStart because this shit is dumb
// Uses java Optional because we need to get a value out from a java class
public final class MinidungeonFeatureConfig {
    private final String name;
    private final Identifier template;
    private final Optional<Identifier> loot;

    public MinidungeonFeatureConfig(String name, Identifier template, Optional<Identifier> loot) {
        this.name = name;
        this.template = template;
        this.loot = loot;
    }

    public String getName() {
        return name;
    }

    public Identifier getTemplate() {
        return template;
    }

    public Optional<Identifier> getLoot() {
        return loot;
    }
}
