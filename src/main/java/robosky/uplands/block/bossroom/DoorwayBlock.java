package robosky.uplands.block.bossroom;

import robosky.uplands.iface.BossDoorwayContext;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DoorwayBlock extends Block implements BlockEntityProvider {

    public static final Property<DoorwayState> STATE = EnumProperty.of("state", DoorwayState.class);

    private static final VoxelShape SHAPE = Block.createCuboidShape(4.0f, 4.0f, 4.0f, 12.0f, 12.0f, 12.0f);

    public DoorwayBlock(Block.Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DoorwayBlock.STATE);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new DoorwayBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        if (state.get(DoorwayBlock.STATE) == DoorwayState.OPEN) {
            if (ctx == ShapeContext.absent() || ((BossDoorwayContext) ctx).uplands_shouldSeeDoorwayOutlines()) {
                return DoorwayBlock.SHAPE;
            } else {
                return VoxelShapes.empty();
            }
        } else {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof DoorwayBlockEntity) {
                return ((DoorwayBlockEntity) be).mimicState().getOutlineShape(world, pos, ctx);
            } else {
                return VoxelShapes.fullCube();
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        if (state.get(DoorwayBlock.STATE) == DoorwayState.OPEN) {
            return VoxelShapes.empty();
        } else {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof DoorwayBlockEntity) {
                return ((DoorwayBlockEntity) be).mimicState().getCollisionShape(world, pos, ctx);
            } else {
                return VoxelShapes.fullCube();
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult ctx) {
        if (state.get(DoorwayBlock.STATE) != DoorwayState.BLOCKED) {
            ItemStack heldStack = player.getStackInHand(hand);
            BlockState block = Block.getBlockFromItem(heldStack.getItem()).getPlacementState(
                new ItemPlacementContext(new ItemUsageContext(player, hand, ctx))
            );
            boolean validMimic = (heldStack.isEmpty() && hand == Hand.MAIN_HAND) ||
                (block != null && block.getRenderType() == BlockRenderType.MODEL);
            if (validMimic) {
                if (!world.isClient()) {
                    BlockEntity be = world.getBlockEntity(pos);
                    if (be instanceof DoorwayBlockEntity) {
                        DoorwayBlockEntity doorway = (DoorwayBlockEntity) be;
                        doorway.setMimicState(block);
                        doorway.markDirty();
                        world.updateListeners(pos, state, state, 3);
                    }
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
