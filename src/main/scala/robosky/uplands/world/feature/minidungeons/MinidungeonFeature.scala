package robosky.uplands.world.feature.minidungeons

import java.util.Random

import net.minecraft.nbt.CompoundTag
import net.minecraft.structure.{StructureManager, StructurePieceType, StructureStart}
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.{BlockBox, BlockPos}
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.BiomeAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.{DefaultFeatureConfig, Feature, StructureFeature}
import robosky.uplands.UplandsMod
import robosky.uplands.world.feature.FeatureRegistry

class MinidungeonFeature(conf: MinidungeonFeatureConfig)
  extends StructureFeature[DefaultFeatureConfig](dyn => DefaultFeatureConfig.deserialize(dyn)) {

  case class Start private[minidungeons](feature: StructureFeature[_], chunkX: Int, chunkZ: Int,
    bbox: BlockBox, references: Int, seed: Long) extends StructureStart(feature, chunkX, chunkZ,
    bbox, references, seed) {

    override def initialize(generator: ChunkGenerator[_], mgr: StructureManager, chunkX: Int, chunkZ: Int,
      biome: Biome): Unit = {
      val x = chunkX * 16
      val z = chunkZ * 16
      val startingPos = new BlockPos(x, 0, z)
      val rotation = BlockRotation.NONE
      MinidungeonGenerator.addParts(mgr, startingPos, rotation, this.children, conf)
      this.setBoundingBoxFromChildren()
    }
  }

  def register(name: String): MinidungeonFeature = {
    val f = FeatureRegistry.register(name, this)
    Registry.register(Registry.STRUCTURE_FEATURE, UplandsMod :/ name, this)
    Feature.STRUCTURES.put(conf.name, this.asInstanceOf[StructureFeature[_]])
    var tpe: StructurePieceType = null // Use var instead of a single recursive val because scalac is dumb
    tpe = Registry.register[StructurePieceType](Registry.STRUCTURE_PIECE, conf.template, (var1: StructureManager,
      var2: CompoundTag) => new MinidungeonGenerator.Piece(tpe, var1, var2))
    f
  }

  override def shouldStartAt(var1: BiomeAccess, var2: ChunkGenerator[_], var3: Random, var4: Int, var5: Int, var6: Biome) = true

  override def getStructureStartFactory: StructureFeature.StructureStartFactory = Start.apply: StructureFeature.StructureStartFactory

  override def getName: String = conf.name

  override def getRadius = 8
}
