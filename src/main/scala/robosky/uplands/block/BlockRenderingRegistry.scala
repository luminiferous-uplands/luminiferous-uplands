package robosky.uplands.block

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap

import net.minecraft.client.render.RenderLayer

import BlockRegistry._

object BlockRenderingRegistry {

    def init(): Unit = {
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutoutMipped,
            TALL_UPLANDS_GRASS,
            SKYROOT_SAPLING,
            SKYROOT_DOOR,
            SKYROOT_TRAPDOOR,
            RED_SKYROOT_LEAVES,
            ORANGE_SKYROOT_LEAVES,
            YELLOW_SKYROOT_LEAVES,
            CLOUD_DAISIES,
            POTTED_CLOUD_DAISIES,
            ZEPHYR_ONION_CROP_BLOCK,
            WATER_CHESTNUT_CROP_BLOCK,
            AZOTE_MUSHROOM,
            POTTED_AZOTE_MUSHROOM,
            AWOKEN_AZOTE_MUSHROOM,
            POTTED_AWOKEN_AZOTE_MUSHROOM,
            MEGADUNGEON_ALTAR,
            ACTIVE_MEGADUNGEON_ALTAR
        )
    }
}
