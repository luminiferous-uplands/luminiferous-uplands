package robosky.uplands.entity;

import robosky.uplands.UplandsMod;

import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {

    public static final EntityType<HexahaenEntity> HEXAHAEN = FabricEntityTypeBuilder
        .create(SpawnGroup.MONSTER, (EntityType.EntityFactory<HexahaenEntity>)HexahaenEntity::new)
        .size(EntityDimensions.fixed(1.25f, 1.25f)).build();

    public static void init() {
        registerMob("hexahaen", HEXAHAEN, 0x0e7543, 0xeb8154);
        FabricDefaultAttributeRegistry.register(HEXAHAEN, DefaultAttributeContainer.builder()
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23));
    }

    private static <E extends Entity> void registerMob(String name, EntityType<E> entityType, int primaryColor, int secondaryColor) {
        Registry.register(Registry.ENTITY_TYPE, UplandsMod.id(name), entityType);
        Registry.register(Registry.ITEM, UplandsMod.id(name + "_spawn_egg"),
            new SpawnEggItem(entityType, primaryColor, secondaryColor, new Item.Settings().group(ItemGroup.MISC)));
    }
}
