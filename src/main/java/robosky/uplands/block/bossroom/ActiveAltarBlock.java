package robosky.uplands.block.bossroom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import robosky.uplands.block.unbreakable.Unbreakable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ActiveAltarBlock extends AltarBlock implements Unbreakable {

    private static final Logger logger = LogManager.getLogger();

    private final Block base;

    public ActiveAltarBlock(Block base) {
        super(Block.Settings.copy(base).strength(-1.0f, 3600000.0f));
        this.base = base;
    }

    @Override
    public Block base() {
        return this.base;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult ctx) {
        if (!world.isClient()) {
            BlockEntity be = world.getBlockEntity(pos.offset(Direction.DOWN, 2));
            if (be instanceof ControlBlockEntity) {
                ((ControlBlockEntity) be).activateBoss(pos.up());
            } else {
                logger.warn("Unable to activate boss: no boss control at {}", pos);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, EntityContext ctx) {
        return this.toBreakable(state).getOutlineShape(world, pos, ctx);
    }
}
