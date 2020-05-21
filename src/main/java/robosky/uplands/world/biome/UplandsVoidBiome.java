package robosky.uplands.world.biome;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.WeightedStateProvider;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import robosky.uplands.block.BlockRegistry;
import robosky.uplands.block.UplandsOreBlock;
import robosky.uplands.world.feature.FeatureRegistry;
import robosky.uplands.world.feature.UplandsOreFeatureConfig;
import robosky.uplands.world.feature.megadungeon.MegadungeonFeature$;
import robosky.uplands.world.gen.UplandsAutumnSurfaceBuilder;

public class UplandsVoidBiome extends Biome {
    private static final SurfaceBuilder<SurfaceConfig> UPLANDS_AUTUMN_SURFACE_BUILDER = new UplandsAutumnSurfaceBuilder();

    private static final SurfaceConfig UPLANDS_GRASS_DIRT_STONE_SURFACE = new SurfaceConfig() {
        @Override
        public BlockState getTopMaterial() {
            return BlockRegistry.UPLANDER_GRASS().getDefaultState();
        }

        @Override
        public BlockState getUnderMaterial() {
            return BlockRegistry.UPLANDER_DIRT().getDefaultState();
        }
    };

    protected UplandsVoidBiome() {
        super(new Settings().configureSurfaceBuilder(UPLANDS_AUTUMN_SURFACE_BUILDER,
                UPLANDS_GRASS_DIRT_STONE_SURFACE).precipitation(Precipitation.NONE).category(Category.FOREST)
                .depth(0).scale(0.2F).temperature(0.5F).downfall(0.0F)
                .waterColor(0x9898BC).waterFogColor(0x9898BC).category(Category.FOREST));

        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, FeatureRegistry.oreFeature().configure(
                new UplandsOreFeatureConfig(9, 1, 128, BlockRegistry.UPLANDS_ORES().get(UplandsOreBlock.OreTypeAegisalt$.MODULE$).get().getDefaultState()))
                .createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(1, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, FeatureRegistry.oreFeature().configure(
                new UplandsOreFeatureConfig(20, 1, 64, BlockRegistry.LODESTONE().getDefaultState()))
                .createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(4, 0, 0, 256))));

        addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, FeatureRegistry.skyLakeFeature().configure(
                new SingleStateFeatureConfig(Blocks.WATER.getDefaultState()))
                .createDecoratedFeature(Decorator.WATER_LAKE.configure(new ChanceDecoratorConfig(8))));

        addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, FeatureRegistry.treehouseFeature().configure(FeatureConfig.DEFAULT)
                .createDecoratedFeature(Decorator.CHANCE_PASSTHROUGH.configure(new ChanceDecoratorConfig(100))));

        addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, MegadungeonFeature$.MODULE$.configure(FeatureConfig.DEFAULT)
                .createDecoratedFeature(Decorator.NOPE.configure(new NopeDecoratorConfig())));
    }

    @Override
    public int getSkyColor() {
        return 12632319;
    }

    @Override
    protected float computeTemperature(BlockPos blockPos) {
        return getTemperature();
    }

    @Override
    public int getGrassColorAt(double d, double e) {
        return 15382866;
    }

    @Override
    public int getFoliageColor() {
        return 15382866;
    }
}
