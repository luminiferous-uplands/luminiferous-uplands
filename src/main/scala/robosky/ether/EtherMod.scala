package robosky.ether

import net.fabricmc.api.ModInitializer
import robosky.ether.block.BlocksEther
import robosky.ether.item.ItemsEther
import robosky.ether.world.WorldRegistry
import robosky.ether.world.biome.BiomeRegistry

object EtherMod extends ModInitializer {
  override def onInitialize(): Unit = {
    ItemsEther.init()
    BlocksEther.init()
    BiomeRegistry.init()
    WorldRegistry.init()
  }
}
