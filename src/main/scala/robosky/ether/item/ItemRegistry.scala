package robosky.ether.item

import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import robosky.ether.UplandsMod

object ItemRegistry {
  val AEGISALT_CRYSTAL: Item = register("aegisalt_crystal", new Item(new Item.Settings()
    .group(UplandsMod.GROUP)))

  val IRON_CHARM: Item = register("iron_charm", new Item(new Item.Settings().group(UplandsMod.GROUP)
    .maxCount(1)))

  val AEGISALT_CHARM: Item = register("aegisalt_charm", new Item(new Item.Settings().group(UplandsMod.GROUP)
    .maxCount(1)))

  def init(): Unit = {}

  private def register[A <: Item](name: String, item: A): A = Registry.register[A](Registry.ITEM, UplandsMod :/ name, item)
}
