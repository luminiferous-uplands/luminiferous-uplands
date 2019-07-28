package robosky.ether.item

import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import robosky.ether.EtherMod

object ItemRegistry {
  val AEGISALT_CRYSTAL: Item = register("aegisalt_crystal", new Item(new Item.Settings().group(EtherMod.ETHER_GROUP)))

  def init(): Unit = {}

  private def register[A <: Item](name: String, item: A) =
    Registry.register(Registry.ITEM, new Identifier("ether_dim", name), item)
}
