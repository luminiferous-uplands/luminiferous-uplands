package robosky.uplands.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class BlockRenderingRegistry {

    public static void init() {
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutoutMipped(),
            BlockRegistry.TALL_UPLANDS_GRASS,
            BlockRegistry.RED_SKYROOT_SAPLING,
            BlockRegistry.ORANGE_SKYROOT_SAPLING,
            BlockRegistry.YELLOW_SKYROOT_SAPLING,
            BlockRegistry.SKYROOT_DOOR,
            BlockRegistry.SKYROOT_TRAPDOOR,
            BlockRegistry.RED_SKYROOT_LEAVES,
            BlockRegistry.ORANGE_SKYROOT_LEAVES,
            BlockRegistry.YELLOW_SKYROOT_LEAVES,
            BlockRegistry.CLOUD_DAISIES,
            BlockRegistry.POTTED_CLOUD_DAISIES,
            BlockRegistry.ZEPHYR_ONION_CROP_BLOCK,
            BlockRegistry.WATER_CHESTNUT_CROP_BLOCK,
            BlockRegistry.AZOTE_MUSHROOM,
            BlockRegistry.POTTED_AZOTE_MUSHROOM,
            BlockRegistry.AWOKEN_AZOTE_MUSHROOM,
            BlockRegistry.POTTED_AWOKEN_AZOTE_MUSHROOM,
            BlockRegistry.MEGADUNGEON_ALTAR,
            BlockRegistry.ACTIVE_MEGADUNGEON_ALTAR
        );
    }
}
