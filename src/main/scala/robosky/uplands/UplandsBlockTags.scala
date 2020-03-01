package robosky.uplands

import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Block
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome

object UplandsBlockTags {
  val AzoteMushroomSpreadable = register("azote_mushroom_spreadable")
  val PlantableOn = register("plantable_on")

  def init(): Unit = {}

  def register(name: String): Tag[Block] = TagRegistry.block(UplandsMod :/ name)
}
