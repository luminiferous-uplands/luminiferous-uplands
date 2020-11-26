package robosky.uplands.world.feature;

import robosky.structurehelpers.structure.pool.ExtendedStructureFeature;
import robosky.uplands.UplandsMod;
import robosky.uplands.world.feature.megadungeon.MegadungeonFeature;
import robosky.uplands.world.feature.minidungeons.MinidungeonFeature;
import robosky.uplands.world.feature.minidungeons.MinidungeonGenerator;
import robosky.uplands.world.feature.plants.SkyrootFlatTreeFeature;
import robosky.uplands.world.feature.plants.SkyrootTreeFeature;
import robosky.uplands.world.feature.plants.SkyrootTreeFeatureConfig;
import robosky.uplands.world.feature.plants.UplandFlowerFeature;
import robosky.uplands.world.feature.plants.WaterChestnutFeature;
import robosky.uplands.world.feature.plants.ZephyrOnionFeature;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

public final class FeatureRegistry {
    public static final UplandsOreFeature ORE_FEATURE = register("oregen", UplandsOreFeature.INSTANCE);
    public static final SkyrootTreeFeature SKYROOT_TREE = register("skyroot_tree", new SkyrootTreeFeature(SkyrootTreeFeatureConfig.CODEC, false));
    public static final SkyrootFlatTreeFeature FLAT_SKYROOT_TREE = register("flat_skyroot_tree", new SkyrootFlatTreeFeature(DefaultFeatureConfig.CODEC, false));
    public static final SkyLakeFeature SKY_LAKE = register("sky_lake", new SkyLakeFeature(SingleStateFeatureConfig.CODEC));
    public static final WaterChestnutFeature WILD_WATER_CHESTNUTS = register("wild_water_chestnuts", new WaterChestnutFeature(DefaultFeatureConfig.CODEC));
    public static final UplandFlowerFeature UPLAND_FLOWER = register("upland_flower", new UplandFlowerFeature(DefaultFeatureConfig.CODEC));
    public static final ZephyrOnionFeature WILD_ZEPHYR_ONION = register("wild_zephyr_onion", new ZephyrOnionFeature(DefaultFeatureConfig.CODEC));

    public static final MinidungeonFeature MINIDUNGEON =
        FabricStructureBuilder.create(UplandsMod.id("minidungeon"), new MinidungeonFeature())
            .step(GenerationStep.Feature.SURFACE_STRUCTURES)
            .defaultConfig(8, 4, 1)
            .register();
    public static final ExtendedStructureFeature MEGADUNGEON =
        FabricStructureBuilder.create(UplandsMod.id("megadungeon"), new MegadungeonFeature())
            .step(GenerationStep.Feature.UNDERGROUND_STRUCTURES)
            .defaultConfig(16, 4, 1)
            .register();

    public static final StructurePieceType SPAWN_PLATFORM = Registry.register(Registry.STRUCTURE_PIECE, UplandsMod.id("spawn_platform"), SpawnPlatformPiece::new);
    public static final StructurePieceType MINIDUNGEON_PIECE = Registry.register(Registry.STRUCTURE_PIECE, UplandsMod.id("minidungeon"), MinidungeonGenerator.Piece::new);

    public static <A extends Feature<?>> A register(String name, A f) {
        return Registry.register(Registry.FEATURE, UplandsMod.id(name), f);
    }

    public static void init() {
    }
}
