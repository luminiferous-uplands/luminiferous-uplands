package robosky.uplands.block.bossroom;

import robosky.uplands.item.UplandsItemTags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.block.FabricBlockSettings;

/**
 * Block for the boss controller. The important bits are on the block entity.
 */
public class ControlBlock extends Block implements BlockEntityProvider {

    public static final Property<ControlAdjustment> ADJUST = EnumProperty.of("adjust", ControlAdjustment.class);

    public ControlBlock() {
        super(FabricBlockSettings.of(Material.STONE)
            .sounds(BlockSoundGroup.STONE)
            .strength(-1.0f, 3600000.0f).build());
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ControlBlockEntity();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ControlBlock.ADJUST);
    }

    private static final Direction[][] adjustmentMapping = {
        {Direction.WEST, Direction.NORTH, Direction.EAST, Direction.SOUTH}, // DOWN/UP
        {Direction.WEST, Direction.DOWN, Direction.EAST, Direction.UP}, // NORTH/SOUTH
        {Direction.NORTH, Direction.DOWN, Direction.SOUTH, Direction.UP} // WEST/EAST
    };

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult ctx) {
        boolean shouldEdit = player.isCreativeLevelTwoOp() &&
            player.inventory.contains(UplandsItemTags.BOSSROOM_TECHNICAL);
        BlockEntity be = world.getBlockEntity(pos);
        if (shouldEdit && be instanceof ControlBlockEntity) {
            ControlBlockEntity ctrl = (ControlBlockEntity) be;
            Item heldItem = player.getMainHandStack().getItem();
            if (heldItem instanceof SpawnEggItem) {
                // set boss type
                ctrl.setBossType(((SpawnEggItem) heldItem).getEntityType(null));
            } else {
                // adjust boss room bounds
                Direction hitSide = ctx.getSide();
                Vec3d hitPos = ctx.getPos();
                double u;
                if (hitSide == Direction.WEST || hitSide == Direction.EAST) {
                    u = Math.abs(hitPos.getZ() - pos.getZ()) - 0.5;
                } else {
                    u = Math.abs(hitPos.getX() - pos.getX()) - 0.5;
                }
                double v;
                if (hitSide == Direction.DOWN || hitSide == Direction.UP) {
                    v = Math.abs(hitPos.getZ() - pos.getZ()) - 0.5;
                } else {
                    v = Math.abs(hitPos.getY() - pos.getY()) - 0.5;
                }
                double rSq = u * u + v * v;
                // u and v range from [-0.5, 0.5], this is the inner third
                // of that range, i.e. < 1/6 ^ 2
                final double centerThird = 0.17 * 0.17;
                if (rSq < centerThird) {
                    world.setBlockState(pos, state.cycle(ControlBlock.ADJUST));
                } else {
                    double theta = Math.atan2(v, u) / Math.PI;
                    Direction dir = adjustmentMapping[hitSide.getId() / 2][((int) ((theta + 1.25) * 2)) % 4];
                    ControlAdjustment adj = state.get(ControlBlock.ADJUST);
                    int blocks = adj == ControlAdjustment.IN ? -1 : 1;
                    ctrl.adjustBounds(dir, blocks);
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
