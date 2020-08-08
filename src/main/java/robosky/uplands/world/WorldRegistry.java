package robosky.uplands.world;

import robosky.uplands.UplandsMod;
import robosky.uplands.UplandsTeleporter;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;

public final class WorldRegistry {

    public static final FabricDimensionType UPLANDS_DIMENSION = FabricDimensionType.builder()
        .factory(UplandsDimension::new)
        .skyLight(true)
        .defaultPlacer(UplandsTeleporter.TO_UPLANDS_BEACON)
        .buildAndRegister(UplandsMod.id("luminiferous_uplands"));

    public static void init() {
    }
}
