package robosky.ether.item

import net.minecraft.entity.effect.{StatusEffect, StatusEffectInstance}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.Hand
import robosky.ether.{TickableItem, UplandsMod}

case class CharmItem(effect: StatusEffect, level: Int, maxDurability: Int = 2400) extends Item(new Item.Settings()
  .group(UplandsMod.GROUP).maxCount(1).maxDamage(maxDurability)) with TickableItem {
  override def tick(entity: PlayerEntity, stack: ItemStack, hand: Hand): Unit = {
    stack.damage(1, entity, (player: PlayerEntity) => {})
    entity.addPotionEffect(new StatusEffectInstance(effect, 10, level, false, false,
      false))
  }
}
