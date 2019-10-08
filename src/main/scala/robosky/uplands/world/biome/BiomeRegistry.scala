package robosky.uplands.world.biome

import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import robosky.uplands.UplandsMod

object BiomeRegistry {
  val UPLANDS_HIGHLANDS_BIOME: UplandsAutumnBiome.type = register("uplands_autumn")(UplandsAutumnBiome)

  def init(): Unit = {}

  def register[B <: Biome](name: String)(b: B): B = Registry.register[B](Registry.BIOME, UplandsMod :/ name, b)
}
