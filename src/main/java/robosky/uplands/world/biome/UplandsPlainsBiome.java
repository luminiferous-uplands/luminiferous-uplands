//package robosky.uplands.world.biome;
//
//import robosky.uplands.block.BlockRegistry;
//import robosky.uplands.world.feature.FeatureRegistry;
//import robosky.uplands.world.feature.megadungeon.MegadungeonFeature;
//
//import net.minecraft.block.Blocks;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.SpawnGroup;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.gen.GenerationStep;
//import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
//import net.minecraft.world.gen.decorator.CountDecoratorConfig;
//import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
//import net.minecraft.world.gen.decorator.Decorator;
//import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
//import net.minecraft.world.gen.feature.FeatureConfig;
//import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
//import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
//import net.minecraft.world.gen.placer.SimpleBlockPlacer;
//import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
//
//public class UplandsPlainsBiome extends Biome implements UplandsBiome {
//
//    protected UplandsPlainsBiome() {
//        super(new Builder().configureSurfaceBuilder(BiomeRegistry.UPLANDS_AUTUMN_SURFACE_BUILDER,
//            BiomeRegistry.UPLANDS_GRASS_DIRT_STONE_SURFACE).precipitation(Precipitation.NONE).category(Category.FOREST)
//            .depth(0.1f).scale(0.2F).temperature(0.5F).downfall(0.0F)
//            .waterColor(0x9898BC).waterFogColor(0x9898BC).category(Category.FOREST));
//
//        addFeature(GenerationStep.Feature.VEGETAL_DECORATION, FeatureRegistry.SKYROOT_TREE.configure(FeatureConfig.DEFAULT)
//            .decorate(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraDecoratorConfig(0, 0.1f, 1))));
//
//        addFeature(GenerationStep.Feature.VEGETAL_DECORATION, FeatureRegistry.WILD_WATER_CHESTNUTS.configure(FeatureConfig.DEFAULT)
//            .decorate(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraDecoratorConfig(4, 0.1f, 1))));
//
//        addFeature(GenerationStep.Feature.VEGETAL_DECORATION, FeatureRegistry.TALL_UPLANDS_GRASS.configure(
//            new RandomPatchFeatureConfig.Builder(
//                new WeightedBlockStateProvider()
//                    .addState(BlockRegistry.TALL_UPLANDS_GRASS.getDefaultState(), 7)
//                    .addState(BlockRegistry.CLOUD_DAISIES.getDefaultState(), 1),
//                new SimpleBlockPlacer()
//            ).tries(32).build())
//            .decorate(Decorator.COUNT_HEIGHTMAP_DOUBLE.configure(new CountDecoratorConfig(8))));
//
//        addFeature(GenerationStep.Feature.VEGETAL_DECORATION, FeatureRegistry.WILD_ZEPHYR_ONION.configure(FeatureConfig.DEFAULT)
//            .decorate(Decorator.COUNT_HEIGHTMAP_32.configure(new CountDecoratorConfig(5))));
//
//        addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, FeatureRegistry.SKY_LAKE.configure(
//            new SingleStateFeatureConfig(Blocks.WATER.getDefaultState()))
//            .decorate(Decorator.WATER_LAKE.configure(new ChanceDecoratorConfig(6))));
//
//        addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, FeatureRegistry.TREEHOUSE.configure(FeatureConfig.DEFAULT)
//            .decorate(Decorator.CHANCE_PASSTHROUGH.configure(new ChanceDecoratorConfig(100))));
//
//        addStructureFeature(MegadungeonFeature.INSTANCE.configure(FeatureConfig.DEFAULT));
//        addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, MegadungeonFeature.INSTANCE.configure(FeatureConfig.DEFAULT)
//            .decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));
//
//        addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ENDERMAN, 10, 1, 4));
//    }
//
//    @Override
//    public int getSkyColor() {
//        return 12632319;
//    }
//
//    @Override
//    protected float computeTemperature(BlockPos blockPos) {
//        return getTemperature();
//    }
//
//    @Override
//    public int getGrassColorAt(double d, double e) {
//        return 15382866;
//    }
//
//    @Override
//    public int getFoliageColor() {
//        return 15382866;
//    }
//
//    @Override
//    public double getIslandSize() {
//        return 20;
//    }
//}
