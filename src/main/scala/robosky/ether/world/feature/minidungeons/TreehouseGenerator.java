package robosky.ether.world.feature.minidungeons;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import robosky.ether.world.feature.FeatureRegistry;

import java.util.List;
import java.util.Random;

public class TreehouseGenerator {
    public static final Identifier name = new Identifier("luminiferous_uplands", "minidungeons/treehouse");

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
                    .addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR);
            this.setStructureData(structure_1, this.pos, structurePlacementData_1);
        }

        @Override
        protected void handleMetadata(String var1, BlockPos var2, IWorld var3, Random var4, MutableIntBoundingBox var5) {

        }

        @Override
        public boolean generate(IWorld iWorld_1, Random random_1, MutableIntBoundingBox mutableIntBoundingBox_1, ChunkPos chunkPos_1) {
            int yHeight = iWorld_1.getTop(Heightmap.Type.WORLD_SURFACE_WG, this.pos.getX() + 8, this.pos.getZ() + 8);
            this.pos = this.pos.add(0, yHeight - 1, 0);
            if (pos.getY() < 20) return false;
            for (int x = pos.getX() - 3; x < pos.getX() + 3; x++) {
                for (int z = pos.getZ() - 3; x < pos.getZ() + 3; x++) {
                    if (iWorld_1.getBlockState(new BlockPos(x, pos.getY() - 1, z)).isAir())
                        return false;
                }
            }
            return super.generate(iWorld_1, random_1, mutableIntBoundingBox_1, chunkPos_1);
        }
    }

}
