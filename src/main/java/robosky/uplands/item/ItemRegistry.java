package robosky.uplands.item;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import robosky.uplands.UplandsMod;
import robosky.uplands.block.BlockRegistry;

public class ItemRegistry {

    public static Item AEGISALT_CRYSTAL;

    public static Item AEGISALT_CHARM;
    public static Item SKYROOT_CHARM;

    public static Item ZEPHYR_ONION_ITEM;
    public static Item ROASTED_ZEPHYR_ONION_ITEM;

    public static Item WATER_CHESTNUT_ITEM;
    public static Item WATER_CHESTNUT_SEEDS_ITEM;

    public static Item SKYROOT_FRUIT;

    public static void init() {
        AEGISALT_CRYSTAL = register("aegisalt_crystal", new Item(new Item.Settings().group(UplandsMod.GROUP())));

        AEGISALT_CHARM = register("aegisalt_charm", new CharmItem(StatusEffects.SLOW_FALLING, 0));
        SKYROOT_CHARM = register("skyroot_charm", new CharmItem(StatusEffects.JUMP_BOOST, 1));

        ZEPHYR_ONION_ITEM = register("zephyr_onion", new AliasedBlockItem(
                BlockRegistry.ZEPHYR_ONION_CROP_BLOCK(), new Item.Settings()
                .group(UplandsMod.GROUP())
                .food(new FoodComponent.Builder()
                        .hunger(1)
                        .saturationModifier(0.3F)
                        .build())));
        ROASTED_ZEPHYR_ONION_ITEM = register("roasted_zephyr_onion", new Item(new Item.Settings()
                .group(UplandsMod.GROUP())
                .food(new FoodComponent.Builder()
                        .hunger(5)
                        .saturationModifier(0.6F)
                        .build())));

        WATER_CHESTNUT_ITEM = register("water_chestnut", new Item(new Item.Settings()
                .group(UplandsMod.GROUP())
                .food(new FoodComponent.Builder()
                        .hunger(1)
                        .saturationModifier(0.3F)
                        .build())));
        WATER_CHESTNUT_SEEDS_ITEM = register("water_chestnut_seeds", new WaterChestnutItem(
                new Item.Settings()
                        .group(UplandsMod.GROUP())));

        SKYROOT_FRUIT = register("skyroot_fruit", new Item(new Item.Settings()
                .group(UplandsMod.GROUP()).food(new FoodComponent.Builder()
                        .hunger(4)
                        .saturationModifier(0.6f)
                        .build())));
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, UplandsMod.id(name), item);
    }
}
