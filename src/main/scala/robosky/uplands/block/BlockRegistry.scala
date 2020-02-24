package robosky.uplands.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.registry.{CompostingChanceRegistry, FlammableBlockRegistry, FuelRegistry}
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block._
import net.minecraft.item.{BlockItem, Item, ItemGroup}
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.registry.Registry
import robosky.uplands.{HoeHacks, UplandsMod}
import robosky.uplands.block.bossroom._
import robosky.uplands.world.feature.plants.UplandsSaplingGenerator

object BlockRegistry {
    val UPLANDER_GRASS: Block = register("uplander_grass")(UplandsGrassBlock)
    val UPLANDER_DIRT: Block = register("uplander_dirt")(new Block(FabricBlockSettings.of(Material.EARTH)
        .strength(0.5f, 0.5f).sounds(BlockSoundGroup.GRAVEL).build()))

    val TALL_UPLANDS_GRASS: FernBlock = register("tall_uplands_grass")(new TallUplandsGrassBlock(FabricBlockSettings.of(
        Material.REPLACEABLE_PLANT).breakInstantly().noCollision().sounds(BlockSoundGroup.GRASS).build()))
    FlammableBlockRegistry.getDefaultInstance.add(TALL_UPLANDS_GRASS, 5, 5)
    CompostingChanceRegistry.INSTANCE.add(TALL_UPLANDS_GRASS, 0.3f)

    val UPLANDER_STONE_SETTINGS: Block.Settings = FabricBlockSettings.of(Material.STONE)
        .strength(1.5f, 6f).sounds(BlockSoundGroup.STONE)
        .breakByTool(FabricToolTags.PICKAXES, 1).build

    val UPLANDER_STONE: Block = register("uplander_stone")(new Block(UPLANDER_STONE_SETTINGS))
    val UPLANDER_STONE_STAIRS: StairsBlock = register("uplander_stone_stairs")(new ModStairsBlock(UPLANDER_STONE, UPLANDER_STONE_SETTINGS))
    val UPLANDER_STONE_SLAB: SlabBlock = register("uplander_stone_slab")(new SlabBlock(UPLANDER_STONE_SETTINGS))
    val UPLANDER_STONE_WALL: WallBlock = register("uplander_stone_wall")(new WallBlock(UPLANDER_STONE_SETTINGS))

    val UPLANDER_STONE_BRICKS: Block = register("uplander_stone_bricks")(new Block(UPLANDER_STONE_SETTINGS))
    val UPLANDER_STONE_BRICK_STAIRS: StairsBlock = register("uplander_stone_brick_stairs")(new ModStairsBlock(UPLANDER_STONE_BRICKS, UPLANDER_STONE_SETTINGS))
    val UPLANDER_STONE_BRICK_SLAB: SlabBlock = register("uplander_stone_brick_slab")(new SlabBlock(UPLANDER_STONE_SETTINGS))
    val UPLANDER_STONE_BRICK_WALL: WallBlock = register("uplander_stone_brick_wall")(new WallBlock(UPLANDER_STONE_SETTINGS))

    val UNBREAKABLE_UPLANDER_STONE_BRICKS: Block = register("unbreakable_uplander_stone_bricks")(new unbreakable.Block(UPLANDER_STONE_BRICKS))

    val UPLANDER_STONE_BUTTON: ModStoneButtonBlock = register("uplander_stone_button")(new ModStoneButtonBlock(FabricBlockSettings.of(Material.STONE)
      .strength(0.5f, 2.5f)
      .sounds(BlockSoundGroup.STONE)
      .noCollision()
      .build()))

    val aegisalt_bricks_settings: Block.Settings = FabricBlockSettings.of(Material.STONE)
      .strength(1.5f, 30f)
      .breakByTool(FabricToolTags.PICKAXES, 1)
      .build()

    val AEGISALT_BRICKS_BLOCK: Block = register("aegisalt_bricks")(new Block(aegisalt_bricks_settings))
    val AEGISALT_BRICK_SLAB_BLOCK: SlabBlock = register("aegisalt_brick_slab")(new SlabBlock(aegisalt_bricks_settings))
    val AEGISALT_BRICK_STAIRS_BLOCK: ModStairsBlock = register("aegisalt_brick_stairs")(new ModStairsBlock(AEGISALT_BRICKS_BLOCK, aegisalt_bricks_settings))
    val AEGISALT_PILLAR_BLOCK: PillarBlock = register("aegisalt_pillar")(new PillarBlock(aegisalt_bricks_settings))

    val BLOCK_OF_AEGISALT_BLOCK: Block = register("block_of_aegisalt")(new Block(aegisalt_bricks_settings))

    val UPLANDS_ORES: Map[UplandsOreBlock.UplandsOreType, UplandsOreBlock] = UplandsOreBlock.oreTypes.map(t => t ->
      register(s"${t.name}_ore")(new UplandsOreBlock(t))).toMap

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

    val SKYROOT_DOOR: DoorBlock = register("skyroot_door")(b = new ModDoorBlock(FabricBlockSettings.of(Material.WOOD)
      .strength(3.0f, 15f)
      .sounds(BlockSoundGroup.WOOD)
      .nonOpaque
      .build()))
    FuelRegistry.INSTANCE.add(SKYROOT_DOOR, 200)

    val SKYROOT_TRAPDOOR: TrapdoorBlock = register("skyroot_trapdoor")(new ModTrapdoorBlock(FabricBlockSettings.of(Material.WOOD)
        .strength(3.0f, 15f)
        .sounds(BlockSoundGroup.WOOD)
        .nonOpaque
        .build()))
    FuelRegistry.INSTANCE.add(SKYROOT_TRAPDOOR, 300)

    val SKYROOT_BUTTON: WoodButtonBlock = register("skyroot_button")(new ModWoodButtonBlock(FabricBlockSettings.of(Material.WOOD)
      .strength(0.5f, 2.5f)
      .sounds(BlockSoundGroup.WOOD)
      .noCollision()
      .build()))
    FuelRegistry.INSTANCE.add(SKYROOT_SLAB, 100)

    val SKYROOT_PRESSURE_PLATE: ModPressurePlateBlock = register("skyroot_pressure_plate")(new ModPressurePlateBlock(
        PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.of(Material.WOOD)
          .sounds(BlockSoundGroup.WOOD)
          .noCollision()
          .strength(0.5f, 2.5f)
          .build()
    ))
    FuelRegistry.INSTANCE.add(SKYROOT_PRESSURE_PLATE, 300)

    val SKYROOT_SAPLING: UplandsSaplingBlock = register("skyroot_sapling")(new UplandsSaplingBlock(
        UplandsSaplingGenerator.SkyrootSaplingGenerator, FabricBlockSettings.of(Material.PLANT).noCollision.ticksRandomly
            .breakInstantly.sounds(BlockSoundGroup.GRASS).build()))
    CompostingChanceRegistry.INSTANCE.add(SKYROOT_SAPLING, 0.65f)
    FuelRegistry.INSTANCE.add(SKYROOT_WOOD, 100)
    val POTTED_SKYROOT_SAPLING: Block = Registry.register(Registry.BLOCK, UplandsMod :/ "potted_skyroot_sapling",
        new FlowerPotBlock(SKYROOT_SAPLING, FabricBlockSettings.of(Material.PART).breakInstantly().build()))

    val skyrootLeavesSettings: Block.Settings = FabricBlockSettings
      .of(Material.LEAVES)
      .strength(0.2F, 0.2F)
      .ticksRandomly
      .nonOpaque
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

    val UPLANDER_BEACON: UplanderBeaconBlock.type = register("uplander_beacon")(UplanderBeaconBlock)

    val FLOWER_SETTINGS: Block.Settings = FabricBlockSettings.of(Material.LEAVES).breakInstantly().collidable(false).build()

    val CLOUD_DAISIES: FlowerBlock = register("cloud_daisies")(new CloudDaisiesBlock(FLOWER_SETTINGS))
    CompostingChanceRegistry.INSTANCE.add(CLOUD_DAISIES, 0.65f)
    FlammableBlockRegistry.getDefaultInstance.add(CLOUD_DAISIES, 100, 30)

    val POTTED_CLOUD_DAISIES: FlowerPotBlock = register("potted_cloud_daisies", item = false)(
        new FlowerPotBlock(CLOUD_DAISIES, FabricBlockSettings.of(Material.PART).breakInstantly().build()))

    val UPLANDER_FARMLAND: Block = register("uplander_farmland")(new UplanderFarmlandBlock(
        FabricBlockSettings.of(Material.EARTH)
      .strength(0.6f, 0.6f).sounds(BlockSoundGroup.GRAVEL).build()))
    HoeHacks.addHoeable(UPLANDER_DIRT, UPLANDER_FARMLAND.getDefaultState)
    HoeHacks.addHoeable(UPLANDER_GRASS, UPLANDER_FARMLAND.getDefaultState)

    val ZEPHYR_ONION_CROP_BLOCK: ZephyrOnionBlock = register("zephyr_onion_crop", item = false)(
      new ZephyrOnionBlock(
          FabricBlockSettings.of(Material.PLANT)
            .breakInstantly()
            .collidable(false)
            .ticksRandomly()
            .sounds(BlockSoundGroup.GRASS)
            .build()))

    val WATER_CHESTNUT_CROP_BLOCK: WaterChestnutBlock = register("water_chestnut_crop", item = false)(
      new WaterChestnutBlock(
        FabricBlockSettings.of(Material.PLANT)
            .breakInstantly()
            .noCollision()
            .ticksRandomly()
            .sounds(BlockSoundGroup.WET_GRASS)
            .build()))

    val LODESTONE: LodestoneBlock = register("lodestone")(new LodestoneBlock(
        FabricBlockSettings.of(Material.STONE)
          .sounds(BlockSoundGroup.STONE)
          .strength(6.0F, 8.0F)
          .breakByTool(FabricToolTags.PICKAXES, 2)
          .build()))

    val AZOTE_MUSHROOM: AzoteMushroomBlock = register("azote_mushroom")(new AzoteMushroomBlock(
        FabricBlockSettings.of(Material.ORGANIC)
          .sounds(BlockSoundGroup.NETHER_WART)
          .breakInstantly()
          .collidable(false)
          .ticksRandomly()
          .build()))

    val POTTED_AZOTE_MUSHROOM: FlowerPotBlock = register("potted_azote_mushroom", item = false)(
        new FlowerPotBlock(AZOTE_MUSHROOM, FabricBlockSettings.of(Material.PART).breakInstantly().build()))

    val AWOKEN_AZOTE_MUSHROOM: AwokenAzoteMushroomBlock = register("awoken_azote_mushroom")(
      new AwokenAzoteMushroomBlock(
      FabricBlockSettings.of(Material.ORGANIC)
        .sounds(BlockSoundGroup.NETHER_WART)
        .breakInstantly()
        .collidable(false)
        .lightLevel(15)
        .build()
    ))

    val POTTED_AWOKEN_AZOTE_MUSHROOM: FlowerPotBlock = register("potted_awoken_azote_mushroom", item = false)(
      new FlowerPotBlock(AWOKEN_AZOTE_MUSHROOM, FabricBlockSettings.of(Material.PART).lightLevel(15).breakInstantly().build())
    )

    val BOSS_CONTROL: ControlBlock = register("boss_control", itemGroup = null)(new ControlBlock())
    Registry.register(Registry.BLOCK_ENTITY, UplandsMod :/ "boss_control", ControlBlockEntity.TYPE)

    val BOSS_DOORWAY: DoorwayBlock = register("boss_doorway", itemGroup = null)(new DoorwayBlock(Block.Settings.copy(BOSS_CONTROL)))
    Registry.register(Registry.BLOCK_ENTITY, UplandsMod :/ "boss_doorway", DoorwayBlockEntity.TYPE)

    val MEGADUNGEON_ALTAR: Block = register("megadungeon_altar")(MegadungeonAltarBlock)

    val ACTIVE_MEGADUNGEON_ALTAR: Block = register("active_megadungeon_altar")(new ActiveAltarBlock(MEGADUNGEON_ALTAR))

    def init(): Unit = {}

    def register[B <: Block](name: String, item: Boolean = true, itemGroup: ItemGroup = UplandsMod.GROUP)(b: B): B = {
        val b1 = Registry.register[B](Registry.BLOCK, UplandsMod :/ name, b)
        if (item) {
            Registry.register(Registry.ITEM, UplandsMod :/ name, new BlockItem(b1, new Item.Settings().group(itemGroup)))
        }
        b1
    }
}
