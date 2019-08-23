package robosky.ether.world.biome

import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import robosky.ether.UplandsMod

object BiomeRegistry {
  val ETHER_HIGHLANDS_BIOME: BiomeEtherHighlands.type = register("ether_highlands")(BiomeEtherHighlands)

  def init(): Unit = {}

  def register[B <: Biome](name: String)(b: B): B = Registry.register[B](Registry.BIOME, UplandsMod :/ name, b)
}
