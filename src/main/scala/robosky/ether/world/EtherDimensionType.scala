package robosky.ether.world

import com.github.draylar.dimension.api.FabricDimensionType
import robosky.ether.UplandsMod

object EtherDimensionType extends FabricDimensionType(UplandsMod :/ "luminiferous_uplands", 10,
  new EtherDimension(_, _))
