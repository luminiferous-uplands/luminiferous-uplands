package robosky.uplands.world.biome;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import robosky.uplands.UplandsMod;

public class BiomeRegistry {
    public static final Biome UPLANDS_VOID_BIOME = register("uplands_void", new UplandsVoidBiome());
    public static final Biome UPLANDS_AUTUMN_BIOME = register("uplands_autumn", new UplandsAutumnBiome());
    public static final Biome UPLANDS_PLAINS_BIOME = register("uplands_plains", new UplandsPlainsBiome());
    public static final Biome UPLANDS_SAVANNA_BIOME = register("uplands_savanna", new UplandsSavannaBiome());
    public static final Biome UPLANDS_MOUNTAINS_BIOME = register("uplands_mountains", new UplandsMountainsBiome());

    public static void init() {

    }

    private static Biome register(String name, Biome biome) {
        return Registry.register(Registry.BIOME, UplandsMod.id(name), biome);
    }
}
