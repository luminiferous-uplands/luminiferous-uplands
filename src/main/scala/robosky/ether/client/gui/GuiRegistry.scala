package robosky.ether.client.gui

import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.client.CottonScreen

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry

import net.minecraft.container.BlockContext
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}

import robosky.ether.UplandsMod
import robosky.ether.block.machine.MachineRegistry
import robosky.ether.block.machine.infuser.InfuserContainer

// delete this class?
class InfuserScreen(container: InfuserContainer, player: PlayerEntity)
  extends CottonScreen[InfuserContainer](container, player)

object GuiRegistry {

  val agisaltInfuserScreen = registerGui("aegisalt_infuser", aegisaltInfuser.gui.get, new InfuserScreen(_, _))

  def registerGui[C <: CottonScreenController](
      id: String,
      ctrl: (Int, PlayerInventory, BlockContext) => C,
      screen: (C, PlayerEntity) => CottonScreen[C]): Unit = {
    ScreenProviderRegistry.INSTANCE.registerFactory(UplandsMod :/ id,
      (syncId, _, player, buf) =>
        screen(ctrl(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player))
  }

  def init(): Unit = {}
}
