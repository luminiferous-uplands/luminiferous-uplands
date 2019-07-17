package robosky.ether.world

import com.github.draylar.dimension.api.FabricDimensionType
import net.minecraft.util.Identifier

object EtherDimensionType extends FabricDimensionType(new Identifier("ether-dim", "ether-dim"),
  10, new EtherDimension(_, _))
