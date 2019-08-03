package robosky.ether.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.registry.{CompostingChanceRegistry, FlammableBlockRegistry, FuelRegistry}
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block.Block.Settings
import net.minecraft.block._
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.{BlockItem, Item, ItemGroup}
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.tag.BlockTags
import net.minecraft.util.registry.Registry
import robosky.ether.UplandsMod
import robosky.ether.world.feature.trees.EtherSaplingGenerator

object BlockRegistry {
    val ETHER_GRASS: Block = register("uplander_grass")(EtherGrassBlock)
    val ETHER_DIRT: Block = register("uplander_dirt")(new Block(FabricBlockSettings.of(Material.ORGANIC)
        .strength(0.5f, 0.5f).sounds(BlockSoundGroup.GRAVEL).build()))

    val ETHER_STONE_SETTINGS: Block.Settings = FabricBlockSettings.of(Material.STONE)
        .strength(1.5f, 6f).sounds(BlockSoundGroup.STONE)
        .breakByTool(FabricToolTags.PICKAXES, 1).build

    val ETHER_STONE: Block = register("uplander_stone")(new Block(ETHER_STONE_SETTINGS))
    val ETHER_STONE_STAIRS: StairsBlock = register("uplander_stone_stairs")(new ModStairsBlock(ETHER_STONE, ETHER_STONE_SETTINGS))
    val ETHER_STONE_SLAB: SlabBlock = register("uplander_stone_slab")(new SlabBlock(ETHER_STONE_SETTINGS))
    val ETHER_STONE_WALL: WallBlock = register("uplander_stone_wall")(new WallBlock(ETHER_STONE_SETTINGS))

    val ETHER_STONE_BRICKS: Block = register("uplander_stone_bricks")(new Block(ETHER_STONE_SETTINGS))
    val ETHER_STONE_BRICK_STAIRS: StairsBlock = register("uplander_stone_brick_stairs")(new ModStairsBlock(ETHER_STONE_BRICKS, ETHER_STONE_SETTINGS))
    val ETHER_STONE_BRICK_SLAB: SlabBlock = register("uplander_stone_brick_slab")(new SlabBlock(ETHER_STONE_SETTINGS))
    val ETHER_STONE_BRICK_WALL: WallBlock = register("uplander_stone_brick_wall")(new WallBlock(ETHER_STONE_SETTINGS))

    val ETHER_ORES: Map[EtherOreBlock.EtherOreType, EtherOreBlock] = EtherOreBlock.oreTypes.map(t => t ->
        register(s"${t.name}_ore")(new EtherOreBlock(t))).toMap

    val skyroot: Block.Settings = FabricBlockSettings.of(Material.WOOD, MaterialColor.BROWN)
        .strength(2.0F, 2.0F).sounds(BlockSoundGroup.WOOD)
        .breakByTool(FabricToolTags.AXES, -1).build

    val SKYROOT_LOG: LogBlock = register("skyroot_log")(new LogBlock(MaterialColor.WOOD, skyroot))
    FuelRegistry.INSTANCE.add(SKYROOT_LOG, 300)
    FlammableBlockRegistry.getDefaultInstance.add(SKYROOT_LOG, 5, 5)

    val SKYROOT_WOOD: LogBlock = register("skyroot_wood")(new LogBlock(MaterialColor.WOOD, skyroot))
    FuelRegistry.INSTANCE.add(SKYROOT_WOOD, 300)
    FlammableBlockRegistry.getDefaultInstance.add(SKYROOT_WOOD, 5, 5)

    val SKYROOT_PLANKS: Block = register("skyroot_planks")(new Block(skyroot))
    FuelRegistry.INSTANCE.add(SKYROOT_PLANKS, 300)
    FlammableBlockRegistry.getDefaultInstance.add(SKYROOT_PLANKS, 20, 5)

    val SKYROOT_SLAB: SlabBlock = register("skyroot_slab")(new SlabBlock(skyroot))
    FuelRegistry.INSTANCE.add(SKYROOT_SLAB, 150)
    FlammableBlockRegistry.getDefaultInstance.add(SKYROOT_SLAB, 20, 5)

    val SKYROOT_STAIRS: StairsBlock = register("skyroot_stairs")(new ModStairsBlock(SKYROOT_PLANKS, skyroot))
    FuelRegistry.INSTANCE.add(SKYROOT_STAIRS, 300)
    FlammableBlockRegistry.getDefaultInstance.add(SKYROOT_STAIRS, 20, 5)

    val SKYROOT_FENCE: FenceBlock = register("skyroot_fence")(new FenceBlock(skyroot))
    FuelRegistry.INSTANCE.add(SKYROOT_FENCE, 300)
    FlammableBlockRegistry.getDefaultInstance.add(SKYROOT_FENCE, 20, 5)

    val SKYROOT_FENCE_GATE: FenceGateBlock = register("skyroot_fence_gate")(new FenceGateBlock(skyroot))
    FuelRegistry.INSTANCE.add(SKYROOT_FENCE_GATE, 300)
    FlammableBlockRegistry.getDefaultInstance.add(SKYROOT_FENCE_GATE, 20, 5)

    val SKYROOT_SAPLING: EtherSaplingBlock = register("skyroot_sapling")(new EtherSaplingBlock(
        EtherSaplingGenerator.SkyrootSaplingGenerator, FabricBlockSettings.of(Material.PLANT).noCollision.ticksRandomly
            .breakInstantly.sounds(BlockSoundGroup.GRASS).build()))
    CompostingChanceRegistry.INSTANCE.add(SKYROOT_SAPLING, 0.65f)
    FuelRegistry.INSTANCE.add(SKYROOT_WOOD, 100)
    val POTTED_SKYROOT_SAPLING = Registry.register(Registry.BLOCK, UplandsMod :/ "potted_skyroot_sapling",
        new FlowerPotBlock(SKYROOT_SAPLING, FabricBlockSettings.of(Material.PART).breakInstantly().build()))

    val skyrootLeavesSettings: Block.Settings = FabricBlockSettings
        .of(Material.LEAVES)
        .strength(0.2F, 0.2F)
        .ticksRandomly
        .sounds(BlockSoundGroup.GRASS).build()

    val RED_SKYROOT_LEAVES: LeavesBlock = register("red_skyroot_leaves")(new LeavesBlock(skyrootLeavesSettings))
    CompostingChanceRegistry.INSTANCE.add(RED_SKYROOT_LEAVES, 0.30f)
    FlammableBlockRegistry.getDefaultInstance.add(RED_SKYROOT_LEAVES, 60, 30)

    val ORANGE_SKYROOT_LEAVES: LeavesBlock = register("orange_skyroot_leaves")(new LeavesBlock(skyrootLeavesSettings))
    CompostingChanceRegistry.INSTANCE.add(ORANGE_SKYROOT_LEAVES, 0.30f)
    FlammableBlockRegistry.getDefaultInstance.add(ORANGE_SKYROOT_LEAVES, 60, 30)

    val YELLOW_SKYROOT_LEAVES: LeavesBlock = register("yellow_skyroot_leaves")(new LeavesBlock(skyrootLeavesSettings))
    CompostingChanceRegistry.INSTANCE.add(YELLOW_SKYROOT_LEAVES, 0.30f)
    FlammableBlockRegistry.getDefaultInstance.add(YELLOW_SKYROOT_LEAVES, 60, 30)

    val ETHER_BEACON: EtherBeaconBlock.type = register("uplander_beacon")(EtherBeaconBlock)

    val FLOWER_SETTINGS: Block.Settings = FabricBlockSettings.of(Material.LEAVES).breakInstantly().collidable(false).build()

    val CLOUD_DAISIES: FlowerBlock = register("cloud_daisies")(new CloudDaisiesBlock(FLOWER_SETTINGS))
    CompostingChanceRegistry.INSTANCE.add(CLOUD_DAISIES, 0.65f)
    FlammableBlockRegistry.getDefaultInstance.add(CLOUD_DAISIES, 100, 30)
    val POTTED_CLOUD_DAISIES = Registry.register(Registry.BLOCK, UplandsMod :/ "potted_cloud_daisies",
        new FlowerPotBlock(CLOUD_DAISIES, FabricBlockSettings.of(Material.PART).breakInstantly().build()))

    val CROP_SETTINGS = FabricBlockSettings.of(Material.PLANT)
        .breakInstantly()
        .collidable(false)
        .ticksRandomly()
        .build()

    val ZEPHYR_ONION_CROP_BLOCK = Registry.register(Registry.BLOCK, UplandsMod :/ "zephyr_onion_crop", new ZephyrOnionBlock(CROP_SETTINGS))

    def init(): Unit = {}

    def register[B <: Block](name: String, item: Boolean = true, itemGroup: ItemGroup = UplandsMod.GROUP)(b: B): B = {
        val b1 = Registry.register[B](Registry.BLOCK, UplandsMod :/ name, b)
        if (item) {
            Registry.register(Registry.ITEM, UplandsMod :/ name, new BlockItem(b1, new Item.Settings().group(itemGroup)))
        }
        b1
    }
}
