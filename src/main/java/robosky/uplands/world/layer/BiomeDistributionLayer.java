package robosky.uplands.world.layer;

import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.IdentityCoordinateTransformer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum BiomeDistributionLayer implements InitLayer, IdentityCoordinateTransformer {
    INSTANCE;

    @Override
    public int sample(LayerRandomnessSource random, int x, int z) {
//        switch(random.nextInt(3)) {
//            case 0:
//                return Registry.BIOME.getRawId(BiomeRegistry.UPLANDS_AUTUMN);
//            case 1:
//                return Registry.BIOME.getRawId(BiomeRegistry.UPLANDS_PLAINS);
//            default:
//                return Registry.BIOME.getRawId(BiomeRegistry.UPLANDS_SAVANNA);
//        }
        return 0;
    }
}
