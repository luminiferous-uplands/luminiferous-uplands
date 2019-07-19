package robosky.ether.world

import com.github.draylar.dimension.api.FabricDimensionType
import net.minecraft.util.Identifier

object EtherDimensionType extends FabricDimensionType(new Identifier("ether_dim", "ether_dim"),
  10, new EtherDimension(_, _))
