package robosky.ether.world.feature.trees;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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

    public SkyrootTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> deserialize, boolean sapling) {
        this(deserialize, sapling, BlockRegistry.SKYROOT_LOG().getDefaultState(),
                BlockRegistry.SKYROOT_LEAVES().getDefaultState());
    }

    private SkyrootTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> dezerialize, boolean sapling,
                               BlockState log, BlockState leaves) {
        super(dezerialize, sapling);
        this.height = 4;
        this.log = log;
        this.leaves = leaves;
    }

    public boolean generate(Set<BlockPos> set, ModifiableTestableWorld world, Random rand,
                            BlockPos startPos, MutableIntBoundingBox bbox) {
        int segment1Height = this.getTreeHeight(rand) - 2;
        int segment2Height = this.getTreeHeight(rand) + 4;
        Direction bendDirection = Direction.fromHorizontal(rand.nextInt(4));
        boolean keepGoing = true;
        if (startPos.getY() >= 1 && startPos.getY() + segment1Height + 1 <= 256) {
            for (int y = startPos.getY(); y <= startPos.getY() + 1 + segment1Height + segment2Height; ++y) {
                int radius = 1;
                if (y == startPos.getY()) {
                    radius = 0;
                }

                if (y >= startPos.getY() + 1 + segment1Height - 2) {
                    radius = 2;
                }

                BlockPos.Mutable pos = new BlockPos.Mutable();

                for (int x = startPos.getX() - radius; x <= startPos.getX() + radius && keepGoing; ++x) {
                    for (int z = startPos.getZ() - radius; z <= startPos.getZ() + radius && keepGoing; ++z) {
                        if (y >= 0 && y < 256) {
                            if (!canTreeReplace(world, pos.set(x, y, z))) {
                                keepGoing = false;
                            }
                        } else {
                            keepGoing = false;
                        }
                    }
                }
            }

            if (!keepGoing) {
                return false;
            } else if (isDirtOrGrass(world, startPos.down()) && startPos.getY() < 256 - segment1Height - 1) {
                this.setToDirt(world, startPos.down());
                BlockPos segment2Start = startPos.up(segment1Height - 1).offset(bendDirection);
                for (int leavesY = segment2Start.getY() - 4 + segment2Height; leavesY <= segment2Start.getY() + segment2Height; ++leavesY) {
                    int radius = Math.min(1 - (leavesY - (segment2Start.getY() + segment2Height)) / 2, 2);

                    for (int leavesX = segment2Start.getX() - radius; leavesX <= segment2Start.getX() + radius; ++leavesX) {
                        for (int leavesZ = segment2Start.getZ() - radius; leavesZ <= segment2Start.getZ() + radius; ++leavesZ) {
                            if (Math.abs(leavesX - segment2Start.getX()) != radius ||
                                    Math.abs(leavesZ - segment2Start.getZ()) != radius || rand.nextInt(2) != 0
                                    && leavesY - (segment2Start.getY() + segment2Height) != 0) {
                                BlockPos pos = new BlockPos(leavesX, leavesY, leavesZ);
                                if (isAirOrLeaves(world, pos) || isReplaceablePlant(world, pos)) {
                                    this.setBlockState(set, world, pos, this.leaves, bbox);
                                }
                            }
                        }
                    }
                }

                for (int y = 0; y < segment1Height; ++y) {
                    if (isAirOrLeaves(world, startPos.up(y)) || isReplaceablePlant(world, startPos.up(y))) {
                        this.setBlockState(set, world, startPos.up(y), y == segment1Height - 1 ?
                                BlockRegistry.SKYROOT_WOOD().getDefaultState() : this.log, bbox);
                    }
                }
                for (int y = 0; y < segment2Height; ++y) {
                    if (isAirOrLeaves(world, segment2Start.up(y)) || isReplaceablePlant(world, segment2Start.up(y))) {
                        this.setBlockState(set, world, segment2Start.up(y), y == 0 || y ==
                                segment2Height - 1 ? BlockRegistry.SKYROOT_WOOD().getDefaultState() : this.log, bbox);
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
