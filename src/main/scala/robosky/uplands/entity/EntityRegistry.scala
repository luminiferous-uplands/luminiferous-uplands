package robosky.uplands.entity

import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder

import net.minecraft.entity.{EntityCategory, EntityDimensions, EntityType}
import net.minecraft.item.{Item, ItemGroup, SpawnEggItem}
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

import robosky.uplands.UplandsMod

object EntityRegistry {

  val HEXAHAEN: EntityType[HexahaenEntity] = FabricEntityTypeBuilder
    .create(EntityCategory.MONSTER, new HexahaenEntity(_, _))
    .size(EntityDimensions.fixed(1.25f, 1.25f))
    .build

  private def registerMob(name: String, entityType: EntityType[_], primaryColor: Int, secondaryColor: Int): Unit = {
    Registry.register(Registry.ENTITY_TYPE, UplandsMod :/ name, entityType)
    Registry.register(Registry.ITEM, UplandsMod :/ (name + "_spawn_egg"),
        new SpawnEggItem(entityType, primaryColor, secondaryColor, new Item.Settings().group(ItemGroup.MISC)))
  }

  def init(): Unit = {
    registerMob("hexahaen", HEXAHAEN, 0x0e7543, 0xeb8154)
  }
}
