package robosky.uplands.world

import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType
import net.minecraft.util.Identifier
import net.minecraft.world.World
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.chunk.{ChunkGenerator, ChunkGeneratorConfig, ChunkGeneratorType}
import robosky.uplands.world.gen.{EtherChunkGenConfig, EtherChunkGenerator}
import robosky.uplands.{UplandsMod, UplandsTeleporter}

object WorldRegistry {
  val ETHER_CHUNK_GENERATOR: ChunkGeneratorType[EtherChunkGenConfig.type, EtherChunkGenerator] =
    registerChunkGeneratorType(UplandsMod :/ "uplands_chunk_generator", new EtherChunkGenerator(_, _),
      () => EtherChunkGenConfig, appearsOnBuffet = false)

  val UPLANDS_DIMENSION: FabricDimensionType = FabricDimensionType.builder()
    .factory((t: World, u: DimensionType) => new UplandsDimension(t, u)).skyLight(true)
    .defaultPlacer(UplandsTeleporter.ToUplandsBeacon)
    .buildAndRegister(UplandsMod :/ "luminiferous_uplands")

  def init(): Unit = Unit

  private def registerChunkGeneratorType[C <: ChunkGeneratorConfig, T <: ChunkGenerator[C]](id: Identifier,
    factory: (World, BiomeSource) => T, supplier: () => C, appearsOnBuffet: Boolean): ChunkGeneratorType[C, T] =
    new ChunkGeneratorType[C, T](null, appearsOnBuffet, () => supplier()) {
      override def create(world: World, biomeSource: BiomeSource, c: C): T = factory(world, biomeSource)
    }
}
