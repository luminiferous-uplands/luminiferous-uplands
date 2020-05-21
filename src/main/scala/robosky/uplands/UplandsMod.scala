package robosky.uplands

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.{Item, ItemGroup, ItemStack}
import net.minecraft.tag.{ItemTags, Tag}
import net.minecraft.util.Identifier
import robosky.uplands.block.BlockRegistry
import robosky.uplands.entity.EntityRegistry
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

  val BOSSROOM_TECHNICAL_TAG: Tag[Item] = new ItemTags.CachingTag(this :/ "bossroom_technical")

  override def onInitialize(): Unit = {
    BlockRegistry.init()
    ItemRegistry.init()
    EntityRegistry.init()
    FeatureRegistry.init()
    BiomeRegistry.init()
    WorldRegistry.init()
    MachineRegistry.init()
    RecipeRegistry.init()
  }

  def :/(name: String) = new Identifier("luminiferous_uplands", name)

  def id(name: String) = this :/ name
}
