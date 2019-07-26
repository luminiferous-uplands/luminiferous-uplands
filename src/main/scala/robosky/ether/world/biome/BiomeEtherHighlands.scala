package robosky.ether.world.biome

import net.fabricmc.api.{EnvType, Environment}
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biome.Category
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.decorator.{Decorator, RangeDecoratorConfig}
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig
import robosky.ether.block.BlocksEther
import robosky.ether.world.biome.BiomeEtherHighlandsConfig._
import robosky.ether.world.feature.FeaturesEther
import robosky.ether.world.gen.EtherHighlandsSurfaceBuilder

object BiomeEtherHighlandsConfig {

  val ETHER_GRASS_DIRT_STONE_SURFACE: SurfaceConfig = new SurfaceConfig {
    override def getTopMaterial: BlockState = BlocksEther.ETHER_GRASS.getDefaultState

    override def getUnderMaterial: BlockState = BlocksEther.ETHER_DIRT.getDefaultState
  }

  val ETHER_HIGHLANDS_SURFACE_BUILDER = new EtherHighlandsSurfaceBuilder()
}

object BiomeEtherHighlands extends Biome((new Biome.Settings)
  .configureSurfaceBuilder(ETHER_HIGHLANDS_SURFACE_BUILDER, ETHER_GRASS_DIRT_STONE_SURFACE)
  .precipitation(Biome.Precipitation.NONE).category(Category.FOREST).depth(0.1F).scale(0.2F)
  .temperature(0.5F).downfall(0.0F).waterColor(11139071).waterFogColor(11139071)
  .category(Biome.Category.FOREST)) {

  addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Biome.configureFeature(FeaturesEther.oreFeature,
    new DefaultFeatureConfig, Decorator.COUNT_RANGE, new RangeDecoratorConfig(1, 0, 0, 256)))

  @Environment(EnvType.CLIENT)
  override def getSkyColor(currentTemperature: Float) = 12632319

  override def getGrassColorAt(pos: BlockPos) = 11665355

  override def getFoliageColorAt(pos: BlockPos) = 11665355
}
