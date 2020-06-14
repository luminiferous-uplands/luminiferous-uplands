package robosky.uplands.world.biome;

import robosky.uplands.UplandsMod;
import robosky.uplands.block.BlockRegistry;
import robosky.uplands.world.gen.UplandsAutumnSurfaceBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;

public class BiomeRegistry {
    public static final SurfaceConfig UPLANDS_GRASS_DIRT_STONE_SURFACE = new SurfaceConfig() {
        @Override
        public BlockState getTopMaterial() {
            return BlockRegistry.UPLANDER_GRASS.getDefaultState();
        }

        @Override
        public BlockState getUnderMaterial() {
            return BlockRegistry.UPLANDER_DIRT.getDefaultState();
        }
    };
    public static final SurfaceBuilder<SurfaceConfig> UPLANDS_AUTUMN_SURFACE_BUILDER = new UplandsAutumnSurfaceBuilder();

    public static final Biome UPLANDS_VOID_BIOME = register("uplands_void", new UplandsVoidBiome());
    public static final Biome UPLANDS_AUTUMN_BIOME = register("uplands_autumn", new UplandsAutumnBiome());
    public static final Biome UPLANDS_PLAINS_BIOME = register("uplands_plains", new UplandsPlainsBiome());
    public static final Biome UPLANDS_SAVANNA_BIOME = register("uplands_savanna", new UplandsSavannaBiome());

    public static void init() {

    }

    private static Biome register(String name, Biome biome) {
        return Registry.register(Registry.BIOME, UplandsMod.id(name), biome);
    }
}
