package robosky.ether.block.machine.base

import java.util.function.Consumer

import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.{Hand, PacketByteBuf}
import net.minecraft.world.World

import scala.reflect.ClassTag

trait MachineGUIBlockActivationSkeleton[A <: BlockEntity] {
  self: Block =>

  override def activate(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult): Boolean = {
    if (world.isClient) return true
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
    true
  }

  protected def beCTag: ClassTag[A]
}
