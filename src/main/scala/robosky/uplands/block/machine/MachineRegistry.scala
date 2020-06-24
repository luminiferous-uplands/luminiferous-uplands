package robosky.uplands.block.machine

import com.google.common.collect.ImmutableSet
import io.github.cottonmc.cotton.gui.CottonCraftingController
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.util.registry.Registry
import net.minecraft.util.{Identifier, PacketByteBuf}
import robosky.uplands.block.BlockRegistry
import robosky.uplands.block.machine.base.{BaseMachineBlock, BaseMachineBlockEntity}
import robosky.uplands.block.machine.infuser.{AegisaltInfuser, AegisaltInfuserBlock, InfuserContainer}

import scala.collection.mutable

object MachineRegistry {

  val MACHINES: mutable.HashMap[Identifier, BlockEntityType[_ <: BaseMachineBlockEntity]] = mutable.HashMap.empty

  val aegisaltInfuser: MachineEntry[AegisaltInfuserBlock, AegisaltInfuser, InfuserContainer] =
    register("aegisalt_infuser", Machine(new AegisaltInfuserBlock(), () => new AegisaltInfuser, Some(new InfuserContainer(_, _, _))))

  def register[B <: BaseMachineBlock, E <: BaseMachineBlockEntity, C <: CottonCraftingController](name: String,
    m: Machine[B, E, C]): MachineEntry[B, E, C] = {
    BlockRegistry.registerWithItem(name, m.b)
    register(m)
  }

  def register[B <: BaseMachineBlock, E <: BaseMachineBlockEntity, C <: CottonCraftingController](
    m: Machine[B, E, C]): MachineEntry[B, E, C] = m match {
    case Machine(b, e, gui) =>
      val id = Registry.BLOCK.getId(b)
      val t = new BlockEntityType[E](() => e(), ImmutableSet.of(b), null)
      Registry.register(Registry.BLOCK_ENTITY, id, t)
      MACHINES.put(id, t)
      gui.foreach(registerGui(id, _))
      MachineEntry(m, t)
  }

  def registerGui[C <: CottonCraftingController](id: Identifier, ctrl: (Int, PlayerInventory, BlockContext) => C): Unit = {
    ContainerProviderRegistry.INSTANCE.registerFactory(id, (syncId: Int, _: Identifier, player: PlayerEntity,
      buf: PacketByteBuf) => ctrl(syncId, player.inventory, BlockContext.create(player.world,
      buf.readBlockPos())))
  }

  def init(): Unit = {}

  case class Machine[B <: BaseMachineBlock, E <: BaseMachineBlockEntity, C <: CottonCraftingController](b: B, e: () => E,
    gui: Option[(Int, PlayerInventory, BlockContext) => C] = None)

  case class MachineEntry[B <: BaseMachineBlock, E <: BaseMachineBlockEntity, C <: CottonCraftingController](
    machine: Machine[B, E, C], blockEntityType: BlockEntityType[E]) {
    val block: B = machine.b
    val be: () => E = machine.e
    val gui: Option[(Int, PlayerInventory, BlockContext) => C] = machine.gui
  }
}
