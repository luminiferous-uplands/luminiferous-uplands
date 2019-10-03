package robosky.ether.item

import net.minecraft.advancement.criterion.Criterions
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.{Item, ItemStack, ItemUsageContext}
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.{SoundCategory, SoundEvents}
import net.minecraft.stat.Stats
import net.minecraft.util.{ActionResult, Hand, TypedActionResult}
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult.Type
import net.minecraft.world.RayTraceContext.FluidHandling
import net.minecraft.world.World
import robosky.ether.block.BlockRegistry


class WaterChestnutItem(itemSettings: Item.Settings) extends Item(itemSettings) {
    override def useOnBlock(itemUsageContext: ItemUsageContext) = ActionResult.PASS

    override def use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult[ItemStack] = {
        val itemStack_1 = player.getStackInHand(hand)
        val hitResult = Item.rayTrace(world, player, FluidHandling.SOURCE_ONLY)

        if (hitResult.getType eq Type.MISS) {
            return new TypedActionResult[ItemStack](ActionResult.PASS, itemStack_1)
        }

        if (hitResult.getType eq Type.BLOCK) {
            val blockHitResult = hitResult.asInstanceOf[BlockHitResult]
            val waterBlockPos = blockHitResult.getBlockPos
            val direction = blockHitResult.getSide

            if (!world.canPlayerModifyAt(player, waterBlockPos) || !player.canPlaceOn(waterBlockPos.offset(direction),
                direction, itemStack_1))
            {
                return new TypedActionResult[ItemStack](ActionResult.FAIL, itemStack_1)
            }

            val cropBlockPos = waterBlockPos.up
            val waterFluidState = world.getFluidState(waterBlockPos)

            if ((waterFluidState.getFluid eq Fluids.WATER) && world.isAir(cropBlockPos)) {
                world.setBlockState(cropBlockPos, BlockRegistry.WATER_CHESTNUT_CROP_BLOCK.getDefaultState, 11)

                player match {
                    case entity: ServerPlayerEntity => Criterions.PLACED_BLOCK.handle(entity, cropBlockPos, itemStack_1)
                    case _ =>
                }

                if (!player.abilities.creativeMode) {
                    itemStack_1.decrement(1)
                }
                player.incrementStat(Stats.USED.getOrCreateStat(this))

                world.playSound(player, waterBlockPos, SoundEvents.BLOCK_LILY_PAD_PLACE, SoundCategory.BLOCKS,
                    1.0F, 1.0F)

                return new TypedActionResult[ItemStack](ActionResult.SUCCESS, itemStack_1)
            }
        }

        new TypedActionResult[ItemStack](ActionResult.FAIL, itemStack_1)
    }
}
