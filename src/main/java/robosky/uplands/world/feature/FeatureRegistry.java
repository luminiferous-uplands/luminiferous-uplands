package robosky.uplands.world.feature;

import java.util.Optional;

import robosky.uplands.UplandsMod;
import robosky.uplands.world.feature.megadungeon.MegadungeonFeature;
import robosky.uplands.world.feature.megadungeon.MegadungeonGenerator;
import robosky.uplands.world.feature.minidungeons.MinidungeonFeature;
import robosky.uplands.world.feature.minidungeons.MinidungeonFeatureConfig;
import robosky.uplands.world.feature.plants.SkyrootFlatTreeFeature;
import robosky.uplands.world.feature.plants.SkyrootTreeFeature;
import robosky.uplands.world.feature.plants.TallUplandsGrassFeature;
import robosky.uplands.world.feature.plants.UplandFlowerFeature;
import robosky.uplands.world.feature.plants.WaterChestnutFeature;
import robosky.uplands.world.feature.plants.ZephyrOnionFeature;

import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

public final class FeatureRegistry {
    public static final UplandsOreFeature ORE_FEATURE = register("oregen", UplandsOreFeature.INSTANCE);
    public static final SkyrootTreeFeature SKYROOT_TREE = register("skyroot_tree", new SkyrootTreeFeature(DefaultFeatureConfig::deserialize, false));
    public static final SkyrootFlatTreeFeature FLAT_SKYROOT_TREE = register("flat_skyroot_tree", new SkyrootFlatTreeFeature(DefaultFeatureConfig::deserialize, false));
    public static final SkyLakeFeature SKY_LAKE = register("sky_lake", new SkyLakeFeature(SingleStateFeatureConfig::deserialize));
    public static final WaterChestnutFeature WILD_WATER_CHESTNUTS = register("wild_water_chestnuts", new WaterChestnutFeature(DefaultFeatureConfig::deserialize));
    public static final UplandFlowerFeature UPLAND_FLOWER = register("upland_flower", new UplandFlowerFeature(DefaultFeatureConfig::deserialize));
    public static final ZephyrOnionFeature WILD_ZEPHYR_ONION = register("wild_zephyr_onion", new ZephyrOnionFeature(DefaultFeatureConfig::deserialize));
    public static final TallUplandsGrassFeature TALL_UPLANDS_GRASS = register("tall_uplands_grass", new TallUplandsGrassFeature(RandomPatchFeatureConfig::deserialize));

    public static final MinidungeonFeature TREEHOUSE = new MinidungeonFeature(
        new MinidungeonFeatureConfig("Uplands Treehouse",
            UplandsMod.id("minidungeons/treehouse"),
            Optional.of(UplandsMod.id("chests/minidungeons/treehouse")))
    ).register("treehouse");

    public static final StructurePieceType SPAWN_PLATFORM = Registry.register(Registry.STRUCTURE_PIECE, UplandsMod.id("spawn_platform"), SpawnPlatformPiece::new);

    public static <A extends Feature<?>> A register(String name, A f) {
        return Registry.register(Registry.FEATURE, UplandsMod.id(name), f);
    }

    public static void init() {
        MegadungeonFeature.register();
        MegadungeonGenerator.initialize();
    }
}
