package robosky.uplands.client.gui

import io.github.cottonmc.cotton.gui.CottonCraftingController
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry

import net.minecraft.block.Block
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.registry.Registry

import robosky.uplands.block.machine.MachineRegistry
import robosky.uplands.block.machine.infuser.InfuserContainer

// delete this class?
class InfuserScreen(container: InfuserContainer, player: PlayerEntity)
  extends CottonInventoryScreen[InfuserContainer](container, player)

object GuiRegistry {

  val agisaltInfuserScreen = registerGui(MachineRegistry.aegisaltInfuser, new InfuserScreen(_, _))

  def registerGui[C <: CottonCraftingController](
      entry: MachineRegistry.MachineEntry[_ <: Block, _, C],
      screen: (C, PlayerEntity) => CottonInventoryScreen[C]): Unit = {
    val id = Registry.BLOCK.getId(entry.block: Block)
    val ctrl = entry.gui.get
    ScreenProviderRegistry.INSTANCE.registerFactory(id,
      (syncId, _, player, buf) =>
        screen(ctrl(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player))
  }

  def init(): Unit = {}
}
