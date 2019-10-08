package robosky.uplands

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.{ItemGroup, ItemStack}
import net.minecraft.util.Identifier
import robosky.uplands.block.BlockRegistry
import robosky.uplands.block.machine.{MachineRegistry, RecipeRegistry}
import robosky.uplands.item.ItemRegistry
import robosky.uplands.world.WorldRegistry
import robosky.uplands.world.biome.BiomeRegistry
import robosky.uplands.world.feature.FeatureRegistry

object UplandsMod extends ModInitializer {

  val GROUP: ItemGroup = FabricItemGroupBuilder
    .create(this :/ "general")
    .icon(() => new ItemStack(BlockRegistry.UPLANDER_GRASS))
    .build()

  override def onInitialize(): Unit = {
    ItemRegistry.init()
    BlockRegistry.init()
    FeatureRegistry.init()
    BiomeRegistry.init()
    WorldRegistry.init()
    MachineRegistry.init()
    RecipeRegistry.init()
  }

  def :/(name: String) = new Identifier("luminiferous_uplands", name)
}
