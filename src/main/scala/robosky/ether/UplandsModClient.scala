package robosky.ether

import net.fabricmc.api.ClientModInitializer
import robosky.ether.client.gui.GuiRegistry
import robosky.ether.client.render.blockentity.BlockEntityRenderingRegistry

object UplandsModClient extends ClientModInitializer {

  override def onInitializeClient(): Unit = {
    GuiRegistry.init()
    BlockEntityRenderingRegistry.init()
  }
}
