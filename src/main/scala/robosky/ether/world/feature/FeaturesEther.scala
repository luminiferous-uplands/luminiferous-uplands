package robosky.ether.world.feature

import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object FeaturesEther {
  val oreFeature: EtherOreFeature.type = Registry.register[EtherOreFeature.type](Registry.FEATURE, new Identifier("ether_dim", "oregen"), EtherOreFeature)

  def init(): Unit = {}
}
