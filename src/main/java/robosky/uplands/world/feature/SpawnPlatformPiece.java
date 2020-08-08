package robosky.uplands.world.feature;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.IWorld;

import java.util.Random;

public class SpawnPlatformPiece extends SimpleStructurePiece {
    public SpawnPlatformPiece(StructureManager mgr, BlockPos pos) {
        super(FeatureRegistry.SPAWN_PLATFORM, 0);
        this.pos = pos;
        initializeStructureData(mgr);
    }

    public SpawnPlatformPiece(StructureManager mgr, CompoundTag compoundTag_1) {
        super(FeatureRegistry.SPAWN_PLATFORM, compoundTag_1);
        initializeStructureData(mgr);
    }

    private void initializeStructureData(StructureManager structureManager_1) {
        Structure structure_1 = structureManager_1.getStructureOrBlank(new Identifier("luminiferous_uplands", "spawn_platform"));
        StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setRotation(BlockRotation.NONE)
                .setMirrored(BlockMirror.NONE).setPosition(new BlockPos(0, 0, 0))
                .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        this.setStructureData(structure_1, this.pos, structurePlacementData_1);
    }

    @Override
    protected void handleMetadata(String var1, BlockPos var2, IWorld var3, Random var4, BlockBox var5) {

    }
}
