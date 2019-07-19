package robosky.ether.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.{Block, GrassBlock, Material}
import net.minecraft.item.{BlockItem, Item, ItemGroup}
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object BlocksEther {
  val ETHER_STONE: Block = register(EtherGrassBlock,
    "ether_grass", itemGroup = ItemGroup.BUILDING_BLOCKS)
  val ETHER_DIRT: Block = register(new Block(FabricBlockSettings.of(Material.ORGANIC)
    .strength(0.5f, 0.5f).sounds(BlockSoundGroup.GRAVEL).build()), "ether_dirt",
    itemGroup = ItemGroup.BUILDING_BLOCKS)
  val ETHER_GRASS: Block = register(new Block(FabricBlockSettings.of(Material.STONE)
    .strength(1.5f, 6f).sounds(BlockSoundGroup.STONE).build()), "ether_stone",
    itemGroup = ItemGroup.BUILDING_BLOCKS)

  def init(): Unit = {}

  private def register[B <: Block](b: B, name: String, item: Boolean = true, itemGroup: ItemGroup = ItemGroup.MISC) = {
    val b1 = Registry.register(Registry.BLOCK, new Identifier("ether-dim", name), b)
    if (item) {
      Registry.register(Registry.ITEM, new Identifier("ether-dim", name), new BlockItem(b1, new Item.Settings().group(itemGroup)))
    }
    b1
  }
}
