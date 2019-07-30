package robosky.ether.world.feature.minidungeons;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.IWorld;
import robosky.ether.world.feature.FeatureRegistry;

import java.util.List;
import java.util.Random;

public class TreehouseGenerator {
    public static final Identifier name = new Identifier("minidungeons/treehouse");

    static void addParts(StructureManager mgr, BlockPos pos, BlockRotation rot, List<StructurePiece> pieces) {
        pieces.add(new Piece(mgr, pos, rot));
    }

    public static class Piece extends SimpleStructurePiece {
        private BlockRotation rotation;

        Piece(StructureManager mgr, BlockPos pos, BlockRotation rotation) {
            super(FeatureRegistry.treehousePiece(), 0);
            this.rotation = rotation;
            this.pos = pos;
            initializeStructureData(mgr);
        }

        public Piece(StructureManager mgr, CompoundTag compoundTag_1) {
            super(FeatureRegistry.treehousePiece(), compoundTag_1);
            this.rotation = BlockRotation.valueOf(compoundTag_1.getString("Rot"));
            initializeStructureData(mgr);
        }

        protected void toNbt(CompoundTag compoundTag_1) {
            super.toNbt(compoundTag_1);
            compoundTag_1.putString("Rot", this.rotation.name());
        }

        private void initializeStructureData(StructureManager structureManager_1) {
            Structure structure_1 = structureManager_1.getStructureOrBlank(name);
            StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setRotation(rotation)
                    .setMirrored(BlockMirror.NONE).setPosition(new BlockPos(0, 0, 0))
                    .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure_1, this.pos, structurePlacementData_1);
        }

        @Override
        protected void handleMetadata(String var1, BlockPos var2, IWorld var3, Random var4, MutableIntBoundingBox var5) {

        }
    }

}
