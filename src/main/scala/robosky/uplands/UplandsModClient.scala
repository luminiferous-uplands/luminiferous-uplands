package robosky.uplands

import net.fabricmc.api.ClientModInitializer
import robosky.uplands.client.gui.GuiRegistry
import robosky.uplands.client.render.blockentity.BlockEntityRenderingRegistry

object UplandsModClient extends ClientModInitializer {

  override def onInitializeClient(): Unit = {
    GuiRegistry.init()
    BlockEntityRenderingRegistry.init()
  }
}
