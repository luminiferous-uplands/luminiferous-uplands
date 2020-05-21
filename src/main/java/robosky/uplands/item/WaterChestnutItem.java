package robosky.uplands.item;

import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import robosky.uplands.block.BlockRegistry;

public class WaterChestnutItem extends Item {

    public WaterChestnutItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        HitResult hitResult = Item.rayTrace(world, player, RayTraceContext.FluidHandling.SOURCE_ONLY);

        if (hitResult.getType() == HitResult.Type.MISS)
            return new TypedActionResult<>(ActionResult.PASS, itemStack);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            BlockPos waterBlockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getSide();

            if (!world.canPlayerModifyAt(player, waterBlockPos) || !player.canPlaceOn(waterBlockPos.offset(direction), direction, itemStack))
                return new TypedActionResult<>(ActionResult.FAIL, itemStack);

            BlockPos cropBlockPos = waterBlockPos.up();
            FluidState waterFluidState = world.getFluidState(waterBlockPos);

            if ((waterFluidState.getFluid() == Fluids.WATER) && world.isAir(cropBlockPos)) {
                world.setBlockState(cropBlockPos, BlockRegistry.WATER_CHESTNUT_CROP_BLOCK.getDefaultState(), 11);

                if (player instanceof ServerPlayerEntity)
                    Criterions.PLACED_BLOCK.trigger((ServerPlayerEntity)player, cropBlockPos, itemStack);

                if (!player.abilities.creativeMode)
                    itemStack.decrement(1);

                player.incrementStat(Stats.USED.getOrCreateStat(this));

                world.playSound(player, waterBlockPos, SoundEvents.BLOCK_LILY_PAD_PLACE, SoundCategory.BLOCKS,
                        1.0F, 1.0F);

                return new TypedActionResult<>(ActionResult.SUCCESS, itemStack);
            }
        }

        return new TypedActionResult<>(ActionResult.FAIL, itemStack);
    }
}
