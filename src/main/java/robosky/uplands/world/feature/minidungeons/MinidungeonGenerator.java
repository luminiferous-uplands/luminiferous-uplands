package robosky.uplands.world.feature.minidungeons;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class MinidungeonGenerator {
    static void addParts(StructureManager mgr, BlockPos pos, BlockRotation rot, List<StructurePiece> pieces,
                         MinidungeonFeatureConfig conf) {
        pieces.add(new Piece(mgr, pos, rot, conf));
    }

    public static class Piece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier template;
        private Identifier loot = null;

        Piece(StructureManager mgr, BlockPos pos, BlockRotation rotation, MinidungeonFeatureConfig conf) {
            super(Registry.STRUCTURE_PIECE.get(conf.getTemplate()), 0);
            this.rotation = rotation;
            this.pos = pos;
            this.template = conf.getTemplate();
            this.loot = conf.getLoot().orElse(null);

            initializeStructureData(mgr);
        }

        public Piece(StructurePieceType tpe, StructureManager mgr, CompoundTag tag) {
            super(tpe, tag);
            this.rotation = BlockRotation.valueOf(tag.getString("Rot"));
            this.template = new Identifier(tag.getString("Template"));
            if(tag.contains("LootTable")) {
                this.loot = new Identifier(tag.getString("LootTable"));
            }
            initializeStructureData(mgr);
        }

        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            tag.putString("Rot", this.rotation.name());
            tag.putString("Template", template.toString());
            if(loot != null) {
                tag.putString("LootTable", loot.toString());
            }
        }

        private void initializeStructureData(StructureManager mgr) {
            Structure structure = mgr.getStructureOrBlank(template);
            StructurePlacementData placementData = (new StructurePlacementData()).setRotation(rotation)
                .setMirror(BlockMirror.NONE).setPosition(new BlockPos(0, 0, 0))
                .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure, this.pos, placementData);
        }

        @Override
        protected void handleMetadata(String dataName, BlockPos pos, ServerWorldAccess world, Random rand, BlockBox boundingBox) {
            if("chest".equals(dataName)) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                BlockEntity blockEntity_1 = world.getBlockEntity(pos.down());
                if(blockEntity_1 instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockEntity_1).setLootTable(loot, rand.nextLong());
                }
            }
        }

        @Override
        public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random rand, BlockBox bbox, ChunkPos chunkPos, BlockPos blockPos) {
            int yHeight = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, this.pos.add(8, 0, 8)).getY();

            this.pos = this.pos.add(0, yHeight - 1, 0);

            if(pos.getY() < 20) return false;

            for(int x = pos.getX() - 3 + 8; x < pos.getX() + 3 + 8; x++) {
                for(int z = pos.getZ() - 3 + 8; z < pos.getZ() + 3 + 8; z++) {
                    if(world.getBlockState(new BlockPos(x, pos.getY() - 1, z)).isAir())
                        return false;
                }
            }

            return super.generate(world, structureAccessor, chunkGenerator, rand, bbox, chunkPos, blockPos);
        }
    }
}
