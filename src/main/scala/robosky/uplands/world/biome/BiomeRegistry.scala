package robosky.uplands.world.biome

import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import robosky.uplands.UplandsMod

object BiomeRegistry {
  val ETHER_HIGHLANDS_BIOME: BiomeUplandsHighlands.type = register("uplands_highlands")(BiomeUplandsHighlands)

  def init(): Unit = {}

  def register[B <: Biome](name: String)(b: B): B = Registry.register[B](Registry.BIOME, UplandsMod :/ name, b)
}
