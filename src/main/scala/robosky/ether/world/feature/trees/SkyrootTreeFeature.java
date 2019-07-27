package robosky.ether.world.feature.trees;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import robosky.ether.block.BlockRegistry;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class SkyrootTreeFeature extends AbstractEtherTree<DefaultFeatureConfig> {
    private final int height;
    private final boolean hasVinesAndCocoa;
    private final BlockState log;
    private final BlockState leaves;

    public SkyrootTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1, boolean sapling) {
        this(function_1, sapling, 4, BlockRegistry.SKYROOT_LOG().getDefaultState(),
                BlockRegistry.SKYROOT_LEAVES().getDefaultState(), false);
    }

    private SkyrootTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1, boolean sapling, int int_1,
                               BlockState blockState_1, BlockState blockState_2, boolean boolean_2) {
        super(function_1, false);
        this.height = int_1;
        this.log = blockState_1;
        this.leaves = blockState_2;
        this.hasVinesAndCocoa = boolean_2;
    }

    public boolean generate(Set<BlockPos> set_1, ModifiableTestableWorld modifiableTestableWorld_1, Random random_1,
                            BlockPos blockPos_1, MutableIntBoundingBox mutableIntBoundingBox_1) {
        int int_1 = this.getTreeHeight(random_1);
        boolean boolean_1 = true;
        if (blockPos_1.getY() >= 1 && blockPos_1.getY() + int_1 + 1 <= 256) {
            int int_9;
            int int_18;
            for (int int_2 = blockPos_1.getY(); int_2 <= blockPos_1.getY() + 1 + int_1; ++int_2) {
                int int_3 = 1;
                if (int_2 == blockPos_1.getY()) {
                    int_3 = 0;
                }

                if (int_2 >= blockPos_1.getY() + 1 + int_1 - 2) {
                    int_3 = 2;
                }

                BlockPos.Mutable blockPos$Mutable_1 = new BlockPos.Mutable();

                for (int_9 = blockPos_1.getX() - int_3; int_9 <= blockPos_1.getX() + int_3 && boolean_1; ++int_9) {
                    for (int_18 = blockPos_1.getZ() - int_3; int_18 <= blockPos_1.getZ() + int_3 && boolean_1; ++int_18) {
                        if (int_2 >= 0 && int_2 < 256) {
                            if (!canTreeReplace(modifiableTestableWorld_1, blockPos$Mutable_1.set(int_9, int_2, int_18))) {
                                boolean_1 = false;
                            }
                        } else {
                            boolean_1 = false;
                        }
                    }
                }
            }

            if (!boolean_1) {
                return false;
            } else if (isDirtOrGrass(modifiableTestableWorld_1, blockPos_1.down()) && blockPos_1.getY() < 256 - int_1 - 1) {
                this.setToDirt(modifiableTestableWorld_1, blockPos_1.down());
                int int_19;
                int int_20;
                BlockPos blockPos_4;
                int int_21;
                for (int_21 = blockPos_1.getY() - 3 + int_1; int_21 <= blockPos_1.getY() + int_1; ++int_21) {
                    int_9 = int_21 - (blockPos_1.getY() + int_1);
                    int_18 = 1 - int_9 / 2;

                    for (int int_11 = blockPos_1.getX() - int_18; int_11 <= blockPos_1.getX() + int_18; ++int_11) {
                        int_19 = int_11 - blockPos_1.getX();

                        for (int_20 = blockPos_1.getZ() - int_18; int_20 <= blockPos_1.getZ() + int_18; ++int_20) {
                            int int_14 = int_20 - blockPos_1.getZ();
                            if (Math.abs(int_19) != int_18 || Math.abs(int_14) != int_18 || random_1.nextInt(2) != 0 && int_9 != 0) {
                                blockPos_4 = new BlockPos(int_11, int_21, int_20);
                                if (isAirOrLeaves(modifiableTestableWorld_1, blockPos_4) || isReplaceablePlant(modifiableTestableWorld_1, blockPos_4)) {
                                    this.setBlockState(set_1, modifiableTestableWorld_1, blockPos_4, this.leaves, mutableIntBoundingBox_1);
                                }
                            }
                        }
                    }
                }

                for (int_21 = 0; int_21 < int_1; ++int_21) {
                    if (isAirOrLeaves(modifiableTestableWorld_1, blockPos_1.up(int_21)) || isReplaceablePlant(modifiableTestableWorld_1, blockPos_1.up(int_21))) {
                        this.setBlockState(set_1, modifiableTestableWorld_1, blockPos_1.up(int_21), this.log, mutableIntBoundingBox_1);
                        if (this.hasVinesAndCocoa && int_21 > 0) {
                            if (random_1.nextInt(3) > 0 && isAir(modifiableTestableWorld_1, blockPos_1.add(-1, int_21, 0))) {
                                this.makeVine(modifiableTestableWorld_1, blockPos_1.add(-1, int_21, 0), VineBlock.EAST);
                            }

                            if (random_1.nextInt(3) > 0 && isAir(modifiableTestableWorld_1, blockPos_1.add(1, int_21, 0))) {
                                this.makeVine(modifiableTestableWorld_1, blockPos_1.add(1, int_21, 0), VineBlock.WEST);
                            }

                            if (random_1.nextInt(3) > 0 && isAir(modifiableTestableWorld_1, blockPos_1.add(0, int_21, -1))) {
                                this.makeVine(modifiableTestableWorld_1, blockPos_1.add(0, int_21, -1), VineBlock.SOUTH);
                            }

                            if (random_1.nextInt(3) > 0 && isAir(modifiableTestableWorld_1, blockPos_1.add(0, int_21, 1))) {
                                this.makeVine(modifiableTestableWorld_1, blockPos_1.add(0, int_21, 1), VineBlock.NORTH);
                            }
                        }
                    }
                }

                if (this.hasVinesAndCocoa) {
                    for (int_21 = blockPos_1.getY() - 3 + int_1; int_21 <= blockPos_1.getY() + int_1; ++int_21) {
                        int_9 = int_21 - (blockPos_1.getY() + int_1);
                        int_18 = 2 - int_9 / 2;
                        BlockPos.Mutable blockPos$Mutable_2 = new BlockPos.Mutable();

                        for (int_19 = blockPos_1.getX() - int_18; int_19 <= blockPos_1.getX() + int_18; ++int_19) {
                            for (int_20 = blockPos_1.getZ() - int_18; int_20 <= blockPos_1.getZ() + int_18; ++int_20) {
                                blockPos$Mutable_2.set(int_19, int_21, int_20);
                                if (isLeaves(modifiableTestableWorld_1, blockPos$Mutable_2)) {
                                    BlockPos blockPos_3 = blockPos$Mutable_2.west();
                                    blockPos_4 = blockPos$Mutable_2.east();
                                    BlockPos blockPos_5 = blockPos$Mutable_2.north();
                                    BlockPos blockPos_6 = blockPos$Mutable_2.south();
                                    if (random_1.nextInt(4) == 0 && isAir(modifiableTestableWorld_1, blockPos_3)) {
                                        this.makeVineColumn(modifiableTestableWorld_1, blockPos_3, VineBlock.EAST);
                                    }

                                    if (random_1.nextInt(4) == 0 && isAir(modifiableTestableWorld_1, blockPos_4)) {
                                        this.makeVineColumn(modifiableTestableWorld_1, blockPos_4, VineBlock.WEST);
                                    }

                                    if (random_1.nextInt(4) == 0 && isAir(modifiableTestableWorld_1, blockPos_5)) {
                                        this.makeVineColumn(modifiableTestableWorld_1, blockPos_5, VineBlock.SOUTH);
                                    }

                                    if (random_1.nextInt(4) == 0 && isAir(modifiableTestableWorld_1, blockPos_6)) {
                                        this.makeVineColumn(modifiableTestableWorld_1, blockPos_6, VineBlock.NORTH);
                                    }
                                }
                            }
                        }
                    }

                    if (random_1.nextInt(5) == 0 && int_1 > 5) {
                        for (int_21 = 0; int_21 < 2; ++int_21) {
                            for (Direction direction_1 : Direction.Type.HORIZONTAL) {
                                if (random_1.nextInt(4 - int_21) == 0) {
                                    Direction direction_2 = direction_1.getOpposite();
                                    this.makeCocoa(modifiableTestableWorld_1, random_1.nextInt(3), blockPos_1.add(direction_2.getOffsetX(), int_1 - 5 + int_21, direction_2.getOffsetZ()), direction_1);
                                }
                            }
                        }
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getTreeHeight(Random random_1) {
        return this.height + random_1.nextInt(3);
    }

    private void makeCocoa(ModifiableWorld modifiableWorld_1, int int_1, BlockPos blockPos_1, Direction direction_1) {
        this.setBlockState(modifiableWorld_1, blockPos_1, (BlockState) ((BlockState) Blocks.COCOA.getDefaultState().with(CocoaBlock.AGE, int_1)).with(CocoaBlock.FACING, direction_1));
    }

    private void makeVine(ModifiableWorld modifiableWorld_1, BlockPos blockPos_1, BooleanProperty booleanProperty_1) {
        this.setBlockState(modifiableWorld_1, blockPos_1, (BlockState) Blocks.VINE.getDefaultState().with(booleanProperty_1, true));
    }

    private void makeVineColumn(ModifiableTestableWorld modifiableTestableWorld_1, BlockPos blockPos_1, BooleanProperty booleanProperty_1) {
        this.makeVine(modifiableTestableWorld_1, blockPos_1, booleanProperty_1);
        int int_1 = 4;

        for (blockPos_1 = blockPos_1.down(); isAir(modifiableTestableWorld_1, blockPos_1) && int_1 > 0; --int_1) {
            this.makeVine(modifiableTestableWorld_1, blockPos_1, booleanProperty_1);
            blockPos_1 = blockPos_1.down();
        }

    }
}
