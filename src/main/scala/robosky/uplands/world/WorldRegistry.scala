package robosky.uplands.world

import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import robosky.uplands.{UplandsMod, UplandsTeleporter}

object WorldRegistry {

  val UPLANDS_DIMENSION: FabricDimensionType = FabricDimensionType.builder()
    .factory((t: World, u: DimensionType) => new UplandsDimension(t, u)).skyLight(true)
    .defaultPlacer(UplandsTeleporter.ToUplandsBeacon)
    .buildAndRegister(UplandsMod :/ "luminiferous_uplands")

  def init(): Unit = Unit
}
