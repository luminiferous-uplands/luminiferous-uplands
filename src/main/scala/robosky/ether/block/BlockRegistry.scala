package robosky.ether.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.registry.{CompostingChanceRegistry, FlammableBlockRegistry}
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block._
import net.minecraft.item.{BlockItem, Item, ItemGroup}
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.registry.Registry
import robosky.ether.UplandsMod
import robosky.ether.world.feature.trees.EtherSaplingGenerator

object BlockRegistry {
    val ETHER_GRASS: Block = register("ether_grass")(EtherGrassBlock)
    val ETHER_DIRT: Block = register("ether_dirt")(new Block(FabricBlockSettings.of(Material.ORGANIC)
        .strength(0.5f, 0.5f).sounds(BlockSoundGroup.GRAVEL).build()))

    val ETHER_STONE_SETTINGS: Block.Settings = FabricBlockSettings.of(Material.STONE)
        .strength(1.5f, 6f).sounds(BlockSoundGroup.STONE)
        .breakByTool(FabricToolTags.PICKAXES, 1).build;

    val ETHER_STONE: Block = register("ether_stone")(new Block(ETHER_STONE_SETTINGS));
    val ETHER_STONE_STAIRS: StairsBlock = register("ether_stone_stairs")(new ModStairsBlock(ETHER_STONE, ETHER_STONE_SETTINGS));
    val ETHER_STONE_SLAB: SlabBlock = register("ether_stone_slab")(new SlabBlock(ETHER_STONE_SETTINGS));
    val ETHER_STONE_WALL: WallBlock = register("ether_stone_wall")(new WallBlock(ETHER_STONE_SETTINGS));

    val ETHER_STONE_BRICKS: Block = register("ether_stone_bricks")(new Block(ETHER_STONE_SETTINGS));
    val ETHER_STONE_BRICK_STAIRS: StairsBlock = register("ether_stone_brick_stairs")(new ModStairsBlock(ETHER_STONE_BRICKS, ETHER_STONE_SETTINGS));
    val ETHER_STONE_BRICK_SLAB: SlabBlock = register("ether_stone_brick_slab")(new SlabBlock(ETHER_STONE_SETTINGS));
    val ETHER_STONE_BRICK_WALL: WallBlock = register("ether_stone_brick_wall")(new WallBlock(ETHER_STONE_SETTINGS));

    val ETHER_ORES: Map[EtherOreBlock.EtherOreType, EtherOreBlock] = EtherOreBlock.oreTypes.map(t => t ->
        register(s"${t.name}_ore")(new EtherOreBlock(t))).toMap

    val skyroot: Block.Settings = FabricBlockSettings.of(Material.WOOD, MaterialColor.BROWN)
        .strength(2.0F, 2.0F).sounds(BlockSoundGroup.WOOD)
        .breakByTool(FabricToolTags.AXES, -1).build
    val SKYROOT_LOG: LogBlock = register("skyroot_log")(new LogBlock(MaterialColor.WOOD, skyroot))
    val SKYROOT_WOOD: LogBlock = register("skyroot_wood")(new LogBlock(MaterialColor.WOOD, skyroot));
    val SKYROOT_PLANKS: Block = register("skyroot_planks")(new Block(skyroot))
    val SKYROOT_SLAB: SlabBlock = register("skyroot_slab")(new SlabBlock(skyroot))
    val SKYROOT_STAIRS: StairsBlock = register("skyroot_stairs")(new ModStairsBlock(SKYROOT_PLANKS, skyroot))
    val SKYROOT_FENCE: FenceBlock = register("skyroot_fence")(new FenceBlock(skyroot))
    val SKYROOT_FENCE_GATE: FenceGateBlock = register("skyroot_fence_gate")(new FenceGateBlock(skyroot))

    val SKYROOT_SAPLING: EtherSaplingBlock = register("skyroot_sapling")(new EtherSaplingBlock(
        EtherSaplingGenerator.SkyrootSaplingGenerator, FabricBlockSettings.of(Material.PLANT).noCollision.ticksRandomly
            .breakInstantly.sounds(BlockSoundGroup.GRASS).build()))

    CompostingChanceRegistry.INSTANCE.add(SKYROOT_SAPLING, 0.65f);

    val skyrootLeavesSettings: Block.Settings = FabricBlockSettings
        .of(Material.LEAVES)
        .strength(0.2F, 0.2F)
        .ticksRandomly
        .sounds(BlockSoundGroup.GRASS).build()

    val RED_SKYROOT_LEAVES: LeavesBlock = register("red_skyroot_leaves")(new LeavesBlock(skyrootLeavesSettings));
    val ORANGE_SKYROOT_LEAVES: LeavesBlock = register("orange_skyroot_leaves")(new LeavesBlock(skyrootLeavesSettings));
    val YELLOW_SKYROOT_LEAVES: LeavesBlock = register("yellow_skyroot_leaves")(new LeavesBlock(skyrootLeavesSettings));

    CompostingChanceRegistry.INSTANCE.add(RED_SKYROOT_LEAVES, 0.30f);
    CompostingChanceRegistry.INSTANCE.add(ORANGE_SKYROOT_LEAVES, 0.30f);
    CompostingChanceRegistry.INSTANCE.add(YELLOW_SKYROOT_LEAVES, 0.30f);

    FlammableBlockRegistry.getDefaultInstance().add(SKYROOT_LOG, 5, 5);
    FlammableBlockRegistry.getDefaultInstance().add(SKYROOT_WOOD, 5, 5);
    FlammableBlockRegistry.getDefaultInstance().add(SKYROOT_PLANKS, 20, 5);
    FlammableBlockRegistry.getDefaultInstance().add(SKYROOT_SLAB, 20, 5);
    FlammableBlockRegistry.getDefaultInstance().add(SKYROOT_STAIRS, 20, 5);
    FlammableBlockRegistry.getDefaultInstance().add(RED_SKYROOT_LEAVES, 60, 30);
    FlammableBlockRegistry.getDefaultInstance().add(ORANGE_SKYROOT_LEAVES, 60, 30);
    FlammableBlockRegistry.getDefaultInstance().add(YELLOW_SKYROOT_LEAVES, 60, 30);
    FlammableBlockRegistry.getDefaultInstance().add(SKYROOT_FENCE, 20, 5);
    FlammableBlockRegistry.getDefaultInstance().add(SKYROOT_FENCE_GATE, 20, 5);

    val ETHER_BEACON: EtherBeaconBlock.type = register("ether_beacon")(EtherBeaconBlock)

    def init(): Unit = {}

    def register[B <: Block](name: String, item: Boolean = true, itemGroup: ItemGroup = UplandsMod.GROUP)(b: B): B = {
        val b1 = Registry.register[B](Registry.BLOCK, UplandsMod :/ name, b)
        if (item) {
            Registry.register(Registry.ITEM, UplandsMod :/ name, new BlockItem(b1, new Item.Settings().group(itemGroup)))
        }
        b1
    }
}
