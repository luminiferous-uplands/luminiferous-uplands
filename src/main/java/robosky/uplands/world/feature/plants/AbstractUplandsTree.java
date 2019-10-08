package robosky.uplands.world.feature.plants;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.Structure;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.world.IWorld;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import robosky.uplands.block.BlockRegistry;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public abstract class AbstractUplandsTree<T extends FeatureConfig> extends Feature<T> {
    AbstractUplandsTree(Function<Dynamic<?>, ? extends T> function_1, boolean boolean_1) {
        super(function_1, boolean_1);
    }

    static boolean canTreeReplace(TestableWorld testableWorld_1, BlockPos blockPos_1) {
        return testableWorld_1.testBlockState(blockPos_1, (blockState_1) -> {
            Block block_1 = blockState_1.getBlock();
            return blockState_1.isAir() || blockState_1.matches(BlockTags.LEAVES) || block_1 == BlockRegistry.UPLANDER_GRASS()
                    || block_1 == BlockRegistry.UPLANDER_DIRT() || block_1.matches(BlockTags.LOGS) ||
                    block_1.matches(BlockTags.SAPLINGS) || block_1 == Blocks.VINE;
        });
    }

    private static boolean isNaturalDirt(TestableWorld testableWorld_1, BlockPos blockPos_1) {
        return testableWorld_1.testBlockState(blockPos_1, (blockState_1) -> Block.isNaturalDirt(blockState_1.getBlock()));
    }

    static boolean isAirOrLeaves(TestableWorld testableWorld_1, BlockPos blockPos_1) {
        return testableWorld_1.testBlockState(blockPos_1, (blockState_1) -> blockState_1.isAir() || blockState_1.matches(BlockTags.LEAVES));
    }

    static boolean isDirtOrGrass(TestableWorld testableWorld_1, BlockPos blockPos_1) {
        return testableWorld_1.testBlockState(blockPos_1, (blockState_1) -> {
            Block block_1 = blockState_1.getBlock();
            return block_1 == BlockRegistry.UPLANDER_DIRT() || block_1 == BlockRegistry.UPLANDER_GRASS();
        });
    }

    static boolean isReplaceablePlant(TestableWorld testableWorld_1, BlockPos blockPos_1) {
        return testableWorld_1.testBlockState(blockPos_1, (blockState_1) -> {
            Material material_1 = blockState_1.getMaterial();
            return material_1 == Material.REPLACEABLE_PLANT;
        });
    }

    void setToDirt(ModifiableTestableWorld modifiableTestableWorld_1, BlockPos blockPos_1) {
        if (!isNaturalDirt(modifiableTestableWorld_1, blockPos_1)) {
            this.setBlockState(modifiableTestableWorld_1, blockPos_1, BlockRegistry.UPLANDER_DIRT().getDefaultState());
        }

    }

    protected void setBlockState(ModifiableWorld modifiableWorld_1, BlockPos blockPos_1, BlockState blockState_1) {
        this.setBlockStateWithoutUpdatingNeighbors(modifiableWorld_1, blockPos_1, blockState_1);
    }

    final void setBlockState(Set<BlockPos> set_1, ModifiableWorld modifiableWorld_1, BlockPos blockPos_1, BlockState blockState_1, MutableIntBoundingBox mutableIntBoundingBox_1) {
        this.setBlockStateWithoutUpdatingNeighbors(modifiableWorld_1, blockPos_1, blockState_1);
        mutableIntBoundingBox_1.setFrom(new MutableIntBoundingBox(blockPos_1, blockPos_1));
        if (BlockTags.LOGS.contains(blockState_1.getBlock())) {
            set_1.add(blockPos_1.toImmutable());
        }

    }

    private void setBlockStateWithoutUpdatingNeighbors(ModifiableWorld modifiableWorld_1, BlockPos blockPos_1, BlockState blockState_1) {
        if (this.emitNeighborBlockUpdates) {
            modifiableWorld_1.setBlockState(blockPos_1, blockState_1, 19);
        } else {
            modifiableWorld_1.setBlockState(blockPos_1, blockState_1, 18);
        }

    }

    public final boolean generate(IWorld iWorld_1, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator_1, Random random_1, BlockPos blockPos_1, T featureConfig_1) {
        Set<BlockPos> set_1 = Sets.newHashSet();
        MutableIntBoundingBox mutableIntBoundingBox_1 = MutableIntBoundingBox.empty();
        boolean boolean_1 = this.generate(set_1, iWorld_1, random_1, blockPos_1, mutableIntBoundingBox_1);
        if (mutableIntBoundingBox_1.minX > mutableIntBoundingBox_1.maxX) {
            return false;
        } else {
            List<Set<BlockPos>> list_1 = Lists.newArrayList();

            for (int int_2 = 0; int_2 < 6; ++int_2) {
                list_1.add(Sets.newHashSet());
            }

            VoxelSet voxelSet_1 = new BitSetVoxelSet(mutableIntBoundingBox_1.getBlockCountX(), mutableIntBoundingBox_1.getBlockCountY(), mutableIntBoundingBox_1.getBlockCountZ());
            BlockPos.PooledMutable blockPos$PooledMutable_1 = BlockPos.PooledMutable.get();
            Throwable var13 = null;

            try {
                if (boolean_1 && !set_1.isEmpty()) {

                    for (BlockPos blockPos_2 : Lists.newArrayList(set_1)) {
                        if (mutableIntBoundingBox_1.contains(blockPos_2)) {
                            voxelSet_1.set(blockPos_2.getX() - mutableIntBoundingBox_1.minX, blockPos_2.getY() - mutableIntBoundingBox_1.minY, blockPos_2.getZ() - mutableIntBoundingBox_1.minZ, true, true);
                        }

                        for (Direction direction_1 : Direction.values()) {
                            blockPos$PooledMutable_1.method_10114(blockPos_2).method_10118(direction_1);
                            if (!set_1.contains(blockPos$PooledMutable_1)) {
                                BlockState blockState_1 = iWorld_1.getBlockState(blockPos$PooledMutable_1);
                                if (blockState_1.contains(Properties.DISTANCE_1_7)) {
                                    list_1.get(0).add(blockPos$PooledMutable_1.toImmutable());
                                    this.setBlockStateWithoutUpdatingNeighbors(iWorld_1, blockPos$PooledMutable_1, blockState_1.with(Properties.DISTANCE_1_7, 1));
                                    if (mutableIntBoundingBox_1.contains(blockPos$PooledMutable_1)) {
                                        voxelSet_1.set(blockPos$PooledMutable_1.getX() - mutableIntBoundingBox_1.minX, blockPos$PooledMutable_1.getY() - mutableIntBoundingBox_1.minY, blockPos$PooledMutable_1.getZ() - mutableIntBoundingBox_1.minZ, true, true);
                                    }
                                }
                            }
                        }
                    }
                }

                for (int int_3 = 1; int_3 < 6; ++int_3) {
                    Set<BlockPos> set_2 = list_1.get(int_3 - 1);
                    Set<BlockPos> set_3 = list_1.get(int_3);

                    for (BlockPos blockPos_3 : set_2) {
                        if (mutableIntBoundingBox_1.contains(blockPos_3)) {
                            voxelSet_1.set(blockPos_3.getX() - mutableIntBoundingBox_1.minX, blockPos_3.getY() - mutableIntBoundingBox_1.minY, blockPos_3.getZ() - mutableIntBoundingBox_1.minZ, true, true);
                        }

                        for (Direction direction_2 : Direction.values()) {
                            blockPos$PooledMutable_1.method_10114(blockPos_3).method_10118(direction_2);
                            if (!set_2.contains(blockPos$PooledMutable_1) && !set_3.contains(blockPos$PooledMutable_1)) {
                                BlockState blockState_2 = iWorld_1.getBlockState(blockPos$PooledMutable_1);
                                if (blockState_2.contains(Properties.DISTANCE_1_7)) {
                                    int int_4 = blockState_2.get(Properties.DISTANCE_1_7);
                                    if (int_4 > int_3 + 1) {
                                        BlockState blockState_3 = blockState_2.with(Properties.DISTANCE_1_7, int_3 + 1);
                                        this.setBlockStateWithoutUpdatingNeighbors(iWorld_1, blockPos$PooledMutable_1, blockState_3);
                                        if (mutableIntBoundingBox_1.contains(blockPos$PooledMutable_1)) {
                                            voxelSet_1.set(blockPos$PooledMutable_1.getX() - mutableIntBoundingBox_1.minX, blockPos$PooledMutable_1.getY() - mutableIntBoundingBox_1.minY, blockPos$PooledMutable_1.getZ() - mutableIntBoundingBox_1.minZ, true, true);
                                        }

                                        set_3.add(blockPos$PooledMutable_1.toImmutable());
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Throwable var33) {
                var13 = var33;
                throw var33;
            } finally {
                if (blockPos$PooledMutable_1 != null) {
                    if (var13 != null) {
                        try {
                            blockPos$PooledMutable_1.close();
                        } catch (Throwable var32) {
                            var13.addSuppressed(var32);
                        }
                    } else {
                        blockPos$PooledMutable_1.close();
                    }
                }

            }

            Structure.method_20532(iWorld_1, 3, voxelSet_1, mutableIntBoundingBox_1.minX, mutableIntBoundingBox_1.minY, mutableIntBoundingBox_1.minZ);
            return boolean_1;
        }
    }

    protected abstract boolean generate(Set<BlockPos> var1, ModifiableTestableWorld var2, Random var3, BlockPos var4, MutableIntBoundingBox var5);
}
