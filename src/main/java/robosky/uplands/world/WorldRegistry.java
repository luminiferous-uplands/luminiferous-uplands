package robosky.uplands.world;

import robosky.uplands.UplandsMod;
import robosky.uplands.mixin.client.SkyPropertiesAccessor;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public final class WorldRegistry {

//    public static final FabricDimensionType UPLANDS_DIMENSION = FabricDimensionType.builder()
//        .factory(UplandsDimension::new)
//        .skyLight(true)
//        .defaultPlacer(UplandsTeleporter.TO_UPLANDS_BEACON)
//        .buildAndRegister(UplandsMod.id("luminiferous_uplands"));

    public static final RegistryKey<World> UPLANDS_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, UplandsMod.id("luminiferous_uplands"));
    public static final RegistryKey<DimensionType> UPLANDS_DIMENSION_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, UplandsMod.id("luminiferous_uplands"));

    public static void init() {
        SkyPropertiesAccessor.getBY_IDENTIFIER().put(UplandsMod.id("uplands_sky"), new UplandsSkyProperties());
    }
}
