package robosky.ether.world.feature.minidungeons;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.List;
import java.util.Random;

public class TreehouseGenerator {
    public static final Identifier name = new Identifier("minidungeons/treehouse");

    public static void addParts(StructureManager structureManager_1, BlockPos blockPos_1, Rotation rotation_1,
                                List<StructurePiece> list_1, Random random_1, DefaultFeatureConfig featureConfig) {
        list_1.add(new TreehouseFeature());

    }
}
