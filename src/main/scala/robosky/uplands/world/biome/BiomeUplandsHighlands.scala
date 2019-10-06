package robosky.uplands.world.biome

import net.fabricmc.api.{EnvType, Environment}
import net.minecraft.block.{BlockState, Blocks}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biome.Category
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.decorator.{ChanceDecoratorConfig, CountDecoratorConfig, CountExtraChanceDecoratorConfig, Decorator, LakeDecoratorConfig, NoiseHeightmapDecoratorConfig, RangeDecoratorConfig}
import net.minecraft.world.gen.feature._
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig
import robosky.uplands.block.{BlockRegistry, UplandsOreBlock}
import robosky.uplands.world.biome.BiomeEtherHighlandsConfig._
import robosky.uplands.world.feature.{UplandsOreFeatureConfig, FeatureRegistry}
import robosky.uplands.world.gen.EtherHighlandsSurfaceBuilder

object BiomeEtherHighlandsConfig {

  val ETHER_GRASS_DIRT_STONE_SURFACE: SurfaceConfig = new SurfaceConfig {
    override def getTopMaterial: BlockState =
      BlockRegistry.UPLANDER_GRASS.getDefaultState

    override def getUnderMaterial: BlockState =
      BlockRegistry.UPLANDER_DIRT.getDefaultState
  }

  val ETHER_HIGHLANDS_SURFACE_BUILDER = new EtherHighlandsSurfaceBuilder()
}

object BiomeUplandsHighlands
  extends Biome(new Biome.Settings().configureSurfaceBuilder(ETHER_HIGHLANDS_SURFACE_BUILDER,
    ETHER_GRASS_DIRT_STONE_SURFACE).precipitation(Biome.Precipitation.NONE).category(Category.FOREST)
    .depth(0.3F).scale(0.2F).temperature(0.5F).downfall(0.0F)
    .waterColor(0x9898BC).waterFogColor(0x9898BC).category(Biome.Category.FOREST)) {

  addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Biome.configureFeature(FeatureRegistry.oreFeature,
    UplandsOreFeatureConfig(9, 1, 128, BlockRegistry.ETHER_ORES(UplandsOreBlock.OreTypeAegisalt)),
    Decorator.COUNT_RANGE, new RangeDecoratorConfig(1, 0, 0, 256)))
  addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Biome.configureFeature(FeatureRegistry.oreFeature,
    UplandsOreFeatureConfig(20, 1, 64, BlockRegistry.LODESTONE),
    Decorator.COUNT_RANGE, new RangeDecoratorConfig(4, 0, 0, 256)))
  addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(FeatureRegistry.skyrootTreeFeature,
    FeatureConfig.DEFAULT, Decorator.COUNT_EXTRA_HEIGHTMAP, new CountExtraChanceDecoratorConfig(2, 0.1f, 1)))

  addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(FeatureRegistry.waterChestnutFeature,
    FeatureConfig.DEFAULT, Decorator.COUNT_EXTRA_HEIGHTMAP, new CountExtraChanceDecoratorConfig(2, 0.1f, 1)))

  addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(FeatureRegistry.uplandFlowerFeature,
    FeatureConfig.DEFAULT, Decorator.NOISE_HEIGHTMAP_32, new NoiseHeightmapDecoratorConfig(-0.8D, 15, 4)))

  addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(FeatureRegistry.tallUplandsGrassFeature,
    new GrassFeatureConfig(BlockRegistry.TALL_UPLANDS_GRASS.getDefaultState), Decorator.COUNT_HEIGHTMAP_DOUBLE, new CountDecoratorConfig(2)))

  addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(FeatureRegistry.zephyrOnionFeature,
    FeatureConfig.DEFAULT, Decorator.COUNT_HEIGHTMAP_32, new CountDecoratorConfig(3)))

  addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, Biome.configureFeature(FeatureRegistry.skyLakeFeature,
    new LakeFeatureConfig(Blocks.WATER.getDefaultState), Decorator.WATER_LAKE, new LakeDecoratorConfig(8)))

  addStructureFeature(FeatureRegistry.treehouseFeature, FeatureConfig.DEFAULT)
  addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Biome.configureFeature(FeatureRegistry.treehouseFeature,
    FeatureConfig.DEFAULT, Decorator.CHANCE_PASSTHROUGH, new ChanceDecoratorConfig(100)))

  @Environment(EnvType.CLIENT)
  override def getSkyColor(currentTemperature: Float) = 12632319

  override def getGrassColorAt(pos: BlockPos) = 15382866

  override def getFoliageColorAt(pos: BlockPos) = 15382866

  override def computeTemperature(blockPos_1: BlockPos): Float = getTemperature()
}
