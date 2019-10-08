package robosky.uplands

import net.fabricmc.api.ClientModInitializer
import robosky.uplands.client.gui.GuiRegistry

object UplandsModClient extends ClientModInitializer {

  override def onInitializeClient(): Unit = {
    GuiRegistry.init()
  }
}
