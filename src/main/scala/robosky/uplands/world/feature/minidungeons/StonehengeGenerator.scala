package robosky.uplands.world.feature.minidungeons

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.minecraft.structure.pool.StructurePool.Projection
import net.minecraft.structure.pool.{EmptyPoolElement, ListPoolElement, SinglePoolElement, StructurePool, StructurePoolBasedGenerator, StructurePoolElement}
import net.minecraft.structure.processor.BlockRotStructureProcessor
import net.minecraft.util.Identifier
import robosky.uplands.UplandsMod

object StonehengeGenerator {
  StructurePoolBasedGenerator.REGISTRY.add(
    new StructurePool(
      UplandsMod :/ "stonehenge/base",
      new Identifier("empty"),
      ImmutableList.of(Pair.of(
        new SinglePoolElement("stonehenge/base"), 1)),
      Projection.TERRAIN_MATCHING))

  StructurePoolBasedGenerator.REGISTRY.add(
    new StructurePool(
      UplandsMod :/ "stonehenge/pillar_base",
      new Identifier("empty"),
      ImmutableList.of(Pair.of(
        new SinglePoolElement("stonehenge/pillar_base"), 1)),
      Projection.TERRAIN_MATCHING))

  StructurePoolBasedGenerator.REGISTRY.add(
    new StructurePool(
      UplandsMod :/ "stonehenge/altar",
      new Identifier("empty"),
      ImmutableList.of(Pair.of(
        new SinglePoolElement("stonehenge/altar_with_chest"), 1)),
      Projection.RIGID))

  StructurePoolBasedGenerator.REGISTRY.add(
    new StructurePool(
      UplandsMod :/ "stonehenge/pillar",
      new Identifier("empty"),
      ImmutableList.of(Pair.of(
        new SinglePoolElement("stonehenge/pillar_pristine"), 1)),
      Projection.RIGID))
}
