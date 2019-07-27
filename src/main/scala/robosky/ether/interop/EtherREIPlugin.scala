package robosky.ether.interop

import me.shedaniel.rei.api.REIPluginEntry
import net.minecraft.util.Identifier

class EtherREIPlugin extends REIPluginEntry {
  override def getPluginIdentifier: Identifier = new Identifier("ether_dim", "ether_rei")
}
