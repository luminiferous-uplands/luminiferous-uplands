package robosky.uplands.block.bossroom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;

public final class MegadungeonAltarBlock extends AltarBlock {

    private static final VoxelShape SHAPE = VoxelShapes.union(
        Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0),
        Block.createCuboidShape(2.0, 1.0, 2.0, 14.0, 2.0, 14.0),
        Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 4.0, 12.0),
        Block.createCuboidShape(5.0, 4.0, 5.0, 11.0, 14.0, 11.0),
        Block.createCuboidShape(3.0, 14.0, 3.0, 13.0, 15.0, 13.0),
        Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0)
    );

    public MegadungeonAltarBlock() {
        super(FabricBlockSettings.of(Material.STONE)
            .strength(1.5f, 6f)
            .sounds(BlockSoundGroup.STONE)
            .breakByTool(FabricToolTags.PICKAXES, 1)
            .build());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, EntityContext ctx) {
        return SHAPE;
    }
}
