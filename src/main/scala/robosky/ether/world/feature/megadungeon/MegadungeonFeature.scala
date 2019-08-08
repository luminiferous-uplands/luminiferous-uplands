package robosky.ether.world.feature.megadungeon

import java.util.Random

import com.mojang.datafixers.Dynamic
import net.minecraft.nbt.CompoundTag
import net.minecraft.structure.{StructureManager, StructurePieceType, StructureStart}
import net.minecraft.util.math.{BlockPos, ChunkPos, MutableIntBoundingBox}
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.{AbstractTempleFeature, DefaultFeatureConfig, Feature, StructureFeature}
import net.minecraft.world.{Heightmap, IWorld}
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

  override def getName: String = "uplands_megadungeon"

  override def getRadius: Int = 8

  private case class Start(feature: StructureFeature[_], x: Int, z: Int, biome: Biome, bbox: MutableIntBoundingBox, refs: Int, seed: Long)
    extends StructureStart(feature, x, z, biome, bbox, refs, seed) {
    override def initialize(chunkGenerator_1: ChunkGenerator[_], structureManager_1: StructureManager, int_1: Int, int_2: Int, biome_1: Biome): Unit = {
      val blockPos_1 = new BlockPos(int_1 * 16, 90, int_2 * 16)
      MegadungeonGenerator.addPieces(chunkGenerator_1, structureManager_1, blockPos_1, this.children, this.random)
      this.setBoundingBoxFromChildren()
    }

    override def generateStructure(world: IWorld, rand: Random, bbox: MutableIntBoundingBox, pos: ChunkPos): Unit = {
      for {
        x <- pos.getStartX + 5 to pos.getEndX - 5
        z <- pos.getStartZ + 5 to pos.getEndZ - 5
        if world.getBlockState(new BlockPos(x, world.getTop(Heightmap.Type.WORLD_SURFACE_WG, bbox.minX + 8,
          bbox.minZ + 8), z)).isAir
      } return
      super.generateStructure(world, rand, bbox, pos)
    }
  }

}
