package robosky.ether.world.gen

import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biome.Category
import net.minecraft.world.gen.surfacebuilder.{ConfiguredSurfaceBuilder, TernarySurfaceConfig}
import net.fabricmc.api.{EnvType, Environment}
import robosky.ether.block.BlocksEther
import BiomeEtherHighlandsConfig._

object BiomeEtherHighlandsConfig {

  val ETHER_GRASS_DIRT_HOLYSTONE_SURFACE =
    new TernarySurfaceConfig(BlocksEther.ETHER_GRASS.getDefaultState, BlocksEther.ETHER_DIRT.getDefaultState,
      BlocksEther.ETHER_STONE.getDefaultState)

  val ETHER_HIGHLANDS_SURFACE_BUILDER = new EtherHighlandsSurfaceBuilder()
}

object BiomeEtherHighlands extends Biome((new Biome.Settings)
  .surfaceBuilder(new ConfiguredSurfaceBuilder[TernarySurfaceConfig](ETHER_HIGHLANDS_SURFACE_BUILDER, ETHER_GRASS_DIRT_HOLYSTONE_SURFACE))
  .precipitation(Biome.Precipitation.NONE).category(Category.FOREST).depth(0.1F).scale(0.2F)
  .temperature(0.5F).downfall(0.0F).waterColor(11139071).waterFogColor(11139071)
  .parent(null.asInstanceOf[String])) {

  @Environment(EnvType.CLIENT)
  override def getSkyColor(currentTemperature: Float) = 12632319

  override def getGrassColorAt(pos: BlockPos) = 11665355

  override def getFoliageColorAt(pos: BlockPos) = 11665355
}
