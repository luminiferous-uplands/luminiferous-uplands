package robosky.uplands

import net.fabricmc.api.ClientModInitializer
import robosky.uplands.block.BlockRenderingRegistry
import robosky.uplands.client.gui.GuiRegistry
import robosky.uplands.client.render.blockentity.BlockEntityRenderingRegistry
import robosky.uplands.client.render.entity.EntityRenderingRegistry

object UplandsModClient extends ClientModInitializer {

  override def onInitializeClient(): Unit = {
    BlockRenderingRegistry.init()
    GuiRegistry.init()
    BlockEntityRenderingRegistry.init()
    EntityRenderingRegistry.init()
  }
}
