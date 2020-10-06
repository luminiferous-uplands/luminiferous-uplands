package robosky.uplands;

import robosky.uplands.block.BlockRegistry;
import robosky.uplands.block.machine.MachineRegistry;
import robosky.uplands.block.machine.RecipeRegistry;
import robosky.uplands.entity.EntityRegistry;
import robosky.uplands.item.ItemRegistry;
import robosky.uplands.world.WorldRegistry;
import robosky.uplands.world.biome.BiomeRegistry;
import robosky.uplands.world.feature.FeatureRegistry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.class_5423;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class UplandsMod implements ModInitializer {

    public static final ItemGroup GROUP = FabricItemGroupBuilder
        .create(id("general"))
        .icon(() -> new ItemStack(BlockRegistry.UPLANDER_GRASS))
        .build();

    @Override
    public void onInitialize() {
        BlockRegistry.init();
        ItemRegistry.init();
        EntityRegistry.init();
        FeatureRegistry.init();
        BiomeRegistry.init();
        WorldRegistry.init();
        MachineRegistry.init();
        RecipeRegistry.init();
    }

    public static Identifier id(String name) {
        return new Identifier("luminiferous_uplands", name);
    }

    public static boolean isUplandsDimensionType(class_5423 world) {
        return world.getRegistryManager().getDimensionTypes().get(WorldRegistry.UPLANDS_DIMENSION_TYPE_KEY) == world.getDimension();
    }
}
