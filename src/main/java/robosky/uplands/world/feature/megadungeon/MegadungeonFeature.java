//package robosky.uplands.world.feature.megadungeon;
//
//import java.util.Random;
//
//import robosky.uplands.UplandsMod;
//import robosky.uplands.world.feature.FeatureRegistry;
//
//import net.minecraft.structure.StructureManager;
//import net.minecraft.structure.StructurePieceType;
//import net.minecraft.structure.StructureStart;
//import net.minecraft.util.math.BlockBox;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.ChunkPos;
//import net.minecraft.util.registry.Registry;
//import net.minecraft.world.Heightmap;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.biome.source.BiomeAccess;
//import net.minecraft.world.gen.chunk.ChunkGenerator;
//import net.minecraft.world.gen.feature.AbstractTempleFeature;
//import net.minecraft.world.gen.feature.DefaultFeatureConfig;
//import net.minecraft.world.gen.feature.Feature;
//import net.minecraft.world.gen.feature.StructureFeature;
//
//public final class MegadungeonFeature extends AbstractTempleFeature<DefaultFeatureConfig> {
//
//    public static final MegadungeonFeature INSTANCE = new MegadungeonFeature();
//
//    private MegadungeonFeature() {
//        super(DefaultFeatureConfig::deserialize);
//    }
//
//    public static final StructurePieceType PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, UplandsMod.id("megadungeon_piece"), MegadungeonPiece::new);
//
//    public static void register() {
//        FeatureRegistry.register("megadungeon", INSTANCE);
//        Registry.register(Registry.STRUCTURE_FEATURE, UplandsMod.id("megadungeon"), INSTANCE);
//        Feature.STRUCTURES.put(INSTANCE.getName(), INSTANCE);
//    }
//
//    @Override
//    public int getSeedModifier() {
//        return 165745296;
//    }
//
//    @Override
//    public StructureStartFactory getStructureStartFactory() {
//        return Start::new;
//    }
//
//    @Override
//    public String getName() {
//        return "uplands_megadungeon";
//    }
//
//    @Override
//    public int getRadius() {
//        return 8;
//    }
//
//    @Override
//    public boolean shouldStartAt(BiomeAccess access, ChunkGenerator<?> generator, Random rand, int x, int z, Biome biome) {
//        ChunkPos cpos = this.getStart(generator, rand, x, z, 0, 0);
//        BlockPos center = cpos.getStartPos();
//        if (generator.getHeight(center.getX(), center.getZ(), Heightmap.Type.WORLD_SURFACE_WG) < 30) {
//            return false;
//        } else {
//            return super.shouldStartAt(access, generator, rand, x, z, biome);
//        }
//    }
//
//    private static final class Start extends StructureStart {
//
//        Start(StructureFeature<?> feature, int x, int z, BlockBox bbox, int refs, long seed) {
//            super(feature, x, z, bbox, refs, seed);
//        }
//
//        @Override
//        public void init(ChunkGenerator<?> generator, StructureManager manager, int x, int z, Biome biome) {
//            BlockPos blockPos_1 = new BlockPos(x * 16, 90, z * 16);
//            MegadungeonGenerator.addPieces(generator, manager, blockPos_1, this.children, this.random);
//            this.setBoundingBoxFromChildren();
//        }
//    }
//}
