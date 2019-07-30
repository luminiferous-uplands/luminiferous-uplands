package robosky.ether.item

import net.minecraft.entity.effect.{StatusEffectInstance, StatusEffects}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.Hand
import net.minecraft.util.registry.Registry
import robosky.ether.{TickableItem, UplandsMod}

object ItemRegistry {
  private val charmSettings: Item.Settings = new Item.Settings().group(UplandsMod.GROUP).maxCount(1).maxDamage(2400)
  val AEGISALT_CRYSTAL: Item = register("aegisalt_crystal", new Item(new Item.Settings()
    .group(UplandsMod.GROUP)))

  val AEGISALT_CHARM: Item = register("aegisalt_charm", new Item(charmSettings) with TickableItem {
    override def tick(entity: PlayerEntity, stack: ItemStack, hand: Hand): Unit = {
      stack.damage(1, entity, (player: PlayerEntity) => player.addPotionEffect(new StatusEffectInstance(
        StatusEffects.SLOW_FALLING, 10, 0, false, false, false)))
    }
  })
  val SKYROOT_CHARM: Item = register("skyroot_charm", new Item(charmSettings) with TickableItem {
    override def tick(entity: PlayerEntity, stack: ItemStack, hand: Hand): Unit = {
      stack.damage(1, entity, (player: PlayerEntity) => player.addPotionEffect(new StatusEffectInstance(
        StatusEffects.JUMP_BOOST, 10, 0, false, false, false)))
    }
  })

  def init(): Unit = {}

  private def register[A <: Item](name: String, item: A): A = Registry.register[A](Registry.ITEM, UplandsMod :/ name, item)
}
