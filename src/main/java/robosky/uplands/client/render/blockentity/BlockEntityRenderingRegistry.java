package robosky.uplands.client.render.blockentity;

import java.util.function.Function;

import robosky.uplands.block.bossroom.ControlBlockEntity;
import robosky.uplands.block.bossroom.DoorwayBlockEntity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public final class BlockEntityRenderingRegistry {

    private static <T extends BlockEntity> void register(BlockEntityType<T> tpe, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<T>> renderer) {
        BlockEntityRendererRegistry.INSTANCE.register(tpe, renderer);
    }

    public static void init() {
        register(ControlBlockEntity.TYPE, ControlBlockEntityRenderer::new);
        register(DoorwayBlockEntity.TYPE, DoorwayBlockEntityRenderer::new);
    }
}
