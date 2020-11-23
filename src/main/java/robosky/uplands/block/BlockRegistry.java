package robosky.uplands.block;

import java.util.Map;

import robosky.uplands.HoeHacks;
import robosky.uplands.UplandsMod;
import robosky.uplands.block.bossroom.ActiveAltarBlock;
import robosky.uplands.block.bossroom.ControlBlock;
import robosky.uplands.block.bossroom.ControlBlockEntity;
import robosky.uplands.block.bossroom.DoorwayBlock;
import robosky.uplands.block.bossroom.DoorwayBlockEntity;
import robosky.uplands.block.bossroom.MegadungeonAltarBlock;
import robosky.uplands.block.unbreakable.UnbreakableBlock;
import robosky.uplands.world.feature.plants.UplandsSaplingGenerator;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

@SuppressWarnings("unused")
public final class BlockRegistry {
    // Block Settings
    public static final Block.Settings UPLANDER_STONE_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(1.5f, 6f).sounds(BlockSoundGroup.STONE).breakByTool(FabricToolTags.PICKAXES, 1);
    public static final Block.Settings AEGISALT_BRICKS_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(1.5f, 30f).breakByTool(FabricToolTags.PICKAXES, 1);
    public static final Block.Settings SKYROOT_BLOCK_SETTINGS = FabricBlockSettings.of(Material.WOOD, MaterialColor.BROWN).strength(2.0F, 2.0F).sounds(BlockSoundGroup.WOOD).breakByTool(FabricToolTags.AXES, -1);
    public static final Block.Settings SKYROOT_LEAVES_SETTINGS = FabricBlockSettings.of(Material.LEAVES).strength(0.2F, 0.2F).ticksRandomly().nonOpaque().sounds(BlockSoundGroup.GRASS);
    public static final Block.Settings FLOWER_SETTINGS = FabricBlockSettings.of(Material.LEAVES).breakInstantly().collidable(false);

    // Uplander Organic Blocks
    public static final Block UPLANDER_GRASS = registerWithItem("uplander_grass", new UplandsGrassBlock());
    public static final Block UPLANDER_DIRT = registerWithItem("uplander_dirt", new Block(FabricBlockSettings.of(Material.SOIL).strength(0.5f, 0.5f).sounds(BlockSoundGroup.GRAVEL)));
    public static final Block UPLANDER_FARMLAND = registerWithItem("uplander_farmland", new UplanderFarmlandBlock(FabricBlockSettings.of(Material.SOIL).strength(0.6f, 0.6f).sounds(BlockSoundGroup.GRAVEL)));
    public static final Block TALL_UPLANDS_GRASS = registerWithItem("tall_uplands_grass", new TallUplandsGrassBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).breakInstantly().noCollision().sounds(BlockSoundGroup.GRASS)));

    // Uplander stone-like blocks
    public static final Block UPLANDER_STONE = registerWithItem("uplander_stone", new Block(UPLANDER_STONE_SETTINGS));
    public static final Block UPLANDER_STONE_STAIRS = registerWithItem("uplander_stone_stairs", new ModStairsBlock(UPLANDER_STONE, UPLANDER_STONE_SETTINGS));
    public static final Block UPLANDER_STONE_SLAB = registerWithItem("uplander_stone_slab", new SlabBlock(UPLANDER_STONE_SETTINGS));
    public static final Block UPLANDER_STONE_WALL = registerWithItem("uplander_stone_wall", new WallBlock(UPLANDER_STONE_SETTINGS));
    public static final Block UPLANDER_STONE_BUTTON = registerWithItem("uplander_stone_button", new ModStoneButtonBlock(FabricBlockSettings.of(Material.STONE).strength(0.5f, 2.5f).sounds(BlockSoundGroup.STONE).noCollision()));

    // Uplander stonebrick-like blocks
    public static final Block UPLANDER_STONE_BRICKS = registerWithItem("uplander_stone_bricks", new Block(UPLANDER_STONE_SETTINGS));
    public static final Block UPLANDER_STONE_BRICK_STAIRS = registerWithItem("uplander_stone_brick_stairs", new ModStairsBlock(UPLANDER_STONE_BRICKS, UPLANDER_STONE_SETTINGS));
    public static final Block UPLANDER_STONE_BRICK_SLAB = registerWithItem("uplander_stone_brick_slab", new SlabBlock(UPLANDER_STONE_SETTINGS));
    public static final Block UPLANDER_STONE_BRICK_WALL = registerWithItem("uplander_stone_brick_wall", new WallBlock(UPLANDER_STONE_SETTINGS));
    public static final Block UNBREAKABLE_UPLANDER_STONE_BRICKS = registerWithItem("unbreakable_uplander_stone_bricks", new UnbreakableBlock(UPLANDER_STONE_BRICKS));

    // Aegisalt blocks
    public static final Block AEGISALT_BRICKS = registerWithItem("aegisalt_bricks", new Block(AEGISALT_BRICKS_SETTINGS));
    public static final Block AEGISALT_BRICK_SLAB = registerWithItem("aegisalt_brick_slab", new SlabBlock(AEGISALT_BRICKS_SETTINGS));
    public static final Block AEGISALT_BRICK_STAIRS = registerWithItem("aegisalt_brick_stairs", new ModStairsBlock(AEGISALT_BRICKS, AEGISALT_BRICKS_SETTINGS));
    public static final Block AEGISALT_PILLAR_BLOCK = registerWithItem("aegisalt_pillar", new PillarBlock(AEGISALT_BRICKS_SETTINGS));
    public static final Block BLOCK_OF_AEGISALT = registerWithItem("block_of_aegisalt", new Block(AEGISALT_BRICKS_SETTINGS));

    // Skyroot
    public static final Block SKYROOT_LOG = registerWithItem("skyroot_log", new PillarBlock(SKYROOT_BLOCK_SETTINGS)); // BROWN side color
    public static final Block SKYROOT_WOOD = registerWithItem("skyroot_wood", new PillarBlock(SKYROOT_BLOCK_SETTINGS)); // WOOD side color
    public static final Block SKYROOT_PLANKS = registerWithItem("skyroot_planks", new Block(SKYROOT_BLOCK_SETTINGS));
    public static final Block SKYROOT_SLAB = registerWithItem("skyroot_slab", new SlabBlock(SKYROOT_BLOCK_SETTINGS));
    public static final Block SKYROOT_STAIRS = registerWithItem("skyroot_stairs", new ModStairsBlock(SKYROOT_PLANKS, SKYROOT_BLOCK_SETTINGS));
    public static final Block SKYROOT_FENCE = registerWithItem("skyroot_fence", new FenceBlock(SKYROOT_BLOCK_SETTINGS));

    public static final Block SKYROOT_FENCE_GATE = registerWithItem("skyroot_fence_gate", new FenceGateBlock(SKYROOT_BLOCK_SETTINGS));
    public static final Block SKYROOT_DOOR = registerWithItem("skyroot_door", new ModDoorBlock(FabricBlockSettings.of(Material.WOOD).strength(3.0f, 15f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block SKYROOT_TRAPDOOR = registerWithItem("skyroot_trapdoor", new ModTrapdoorBlock(FabricBlockSettings.of(Material.WOOD).strength(3.0f, 15f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block SKYROOT_BUTTON = registerWithItem("skyroot_button", new ModWoodButtonBlock(FabricBlockSettings.of(Material.WOOD).strength(0.5f, 2.5f).sounds(BlockSoundGroup.WOOD).noCollision()));
    public static final Block SKYROOT_PRESSURE_PLATE = registerWithItem("skyroot_pressure_plate", new ModPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).noCollision().strength(0.5f, 2.5f)));

    // Skywood shrubs
    public static final Block SKYROOT_SAPLING = registerWithItem("skyroot_sapling", new UplandsSaplingBlock(RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, UplandsMod.id("autumn_skyroot_tree")), FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS)));
    public static final Block POTTED_SKYROOT_SAPLING = register("potted_skyroot_sapling", new FlowerPotBlock(SKYROOT_SAPLING, FabricBlockSettings.of(Material.SUPPORTED).breakInstantly()));

    public static final Block RED_SKYROOT_LEAVES = registerWithItem("red_skyroot_leaves", new LeavesBlock(SKYROOT_LEAVES_SETTINGS));
    public static final Block ORANGE_SKYROOT_LEAVES = registerWithItem("orange_skyroot_leaves", new LeavesBlock(SKYROOT_LEAVES_SETTINGS));
    public static final Block YELLOW_SKYROOT_LEAVES = registerWithItem("yellow_skyroot_leaves", new LeavesBlock(SKYROOT_LEAVES_SETTINGS));

    // Flowers
    public static final Block CLOUD_DAISIES = registerWithItem("cloud_daisies", new CloudDaisiesBlock(FLOWER_SETTINGS));
    public static final Block POTTED_CLOUD_DAISIES = register("potted_cloud_daisies", new FlowerPotBlock(CLOUD_DAISIES, FabricBlockSettings.of(Material.SUPPORTED).breakInstantly()));

    // Crops
    public static final Block ZEPHYR_ONION_CROP_BLOCK = register("zephyr_onion_crop", new ZephyrOnionBlock(FabricBlockSettings.of(Material.PLANT).breakInstantly().collidable(false).ticksRandomly().sounds(BlockSoundGroup.GRASS)));
    public static final Block WATER_CHESTNUT_CROP_BLOCK = register("water_chestnut_crop", new WaterChestnutBlock(FabricBlockSettings.of(Material.PLANT).breakInstantly().noCollision().ticksRandomly().sounds(BlockSoundGroup.WET_GRASS)));

    // Shrooms
    public static final Block AZOTE_MUSHROOM = registerWithItem("azote_mushroom", new AzoteMushroomBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC).sounds(BlockSoundGroup.NETHER_WART).breakInstantly().collidable(false).ticksRandomly()));
    public static final Block AWOKEN_AZOTE_MUSHROOM = registerWithItem("awoken_azote_mushroom", new AwokenAzoteMushroomBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC).sounds(BlockSoundGroup.NETHER_WART).breakInstantly().collidable(false).luminance(15)));
    public static final Block POTTED_AZOTE_MUSHROOM = register("potted_azote_mushroom", new FlowerPotBlock(AZOTE_MUSHROOM, FabricBlockSettings.of(Material.SUPPORTED).breakInstantly()));
    public static final Block POTTED_AWOKEN_AZOTE_MUSHROOM = register("potted_awoken_azote_mushroom", new FlowerPotBlock(AWOKEN_AZOTE_MUSHROOM, FabricBlockSettings.of(Material.SUPPORTED).luminance(15).breakInstantly()));

    // Boss blocks
    public static final Block BOSS_CONTROL = registerWithItem("boss_control", new ControlBlock(), null);
    public static final Block BOSS_DOORWAY = registerWithItem("boss_doorway", new DoorwayBlock(FabricBlockSettings.copy(BOSS_CONTROL).dynamicBounds().nonOpaque()), null);

    static {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, UplandsMod.id("boss_control"), ControlBlockEntity.TYPE);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, UplandsMod.id("boss_doorway"), DoorwayBlockEntity.TYPE);
    }

    // Altars
    public static final Block MEGADUNGEON_ALTAR = registerWithItem("megadungeon_altar", new MegadungeonAltarBlock());
    public static final Block ACTIVE_MEGADUNGEON_ALTAR = registerWithItem("active_megadungeon_altar", new ActiveAltarBlock(MEGADUNGEON_ALTAR));

    // Misc
    public static final Block LODESTONE = registerWithItem("lodestone", new LodestoneBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(6.0F, 8.0F).breakByTool(FabricToolTags.PICKAXES, 2)));
    public static final Block UPLANDER_BEACON = registerWithItem("uplander_beacon", new UplanderBeaconBlock());

    public static final Map<UplandsOreBlock.UplandsOreType, UplandsOreBlock> UPLANDS_ORES = createOreTypes();
    private static boolean lock;

    // TODO: Register machine blocks elsewhere so this isn't public?
    public static <B extends Block> B registerWithItem(String name, B block) {
        return registerWithItem(name, block, UplandsMod.GROUP);
    }

    private static <B extends Block> B registerWithItem(String name, B block, ItemGroup group) {
        final B b = register(name, block);
        Registry.register(Registry.ITEM, UplandsMod.id(name), new BlockItem(b, new Item.Settings().group(group)));
        return b;
    }

    private static <B extends Block> B register(String name, B block) {
        return Registry.register(Registry.BLOCK, UplandsMod.id(name), block);
    }

    /**
     * @deprecated should be passsed inside of the block constructor in future.
     */
    @Deprecated
    private static Block flammable(Block block, int burn, int spread) {
        FlammableBlockRegistry.getDefaultInstance().add(block, burn, spread);
        return block;
    }

    /**
     * @deprecated should be passed inside of the block constructor in future.
     */
    @Deprecated
    private static Block compostable(Block block, float chance) {
        CompostingChanceRegistry.INSTANCE.add(block, chance);
        return block;
    }

    /**
     * @deprecated should be passsed inside of the block constructor in future.
     */
    @Deprecated
    private static Block fuel(Block block, int i) {
        FuelRegistry.INSTANCE.add(block, i);
        return block;
    }

    /**
     * @deprecated should be passsed inside of the block constructor in future.
     */
    @Deprecated
    private static Block hoeable(Block farmlandBlock, Block... from) {
        for(Block block : from) {
            HoeHacks.addHoeable(block, farmlandBlock.getDefaultState());
        }

        return farmlandBlock;
    }

    public static void init() {
        if(lock) {
            return;
        }

        lock = true;

        // TODO: Find a nice way to inline this all into block constructors
        hoeable(UPLANDER_FARMLAND, UPLANDER_DIRT, UPLANDER_GRASS);

        compostable(TALL_UPLANDS_GRASS, 0.3F);
        flammable(TALL_UPLANDS_GRASS, 5, 5);

        fuel(SKYROOT_LOG, 300);
        flammable(SKYROOT_LOG, 5, 5);

        fuel(SKYROOT_WOOD, 300);
        flammable(SKYROOT_WOOD, 5, 5);

        fuel(SKYROOT_PLANKS, 300);
        flammable(SKYROOT_PLANKS, 20, 5);

        fuel(SKYROOT_SLAB, 150);
        flammable(SKYROOT_SLAB, 20, 5);

        fuel(SKYROOT_STAIRS, 300);
        flammable(SKYROOT_STAIRS, 20, 5);

        fuel(SKYROOT_FENCE, 300);
        flammable(SKYROOT_FENCE, 20, 5);

        fuel(SKYROOT_FENCE_GATE, 300);
        flammable(SKYROOT_FENCE_GATE, 20, 5);

        fuel(SKYROOT_DOOR, 200);

        fuel(SKYROOT_TRAPDOOR, 300);

        fuel(SKYROOT_BUTTON, 100);

        fuel(SKYROOT_PRESSURE_PLATE, 300);

        compostable(SKYROOT_SAPLING, 0.65F);
        fuel(SKYROOT_SAPLING, 100);

        flammable(RED_SKYROOT_LEAVES, 60, 30);
        compostable(RED_SKYROOT_LEAVES, 0.3F);

        flammable(ORANGE_SKYROOT_LEAVES, 60, 30);
        compostable(ORANGE_SKYROOT_LEAVES, 0.3F);

        flammable(YELLOW_SKYROOT_LEAVES, 60, 30);
        compostable(YELLOW_SKYROOT_LEAVES, 0.3F);

        flammable(CLOUD_DAISIES, 100, 30);
        compostable(CLOUD_DAISIES, 0.65F);
    }

    private static Map<UplandsOreBlock.UplandsOreType, UplandsOreBlock> createOreTypes() {
        UplandsOreBlock.oreTypes.forEach((t, b) -> BlockRegistry.registerWithItem(t.name.toLowerCase() + "_ore", b));
        return UplandsOreBlock.oreTypes;
    }

    private BlockRegistry() {
    }
}
