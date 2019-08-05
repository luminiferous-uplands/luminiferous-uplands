package robosky.ether.world.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.nbt.CompoundTag
import net.minecraft.structure.{StructureManager, StructurePieceType}
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.feature.{DefaultFeatureConfig, Feature, LakeFeatureConfig, StructureFeature}
import robosky.ether.UplandsMod
import robosky.ether.world.feature.megadungeon.{MegadungeonFeature, MegadungeonGenerator}
import robosky.ether.world.feature.minidungeons.{TreehouseFeature, TreehouseGenerator}
import robosky.ether.world.feature.trees.SkyrootTreeFeature

object FeatureRegistry {
  val oreFeature: EtherOreFeature.type = register("oregen", EtherOreFeature)
  val skyrootTreeFeature: SkyrootTreeFeature = register("skyroot_tree", new SkyrootTreeFeature(
    (t: Dynamic[_]) => DefaultFeatureConfig.deserialize(t), false))
  val skyLakeFeature: SkyLakeFeature = register("sky_lake", new SkyLakeFeature((t: Dynamic[_]) =>
    LakeFeatureConfig.deserialize(t)))

  val treehouseFeature: TreehouseFeature = register("treehouse", new TreehouseFeature((t: Dynamic[_]) =>
    DefaultFeatureConfig.deserialize(t)))
  Registry.register(Registry.STRUCTURE_FEATURE, UplandsMod :/ "treehouse", treehouseFeature)
  Feature.STRUCTURES.put("Uplands Treehouse", treehouseFeature.asInstanceOf[StructureFeature[_]])

  val spawnPlatformPiece: StructurePieceType = Registry.register[StructurePieceType](Registry.STRUCTURE_PIECE,
    UplandsMod :/ "spawn_platform", (var1: StructureManager, var2: CompoundTag) => new SpawnPlatformPiece(var1, var2))
  val treehousePiece: StructurePieceType = Registry.register[StructurePieceType](Registry.STRUCTURE_PIECE,
    UplandsMod :/ "minidungeons/treehouse", (var1: StructureManager, var2: CompoundTag) => new TreehouseGenerator
    .Piece(var1, var2))

  def register[A <: Feature[_]](name: String, f: A): A = Registry.register[A](Registry.FEATURE, UplandsMod :/ name, f)

  def init(): Unit = {
    MegadungeonFeature.register()
    MegadungeonGenerator.initialize()
  }
}
