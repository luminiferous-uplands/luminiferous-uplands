package robosky.ether.block.machine

import com.google.common.collect.ImmutableSet
import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.client.CottonScreen
import net.fabricmc.api.{EnvType, Environment}
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.util.registry.Registry
import net.minecraft.util.{Identifier, PacketByteBuf}
import robosky.ether.block.machine.base.{BaseMachineBlock, BaseMachineBlockEntity}

import scala.collection.mutable

object MachineRegistry {

  val machineRegistry: mutable.HashMap[Identifier, BlockEntityType[_ <: BaseMachineBlockEntity]] = mutable.HashMap.empty

  def register[B <: BaseMachineBlock, E <: BaseMachineBlockEntity, C <: CottonScreenController](m: Machine[B, E, C]): Unit = m match {
    case Machine(b, e, gui) =>
      val id = Registry.BLOCK.getId(b)
      val t = new BlockEntityType[E](() => e(), ImmutableSet.of(b), null)
      Registry.register(Registry.BLOCK_ENTITY, id, t)
      machineRegistry.put(id, t)
      gui.foreach(registerGui(id, _))
  }

  def registerGui[C <: CottonScreenController](id: Identifier, gui: MachineGui[C]): Unit = {
    ContainerProviderRegistry.INSTANCE.registerFactory(id, (syncId: Int, _: Identifier, player: PlayerEntity,
      buf: PacketByteBuf) => gui.controller(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())))

    @Environment(EnvType.CLIENT)
    def registerClient(): Unit = ScreenProviderRegistry.INSTANCE.registerFactory(id, (syncId: Int, _: Identifier,
      player: PlayerEntity, buf: PacketByteBuf) => gui.screen(gui.controller(syncId, player.inventory,
      BlockContext.create(player.world, buf.readBlockPos())), player))

    registerClient()
  }

  case class MachineGui[C <: CottonScreenController](controller: (Int, PlayerInventory, BlockContext) => C,
    screen: (C, PlayerEntity) => CottonScreen[C])

  case class Machine[B <: BaseMachineBlock, E <: BaseMachineBlockEntity, C <: CottonScreenController](b: B, e: () => E,
    gui: Option[MachineGui[C]] = None)

}
