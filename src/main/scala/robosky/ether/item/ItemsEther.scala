package robosky.ether.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.registry.Registry
import net.minecraft.util.{Hand, Identifier, TypedActionResult}
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import robosky.ether.EtherMod
import robosky.ether.world.WorldRegistry

object ItemsEther {
  val TELEPORT_DEBUG_STAFF: Item = register("debug_teleport_staff", new Item(new Item.Settings().group(EtherMod.ETHER_GROUP).maxCount(1)) {
    override def use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult[ItemStack] = {
      if (hand eq Hand.MAIN_HAND) { // coming from our custom dimension
        if (player.world.dimension.getType eq WorldRegistry.ETHER_DIMENSION) player.changeDimension(DimensionType.OVERWORLD)
        else { // going to our custom dimension
          player.changeDimension(WorldRegistry.ETHER_DIMENSION)
        }
      }
      super.use(world, player, hand)
    }
  })

  val AEGISALT_CRYSTAL: Item = register("aegisalt_crystal", new Item(new Item.Settings().group(EtherMod.ETHER_GROUP)))

  def init(): Unit = {}

  private def register[A <: Item](name: String, item: A) =
    Registry.register(Registry.ITEM, new Identifier("ether_dim", name), item)
}
