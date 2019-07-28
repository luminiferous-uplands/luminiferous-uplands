package robosky.ether.block.machine.base

import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

trait MachineGUIBlockActivationSkeleton[A <: BlockEntity[B], B <: Block] {
  self: B =>

  override def activate(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult): Boolean = {
    if (world.isClient) return true
    world.getBlockEntity(pos) match {
      case _: A =>
        ContainerProviderRegistry.INSTANCE.openContainer(Registry.BLOCK.getId(self), player, buf => buf.writeBlockPos(pos))
      case _ =>
    }
    true
  }
}
