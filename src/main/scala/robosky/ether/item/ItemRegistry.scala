package robosky.ether.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.registry.Registry
import net.minecraft.util.{Hand, Identifier, TypedActionResult}
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import robosky.ether.EtherMod
import robosky.ether.world.WorldRegistry

object ItemRegistry {
  val AEGISALT_CRYSTAL: Item = register("aegisalt_crystal", new Item(new Item.Settings().group(EtherMod.ETHER_GROUP)))

  def init(): Unit = {}

  private def register[A <: Item](name: String, item: A) =
    Registry.register(Registry.ITEM, new Identifier("ether_dim", name), item)
}
