package robosky.uplands.world.feature.megadungeon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;

class MegadungeonPiece extends PoolStructurePiece {
    private MegadungeonPiece(StructureManager mgr, StructurePoolElement elem, BlockPos pos, int groundLevelDelta,
                             BlockRotation rotation, BlockBox bbox) {
        super(MegadungeonFeature.PIECE_TYPE(), mgr, elem, pos, groundLevelDelta, rotation, bbox);
    }

    public static MegadungeonPiece create(StructureManager mgr, StructurePoolElement elem, BlockPos pos, int groundLevelDelta,
                                          BlockRotation rotation, BlockBox bbox) {
        return new MegadungeonPiece(mgr, elem, pos, groundLevelDelta, rotation, bbox);
    }

    public MegadungeonPiece(StructureManager mgr, CompoundTag tag) {
        super(mgr, tag, MegadungeonFeature.PIECE_TYPE());
    }
}
