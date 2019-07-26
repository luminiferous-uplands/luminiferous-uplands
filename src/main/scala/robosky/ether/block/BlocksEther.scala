package robosky.ether.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.{Block, Material}
import net.minecraft.item.{BlockItem, Item, ItemGroup}
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import robosky.ether.EtherMod

object BlocksEther {
  val ETHER_GRASS: Block = register("ether_grass")(EtherGrassBlock)
  val ETHER_DIRT: Block = register("ether_dirt")(
    new Block(FabricBlockSettings.of(Material.ORGANIC).strength(0.5f, 0.5f)
      .sounds(BlockSoundGroup.GRAVEL).build()))
  val ETHER_STONE: Block = register("ether_stone")(
    new Block(FabricBlockSettings.of(Material.STONE).strength(1.5f, 6f)
      .sounds(BlockSoundGroup.STONE).build()))

  val ETHER_ORES: Map[EtherOreBlock.EtherOreType, EtherOreBlock] = EtherOreBlock.oreTypes
    .map(t => t -> register(s"${t.name}_ore")(new EtherOreBlock(t))).toMap

  def init(): Unit = {}

  private def register[B <: Block](name: String, item: Boolean = true, itemGroup: ItemGroup = EtherMod.ETHER_GROUP)(b: B): B = {
    val b1 = Registry.register[B](Registry.BLOCK, new Identifier("ether_dim", name), b)
    if (item) {
      Registry.register(Registry.ITEM, new Identifier("ether_dim", name), new BlockItem(b1, new Item.Settings().group(itemGroup)))
    }
    b1
  }
}
