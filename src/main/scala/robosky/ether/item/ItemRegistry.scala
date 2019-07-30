package robosky.ether.item

import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import robosky.ether.UplandsMod

object ItemRegistry {
  val AEGISALT_CRYSTAL: Item = register("aegisalt_crystal", new Item(new Item.Settings()
    .group(UplandsMod.GROUP)))

  val AEGISALT_CHARM: Item = register("aegisalt_charm", CharmItem(StatusEffects.SLOW_FALLING, 0))
  val SKYROOT_CHARM: Item = register("skyroot_charm", CharmItem(StatusEffects.JUMP_BOOST, 0))

  def init(): Unit = {}

  private def register[A <: Item](name: String, item: A): A = Registry.register[A](Registry.ITEM, UplandsMod :/ name, item)
}
