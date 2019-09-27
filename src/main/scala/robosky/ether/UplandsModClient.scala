package robosky.ether

import net.fabricmc.api.ClientModInitializer
import robosky.ether.client.gui.GuiRegistry

object UplandsModClient extends ClientModInitializer {

  override def onInitializeClient(): Unit = {
    GuiRegistry.init()
  }
}
