package robosky.uplands.entity

import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder

import net.minecraft.entity.{EntityCategory, EntityDimensions, EntityType}
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

import robosky.uplands.UplandsMod

object EntityRegistry {

  val HEXAHAEN: EntityType[HexahaenEntity] = FabricEntityTypeBuilder
    .create(EntityCategory.MONSTER, new HexahaenEntity(_, _))
    .size(EntityDimensions.fixed(1.25f, 1.25f))
    .build

  def init(): Unit = {
    Registry.register(Registry.ENTITY_TYPE, UplandsMod :/ "hexahaen", HEXAHAEN);
  }
}
