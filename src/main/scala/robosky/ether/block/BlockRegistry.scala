package robosky.ether.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block._
import net.minecraft.item.{BlockItem, Item, ItemGroup}
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import robosky.ether.EtherMod
import robosky.ether.world.feature.trees.EtherSaplingGenerator

object BlockRegistry {
  val ETHER_GRASS: Block = register("ether_grass")(EtherGrassBlock)
  val ETHER_DIRT: Block = register("ether_dirt")(
    new Block(FabricBlockSettings.of(Material.ORGANIC).strength(0.5f, 0.5f)
      .sounds(BlockSoundGroup.GRAVEL).build()))
  val ETHER_STONE: Block = register("ether_stone")(
    new Block(FabricBlockSettings.of(Material.STONE).strength(1.5f, 6f)
      .sounds(BlockSoundGroup.STONE).build()))

  val ETHER_ORES: Map[EtherOreBlock.EtherOreType, EtherOreBlock] = EtherOreBlock.oreTypes
    .map(t => t -> register(s"${t.name}_ore")(new EtherOreBlock(t))).toMap

  val skyroot: Block.Settings = FabricBlockSettings.of(Material.WOOD, MaterialColor.BROWN)
    .strength(2.0F, 2.0F).sounds(BlockSoundGroup.WOOD).breakByTool(FabricToolTags.AXES, -1).build
  val SKYROOT_LOG: LogBlock = register("skyroot_log")(new LogBlock(MaterialColor.WOOD, skyroot))
  val SKYROOT_PLANKS: Block = register("skyroot_planks")(new Block(skyroot))
  val SKYROOT_SLAB: SlabBlock = register("skyroot_slab")(new SlabBlock(skyroot))
  val SKYROOT_STAIRS: StairsBlock = register("skyroot_stairs")(new ModStairsBlock(SKYROOT_PLANKS, skyroot))
  val SKYROOT_LEAVES: LeavesBlock = register("skyroot_leaves")(new LeavesBlock(FabricBlockSettings.of(Material.LEAVES)
    .strength(0.2F, 0.2F).ticksRandomly.sounds(BlockSoundGroup.GRASS).build()))
  val SKYROOT_SAPLING: EtherSaplingBlock = register("skyroot_sapling")(new EtherSaplingBlock(
    EtherSaplingGenerator.SkyrootSaplingGenerator, FabricBlockSettings.of(Material.PLANT).noCollision.ticksRandomly
      .breakInstantly.sounds(BlockSoundGroup.GRASS).build()))


  val ETHER_BEACON: EtherBeaconBlock.type = register("ether_beacon")(EtherBeaconBlock)


  def init(): Unit = {}

  private def register[B <: Block](name: String, item: Boolean = true, itemGroup: ItemGroup = EtherMod.ETHER_GROUP)(b: B): B = {
    val b1 = Registry.register[B](Registry.BLOCK, new Identifier("ether_dim", name), b)
    if (item) {
      Registry.register(Registry.ITEM, new Identifier("ether_dim", name), new BlockItem(b1, new Item.Settings().group(itemGroup)))
    }
    b1
  }
}
