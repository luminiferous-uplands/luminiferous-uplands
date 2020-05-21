package robosky.uplands.world.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.IdentityCoordinateTransformer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import robosky.uplands.world.biome.BiomeRegistry;

public enum BiomeDistributionLayer implements InitLayer, IdentityCoordinateTransformer {
    INSTANCE;

    @Override
    public int sample(LayerRandomnessSource random, int x, int z) {
        return random.nextInt(2) == 0 ? Registry.BIOME.getRawId(BiomeRegistry.UPLANDS_AUTUMN_BIOME) : Registry.BIOME.getRawId(BiomeRegistry.UPLANDS_PLAINS_BIOME);
    }
}
