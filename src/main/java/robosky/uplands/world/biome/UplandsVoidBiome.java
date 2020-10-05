//package robosky.uplands.world.biome;
//
//import robosky.uplands.block.BlockRegistry;
//import robosky.uplands.block.UplandsOreBlock;
//import robosky.uplands.world.feature.FeatureRegistry;
//import robosky.uplands.world.feature.UplandsOreFeatureConfig;
//
//import net.minecraft.block.Blocks;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.SpawnGroup;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.gen.GenerationStep;
//import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
//import net.minecraft.world.gen.decorator.Decorator;
//import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
//import net.minecraft.world.gen.feature.FeatureConfig;
//import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
//
//public class UplandsVoidBiome extends Biome {
//
//    protected UplandsVoidBiome() {
//        super(new Builder().configureSurfaceBuilder(BiomeRegistry.UPLANDS_AUTUMN_SURFACE_BUILDER,
//            BiomeRegistry.UPLANDS_GRASS_DIRT_STONE_SURFACE).precipitation(Precipitation.NONE).category(Category.FOREST)
//            .depth(0).scale(0.2F).temperature(0.5F).downfall(0.0F)
//            .waterColor(0x9898BC).waterFogColor(0x9898BC).category(Category.FOREST));
//
//        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, FeatureRegistry.ORE_FEATURE.configure(
//            new UplandsOreFeatureConfig(9, 1, 128, BlockRegistry.UPLANDS_ORES.get(UplandsOreBlock.oreTypeAegisalt).getDefaultState()))
//            .decorate(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(1, 0, 0, 256))));
//        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, FeatureRegistry.ORE_FEATURE.configure(
//            new UplandsOreFeatureConfig(20, 1, 64, BlockRegistry.LODESTONE.getDefaultState()))
//            .decorate(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(4, 0, 0, 256))));
//
//        addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, FeatureRegistry.SKY_LAKE.configure(
//            new SingleStateFeatureConfig(Blocks.WATER.getDefaultState()))
//            .decorate(Decorator.WATER_LAKE.configure(new ChanceDecoratorConfig(8))));
//
//        addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, FeatureRegistry.TREEHOUSE.configure(FeatureConfig.DEFAULT)
//            .decorate(Decorator.CHANCE_PASSTHROUGH.configure(new ChanceDecoratorConfig(100))));
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
//}
