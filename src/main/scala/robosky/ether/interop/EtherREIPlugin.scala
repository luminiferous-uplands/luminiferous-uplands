package robosky.ether.interop

import me.shedaniel.rei.api.REIPluginEntry
import net.minecraft.util.Identifier
import robosky.ether.UplandsMod

class EtherREIPlugin extends REIPluginEntry {
  override def getPluginIdentifier: Identifier = UplandsMod :/ "uplands_rei"
}
