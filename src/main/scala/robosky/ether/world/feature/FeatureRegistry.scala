package robosky.ether.world.feature

import java.util.Optional

import com.mojang.datafixers.Dynamic
import net.minecraft.nbt.CompoundTag
import net.minecraft.structure.{StructureManager, StructurePieceType}
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.feature.{DefaultFeatureConfig, Feature, LakeFeatureConfig}
import robosky.ether.UplandsMod
import robosky.ether.world.feature.minidungeons.{MinidungeonFeature, MinidungeonFeatureConfig}
import robosky.ether.world.feature.plants.SkyrootTreeFeature

object FeatureRegistry {
  val oreFeature: EtherOreFeature.type = register("oregen", EtherOreFeature)
  val skyrootTreeFeature: SkyrootTreeFeature = register("skyroot_tree", new SkyrootTreeFeature(
    (t: Dynamic[_]) => DefaultFeatureConfig.deserialize(t), false))
  val skyLakeFeature: SkyLakeFeature = register("sky_lake", new SkyLakeFeature((t: Dynamic[_]) =>
    LakeFeatureConfig.deserialize(t)))

  val treehouseFeature: MinidungeonFeature = new MinidungeonFeature(MinidungeonFeatureConfig("Uplands Treehouse",
    UplandsMod :/ "minidungeons/treehouse", Optional.of(UplandsMod :/ "chests/minidungeons/treehouse")))
    .register("treehouse")

  val spawnPlatformPiece: StructurePieceType = Registry.register[StructurePieceType](Registry.STRUCTURE_PIECE,
    UplandsMod :/ "spawn_platform", (var1: StructureManager, var2: CompoundTag) => new SpawnPlatformPiece(var1, var2))

  def register[A <: Feature[_]](name: String, f: A): A = Registry.register[A](Registry.FEATURE, UplandsMod :/ name, f)

  def init(): Unit = {}
}
