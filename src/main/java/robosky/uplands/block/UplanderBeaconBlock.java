package robosky.uplands.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.dimension.DimensionType;
import robosky.uplands.iface.UplanderBeaconUser;

import java.util.Random;

public class UplanderBeaconBlock extends Block {

    private static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 16, 14);
    private static final BooleanProperty SMOKING = BooleanProperty.of("smoking");

    public UplanderBeaconBlock() {
        super(FabricBlockSettings.of(Material.STONE).strength(3, 3).breakByTool(FabricToolTags.PICKAXES, 2).ticksRandomly().build());
        setDefaultState(getStateManager().getDefaultState().with(SMOKING, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SMOKING);
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult result) {
        if (player.world.dimension.getType() == DimensionType.OVERWORLD) {
            ((UplanderBeaconUser)player).uplands_setUsingBeacon(true);
        } else {
            if (blockState.get(SMOKING))
                return ActionResult.FAIL;

            world.setBlockState(blockPos, BlockRegistry.UPLANDER_BEACON.getDefaultState().with(SMOKING, true));
            world.getBlockTickScheduler().schedule(blockPos, this, this.getTickRate(world));
            world.playSound(player, blockPos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3f, 0.6f);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public int getTickRate(WorldView worldView) {
        return 35;
    }

    @Override
    public void scheduledTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        if (!serverWorld.isClient && blockState.get(SMOKING))
            serverWorld.setBlockState(blockPos, blockState.with(SMOKING, false));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        if (blockState.get(SMOKING)) {
            double baseX = blockPos.getX() + 0.5D;
            double baseY = blockPos.getY() + 0.9D;
            double baseZ = blockPos.getZ() + 0.5D;

            world.addParticle(ParticleTypes.SMOKE, baseX, baseY, baseZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
        return SHAPE;
    }
}
