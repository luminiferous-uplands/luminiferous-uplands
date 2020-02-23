package robosky.uplands.block.machine.base

import java.util.function.Consumer

import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.{ActionResult, Hand, PacketByteBuf}
import net.minecraft.world.World

import scala.reflect.ClassTag

trait MachineGUIBlockActivationSkeleton[A <: BlockEntity] {
  self: Block =>

  override def onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult): ActionResult = {
    if (world.isClient) return ActionResult.SUCCESS
    world.getBlockEntity(pos) match {
      case a if beCTag.runtimeClass.isInstance(a) =>
        ContainerProviderRegistry.INSTANCE.openContainer(
          Registry.BLOCK.getId(self),
          player,
          new Consumer[PacketByteBuf] {
            override def accept(buf: PacketByteBuf): Unit =
              buf.writeBlockPos(pos)
          }
        )
      case _ =>
    }
    ActionResult.SUCCESS
  }

  protected def beCTag: ClassTag[A]
}
