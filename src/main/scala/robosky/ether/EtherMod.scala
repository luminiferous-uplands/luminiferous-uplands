package robosky.ether

import net.fabricmc.api.ModInitializer
import robosky.ether.block.BlocksEther
import robosky.ether.world.WorldRegistry

object EtherMod extends ModInitializer {
  override def onInitialize(): Unit = {
    BlocksEther.init()
    WorldRegistry.init()
  }
}
