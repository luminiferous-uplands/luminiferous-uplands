package robosky.ether.world.feature.minidungeons

import java.util.Optional

import net.minecraft.util.Identifier

// Does not extend FeatureConfig so we can get it into the StructureStart because this shit is dumb
case class MinidungeonFeatureConfig(name: String, template: Identifier, loot: Optional[Identifier])
