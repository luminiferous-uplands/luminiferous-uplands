package robosky.ether.world.feature.trees;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import robosky.ether.block.BlockRegistry;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class SkyrootTreeFeature extends AbstractEtherTree<DefaultFeatureConfig> {
    private final int height;
    private final BlockState log;
    private final BlockState leaves;

    public SkyrootTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1, boolean sapling) {
        this(function_1, sapling, 4, BlockRegistry.SKYROOT_LOG().getDefaultState(),
                BlockRegistry.SKYROOT_LEAVES().getDefaultState());
    }

    private SkyrootTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1, boolean sapling, int int_1,
                               BlockState blockState_1, BlockState blockState_2) {
        super(function_1, sapling);
        this.height = int_1;
        this.log = blockState_1;
        this.leaves = blockState_2;
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

}
