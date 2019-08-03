package robosky.ether.item

import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.{AliasedBlockItem, Item}
import net.minecraft.util.registry.Registry
import robosky.ether.UplandsMod
import robosky.ether.block.BlockRegistry

object ItemRegistry {
    val AEGISALT_CRYSTAL: Item = register("aegisalt_crystal", new Item(new Item.Settings()
        .group(UplandsMod.GROUP)))

    val AEGISALT_CHARM: Item = register("aegisalt_charm", CharmItem(StatusEffects.SLOW_FALLING, 0))
    val SKYROOT_CHARM: Item = register("skyroot_charm", CharmItem(StatusEffects.JUMP_BOOST, 1))

    val seedSettings: Item.Settings = new Item.Settings().group(UplandsMod.GROUP)

    val ZEPHYR_ONION_ITEM: Item = register("zephyr_onion", new AliasedBlockItem(BlockRegistry.ZEPHYR_ONION_CROP_BLOCK, seedSettings))

    def init(): Unit = {}

    private def register[A <: Item](name: String, item: A): A = Registry.register[A](Registry.ITEM, UplandsMod :/ name, item)
}
