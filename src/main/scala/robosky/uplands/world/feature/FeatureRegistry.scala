package robosky.uplands.world.feature

import java.util.Optional

import com.mojang.datafixers.Dynamic
import net.minecraft.nbt.CompoundTag
import net.minecraft.structure.{StructureManager, StructurePieceType}
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.feature.{DefaultFeatureConfig, Feature, RandomPatchFeatureConfig, SingleStateFeatureConfig}
import robosky.uplands.UplandsMod
import robosky.uplands.world.feature.megadungeon.{MegadungeonFeature, MegadungeonGenerator}
import robosky.uplands.world.feature.minidungeons.{MinidungeonFeature, MinidungeonFeatureConfig}
import robosky.uplands.world.feature.plants.{SkyrootTreeFeature, TallUplandsGrassFeature, UplandFlowerFeature, WaterChestnutFeature, ZephyrOnionFeature}

object FeatureRegistry {
  val oreFeature: UplandsOreFeature.type = register("oregen", UplandsOreFeature)
  val skyrootTreeFeature: SkyrootTreeFeature = register("skyroot_tree", new SkyrootTreeFeature(
    (t: Dynamic[_]) => DefaultFeatureConfig.deserialize(t), false))

  val skyLakeFeature: SkyLakeFeature = register("sky_lake", new SkyLakeFeature((t: Dynamic[_]) =>
    SingleStateFeatureConfig.deserialize(t)))

  val waterChestnutFeature: WaterChestnutFeature = register("wild_water_chestnuts", new WaterChestnutFeature((t: Dynamic[_]) => DefaultFeatureConfig.deserialize(t)))

  val uplandFlowerFeature: UplandFlowerFeature = register("upland_flower", new UplandFlowerFeature((t: Dynamic[_]) => DefaultFeatureConfig.deserialize(t)))

  val zephyrOnionFeature: ZephyrOnionFeature = register("wild_zephyr_onion", new ZephyrOnionFeature((t: Dynamic[_]) => DefaultFeatureConfig.deserialize(t)))

  val tallUplandsGrassFeature: TallUplandsGrassFeature = register("tall_uplands_grass", new TallUplandsGrassFeature((t: Dynamic[_]) => RandomPatchFeatureConfig.deserialize(t)))

  val treehouseFeature: MinidungeonFeature = new MinidungeonFeature(MinidungeonFeatureConfig("Uplands Treehouse",
    UplandsMod :/ "minidungeons/treehouse", Optional.of(UplandsMod :/ "chests/minidungeons/treehouse")))
    .register("treehouse")

  val spawnPlatformPiece: StructurePieceType = Registry.register[StructurePieceType](Registry.STRUCTURE_PIECE,
    UplandsMod :/ "spawn_platform", (var1: StructureManager, var2: CompoundTag) => new SpawnPlatformPiece(var1, var2))

  def register[A <: Feature[_]](name: String, f: A): A = Registry.register[A](Registry.FEATURE, UplandsMod :/ name, f)

  def init(): Unit = {
    MegadungeonFeature.register()
    MegadungeonGenerator.initialize()
  }
}
