package robosky.ether

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.{ItemGroup, ItemStack}
import net.minecraft.util.Identifier
import robosky.ether.block.BlocksEther
import robosky.ether.item.ItemsEther
import robosky.ether.world.WorldRegistry
import robosky.ether.world.biome.BiomeRegistry

object EtherMod extends ModInitializer {

  val ETHER_GROUP: ItemGroup = FabricItemGroupBuilder.create(new Identifier("ether_dim", "general"))
    .icon(() => new ItemStack(BlocksEther.ETHER_GRASS)).build()

  override def onInitialize(): Unit = {
    ItemsEther.init()
    BlocksEther.init()
    BiomeRegistry.init()
    WorldRegistry.init()
  }
}
