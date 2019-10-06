package robosky.uplands.item

import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.{AliasedBlockItem, FoodComponent, Item}
import net.minecraft.util.registry.Registry
import robosky.uplands.UplandsMod
import robosky.uplands.block.BlockRegistry

object ItemRegistry {
    val AEGISALT_CRYSTAL: Item = register("aegisalt_crystal", new Item(new Item.Settings()
        .group(UplandsMod.GROUP)))

    val AEGISALT_CHARM: Item = register("aegisalt_charm", CharmItem(StatusEffects.SLOW_FALLING, 0))
    val SKYROOT_CHARM: Item = register("skyroot_charm", CharmItem(StatusEffects.JUMP_BOOST, 1))

    val ZEPHYR_ONION_ITEM: Item = register("zephyr_onion", new AliasedBlockItem(
        BlockRegistry.ZEPHYR_ONION_CROP_BLOCK, new Item.Settings()
            .group(UplandsMod.GROUP)
            .food(new FoodComponent.Builder()
                .hunger(1)
                .saturationModifier(0.3F)
                .build())))

    val ROASTED_ZEPHYR_ONION_ITEM: Item = register("roasted_zephyr_onion", new Item(new Item.Settings()
        .group(UplandsMod.GROUP)
        .food(new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.6F)
            .build())))

    val WATER_CHESTNUT_ITEM: Item = register("water_chestnut", new Item(new Item.Settings()
        .group(UplandsMod.GROUP)
        .food(new FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0.3F)
            .build())))

    val WATER_CHESTNUT_SEEDS_ITEM: Item = register("water_chestnut_seeds", new WaterChestnutItem(
        new Item.Settings()
            .group(UplandsMod.GROUP)))

    val SKYROOT_FRUIT: Item = register("skyroot_fruit", new Item(new Item.Settings()
        .group(UplandsMod.GROUP).food(new FoodComponent.Builder()
            .hunger(4)
            .saturationModifier(0.6f)
            .build())))

    def init(): Unit = {}

    private def register[A <: Item](name: String, item: A): A = Registry.register[A](Registry.ITEM, UplandsMod :/ name, item)
}
