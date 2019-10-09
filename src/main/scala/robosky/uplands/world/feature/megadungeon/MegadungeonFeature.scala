package robosky.uplands.world.feature.megadungeon

import java.util.Random

import com.mojang.datafixers.Dynamic
import net.minecraft.nbt.CompoundTag
import net.minecraft.structure.{StructureManager, StructurePieceType, StructureStart}
import net.minecraft.util.math.{BlockPos, MutableIntBoundingBox}
import net.minecraft.util.registry.Registry
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.{AbstractTempleFeature, DefaultFeatureConfig, Feature, StructureFeature}
import robosky.uplands.UplandsMod
import robosky.uplands.world.feature.FeatureRegistry

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

  override def getName: String = "uplands_megadungeon"

  override def getRadius: Int = 8


  override def shouldStartAt(chunkGenerator_1: ChunkGenerator[_], random_1: Random, int_1: Int, int_2: Int): Boolean = {
    val cpos = this.getStart(chunkGenerator_1, random_1, int_1, int_2, 0, 0)
    val center = cpos.getCenterBlockPos
    if (chunkGenerator_1.getHeightOnGround(center.getX, center.getZ, Heightmap.Type.WORLD_SURFACE_WG) < 30)
      false
    else
      super.shouldStartAt(chunkGenerator_1, random_1, int_1, int_2)
  }

  private case class Start(feature: StructureFeature[_], x: Int, z: Int, biome: Biome, bbox: MutableIntBoundingBox, refs: Int, seed: Long)
    extends StructureStart(feature, x, z, biome, bbox, refs, seed) {
    override def initialize(chunkGenerator_1: ChunkGenerator[_], structureManager_1: StructureManager, int_1: Int, int_2: Int, biome_1: Biome): Unit = {
      val blockPos_1 = new BlockPos(int_1 * 16, 90, int_2 * 16)
      MegadungeonGenerator.addPieces(chunkGenerator_1, structureManager_1, blockPos_1, this.children, this.random)
      this.setBoundingBoxFromChildren()
    }
  }

}
