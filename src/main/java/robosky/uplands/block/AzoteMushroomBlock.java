package robosky.uplands.block;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.EntityContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Iterator;
import java.util.Random;

public class AzoteMushroomBlock extends MushroomPlantBlock {

    private static final VoxelShape SHAPE = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);

    public AzoteMushroomBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canPlaceAtSide(BlockState blockState, BlockView blockView, BlockPos blockPos, BlockPlacementEnvironment environment) {
        BlockState blockBelow = blockView.getBlockState(blockPos.down());
        return blockBelow.matches(UplandsBlockTags.PLANTABLE_ON) ||
                blockBelow.matches(TagRegistry.block(new Identifier("luminiferous_uplands:azote_mushroom_spreadable")));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
        return SHAPE;
    }

    @Override
    public boolean isFertilizable(BlockView blockView, BlockPos blockPos, BlockState blockState, boolean bl) {
        return false;
    }

    @Override
    public void scheduledTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        // If it should spread this frame
        if (random.nextInt(25) == 0) {
            // The number of mushrooms that can be in close proximity, before it stops spreading.
            int mushroomThreshold = 5;

            // A box representing what the mushroom counts as "close proximity"

            // Iterate over the box
            for (BlockPos nextBlockPos : BlockPos.iterate(blockPos.add(-4, -1, -4), blockPos.add(4, 1, 4))) {
                // For each block in the box, check if it's a mushroom.
                if (serverWorld.getBlockState(nextBlockPos).getBlock() == this) {
                    // If it is, count down by one, and stop if there are too many.
                    mushroomThreshold -= 1;
                    if (mushroomThreshold <= 0) {
                        return;
                    }
                }
            }

            // Vary the spawn position a little bit
            BlockPos mushroomSpawnLocation = blockPos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

            // If this is a valid position, spawn there
            if (serverWorld.isAir(mushroomSpawnLocation)
                    && serverWorld.getBlockState(mushroomSpawnLocation.down()).matches(UplandsBlockTags.AZOTE_MUSHROOM_SPREADABLE)) {
                serverWorld.setBlockState(mushroomSpawnLocation, blockState, 2);
            }
        }
    }
}
