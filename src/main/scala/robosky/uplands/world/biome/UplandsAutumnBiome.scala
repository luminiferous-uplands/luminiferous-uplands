package robosky.uplands.world.biome

import net.fabricmc.api.{EnvType, Environment}
import net.minecraft.block.{BlockState, Blocks}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biome.Category
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.decorator.{ChanceDecoratorConfig, CountDecoratorConfig, CountExtraChanceDecoratorConfig, Decorator, BushDecoratorConfig, NoiseHeightmapDecoratorConfig, NopeDecoratorConfig, RangeDecoratorConfig}
import net.minecraft.world.gen.feature._
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig
import robosky.uplands.block.{BlockRegistry, UplandsOreBlock}
import robosky.uplands.world.biome.UplandsAutumnBiomeConfig._
import robosky.uplands.world.feature.{UplandsOreFeatureConfig, FeatureRegistry}
import robosky.uplands.world.feature.megadungeon.MegadungeonFeature
import robosky.uplands.world.gen.UplandsAutumnSurfaceBuilder

object UplandsAutumnBiomeConfig {

  val UPLANDS_GRASS_DIRT_STONE_SURFACE: SurfaceConfig = new SurfaceConfig {
    override def getTopMaterial: BlockState =
      BlockRegistry.UPLANDER_GRASS.getDefaultState

    override def getUnderMaterial: BlockState =
      BlockRegistry.UPLANDER_DIRT.getDefaultState
  }

  val UPLANDS_AUTUMN_SURFACE_BUILDER = new UplandsAutumnSurfaceBuilder()
}

object UplandsAutumnBiome
  extends Biome(new Biome.Settings().configureSurfaceBuilder(UPLANDS_AUTUMN_SURFACE_BUILDER,
    UPLANDS_GRASS_DIRT_STONE_SURFACE).precipitation(Biome.Precipitation.NONE).category(Category.FOREST)
    .depth(0.3F).scale(0.2F).temperature(0.5F).downfall(0.0F)
    .waterColor(0x9898BC).waterFogColor(0x9898BC).category(Biome.Category.FOREST)) {

  addFeature(GenerationStep.Feature.UNDERGROUND_ORES, FeatureRegistry.oreFeature.configure(
    UplandsOreFeatureConfig(9, 1, 128, BlockRegistry.UPLANDS_ORES(UplandsOreBlock.OreTypeAegisalt)))
    .createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(1, 0, 0, 256))))
  addFeature(GenerationStep.Feature.UNDERGROUND_ORES, FeatureRegistry.oreFeature.configure(
    UplandsOreFeatureConfig(20, 1, 64, BlockRegistry.LODESTONE))
    .createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(4, 0, 0, 256))))
  addFeature(GenerationStep.Feature.VEGETAL_DECORATION, FeatureRegistry.skyrootTreeFeature.configure(FeatureConfig.DEFAULT)
    .createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(2, 0.1f, 1))))

  addFeature(GenerationStep.Feature.VEGETAL_DECORATION, FeatureRegistry.waterChestnutFeature.configure(FeatureConfig.DEFAULT)
    .createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(2, 0.1f, 1))))

  // addFeature(GenerationStep.Feature.VEGETAL_DECORATION, FeatureRegistry.uplandFlowerFeature.configure(FeatureConfig.DEFAULT)
  //   .createDecoratedFeature(Decorator.NOISE_HEIGHTMAP_32.configure(new NoiseHeightmapDecoratorConfig(-0.8D, 15, 4))))

  addFeature(GenerationStep.Feature.VEGETAL_DECORATION, FeatureRegistry.tallUplandsGrassFeature.configure(new DefaultFeatureConfig())
    .createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_DOUBLE.configure(new CountDecoratorConfig(2))))

  addFeature(GenerationStep.Feature.VEGETAL_DECORATION, FeatureRegistry.zephyrOnionFeature.configure(FeatureConfig.DEFAULT)
    .createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_32.configure(new CountDecoratorConfig(3))))

  addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, FeatureRegistry.skyLakeFeature.configure(
    new BushFeatureConfig(Blocks.WATER.getDefaultState))
    .createDecoratedFeature(Decorator.WATER_LAKE.configure(new BushDecoratorConfig(8))))

  addStructureFeature(FeatureRegistry.treehouseFeature.configure(FeatureConfig.DEFAULT))
  addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, FeatureRegistry.treehouseFeature.configure(FeatureConfig.DEFAULT)
    .createDecoratedFeature(Decorator.CHANCE_PASSTHROUGH.configure(new ChanceDecoratorConfig(100))))

  addStructureFeature(MegadungeonFeature.configure(FeatureConfig.DEFAULT))
  addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, MegadungeonFeature.configure(FeatureConfig.DEFAULT)
    .createDecoratedFeature(Decorator.NOPE.configure(new NopeDecoratorConfig())))

  @Environment(EnvType.CLIENT)
  override def getSkyColor(currentTemperature: Float) = 12632319

  override def getGrassColorAt(pos: BlockPos) = 15382866

  override def getFoliageColorAt(pos: BlockPos) = 15382866

  override def computeTemperature(blockPos_1: BlockPos): Float = getTemperature()
}
