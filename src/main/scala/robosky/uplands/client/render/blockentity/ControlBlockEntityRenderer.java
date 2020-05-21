package robosky.uplands.client.render.blockentity;

import robosky.uplands.block.bossroom.ControlBlockEntity;
import robosky.uplands.item.UplandsItemTags;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public class ControlBlockEntityRenderer extends BlockEntityRenderer<ControlBlockEntity> {

    public ControlBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ControlBlockEntity be, float partialTicks, MatrixStack matrix, VertexConsumerProvider provider, int var5, int var6) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && (player.isCreativeLevelTwoOp() || player.isSpectator()) && player.inventory.contains(UplandsItemTags.BOSSROOM_TECHNICAL)) {
            matrix.push();
            double minX = be.bounds().minX();
            double minY = be.bounds().minY();
            double minZ = be.bounds().minZ();
            double maxX = be.bounds().maxX();
            double maxY = be.bounds().maxY();
            double maxZ = be.bounds().maxZ();
            VertexConsumer consumer = provider.getBuffer(RenderLayer.getLines());
            WorldRenderer.drawBox(matrix, consumer, 0, 0, 0, 1, 1, 1, 1.0f, 1.0f, 0.0f, 1.0f);
            WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 0.0f, 0.0f, 0.0f, 1.0f);
            WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 1.0f, 1.0f, 1.0f, 1.0f);
            matrix.pop();
        }
    }
}
