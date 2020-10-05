package robosky.uplands.world.biome;

import java.util.function.Function;

import robosky.uplands.UplandsMod;
import robosky.uplands.world.gen.UplandsAutumnSurfaceBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class BiomeRegistry {

    public static final SurfaceBuilder<TernarySurfaceConfig> UPLANDS_AUTUMN_SURFACE_BUILDER =
        register("uplands_surface_builder", new UplandsAutumnSurfaceBuilder());

    public static final RegistryKey<Biome> UPLANDS_VOID;
    public static final RegistryKey<Biome> UPLANDS_AUTUMN;
    public static final RegistryKey<Biome> UPLANDS_PLAINS;
    public static final RegistryKey<Biome> UPLANDS_SAVANNA;

    static {
        Function<Identifier, RegistryKey<Biome>> f = RegistryKey.createKeyFactory(Registry.BIOME_KEY);
        UPLANDS_VOID = f.apply(UplandsMod.id("uplands_void"));
        UPLANDS_AUTUMN = f.apply(UplandsMod.id("uplands_autumn"));
        UPLANDS_PLAINS = f.apply(UplandsMod.id("uplands_plains"));
        UPLANDS_SAVANNA = f.apply(UplandsMod.id("uplands_savanna"));
    }

    private static <T extends SurfaceBuilder<?>> T register(String name, T surfaceBuilder) {
        return Registry.register(Registry.SURFACE_BUILDER, UplandsMod.id(name), surfaceBuilder);
    }

    public static void init() {

    }
}
