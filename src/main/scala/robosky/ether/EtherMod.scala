package robosky.ether

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.{ItemGroup, ItemStack}
import net.minecraft.util.Identifier
import robosky.ether.block.BlockRegistry
import robosky.ether.item.ItemRegistry
import robosky.ether.world.biome.BiomeRegistry
import robosky.ether.world.feature.FeatureRegistry
import robosky.ether.world.{MixinHackHooksImpl, WorldRegistry}

object EtherMod extends ModInitializer {

  val ETHER_GROUP: ItemGroup = FabricItemGroupBuilder.create(new Identifier("ether_dim", "general"))
    .icon(() => new ItemStack(BlockRegistry.ETHER_GRASS)).build()

  override def onInitialize(): Unit = {
    ItemRegistry.init()
    BlockRegistry.init()
    FeatureRegistry.init()
    BiomeRegistry.init()
    WorldRegistry.init()
    MixinHack.HOOKS = MixinHackHooksImpl
  }
}
