package robosky.ether.world.feature.megadungeon

import com.mojang.datafixers.Dynamic
import net.minecraft.nbt.CompoundTag
import net.minecraft.structure.{StructureManager, StructurePieceType, StructureStart}
import net.minecraft.util.math.{BlockPos, MutableIntBoundingBox}
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.{AbstractTempleFeature, DefaultFeatureConfig, Feature, StructureFeature}
import robosky.ether.UplandsMod
import robosky.ether.world.feature.FeatureRegistry

object MegadungeonFeature extends AbstractTempleFeature[DefaultFeatureConfig]((dyn: Dynamic[_]) =>
  DefaultFeatureConfig.deserialize(dyn)) {

  val PIECE_TYPE: StructurePieceType = Registry.register[StructurePieceType](Registry.STRUCTURE_PIECE,
    UplandsMod :/ "megadungeon_piece", (mgr: StructureManager, tag: CompoundTag) => new MegadungeonPiece(mgr, tag))

  FeatureRegistry.register[MegadungeonFeature.type]("megadungeon", this)
  Registry.register(Registry.STRUCTURE_FEATURE, UplandsMod :/ "megadungeon", this)
  Feature.STRUCTURES.put(getName, this.asInstanceOf[StructureFeature[_]])

  def register(): Unit = {}

  override def getSeedModifier: Int = 165745296

  override def getStructureStartFactory: StructureFeature.StructureStartFactory = Start.apply

  override def getName: String = "Uplands_Megadungeon"

  override def getRadius: Int = 8

  private case class Start(feature: StructureFeature[_], x: Int, z: Int, biome: Biome, bbox: MutableIntBoundingBox, refs: Int, seed: Long)
    extends StructureStart(feature, x, z, biome, bbox, refs, seed) {
    override def initialize(chunkGenerator_1: ChunkGenerator[_], structureManager_1: StructureManager, int_1: Int, int_2: Int, biome_1: Biome): Unit = {
      val blockPos_1 = new BlockPos(int_1 * 16, 90, int_2 * 16)
      MegadungeonGenerator.addPieces(chunkGenerator_1, structureManager_1, blockPos_1, this.children, this.random)
      this.setBoundingBoxFromChildren()
    }
  }

}
