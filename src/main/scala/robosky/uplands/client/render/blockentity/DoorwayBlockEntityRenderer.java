package robosky.uplands.client.render.blockentity;

import robosky.uplands.UplandsMod;
import robosky.uplands.block.BlockRegistry;
import robosky.uplands.block.bossroom.DoorwayBlock;
import robosky.uplands.block.bossroom.DoorwayBlockEntity;
import robosky.uplands.block.bossroom.DoorwayState;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class DoorwayBlockEntityRenderer extends BlockEntityRenderer<DoorwayBlockEntity> {

    public DoorwayBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(DoorwayBlockEntity be, float partialTicks, MatrixStack matrix, VertexConsumerProvider provider, int var5, int var6) {
        BlockState doorwayState = be.hasWorld() ? be.getCachedState() : BlockRegistry.BOSS_DOORWAY().getDefaultState();
        boolean forceRender = be.getWorld().getTime() - be.lastMimicUpdate() <= 20;
        if (doorwayState.get(DoorwayBlock.STATE()) == DoorwayState.BLOCKED) {
            renderBlockModel(be.mimicState(), matrix, provider, var5);
        } else if (playerIsHoldingDoorwayItem()) {
            if (be.mimicState().getRenderType() == BlockRenderType.MODEL &&
                (forceRender || (be.getWorld().getTime() / 20) % 2 == 0)) {
                renderBlockModel(be.mimicState(), matrix, provider, var5);
            } else {
                renderBoxOutline(matrix, provider.getBuffer(RenderLayer.getLines()));
            }
        }
    }

    private void renderBlockModel(BlockState state, MatrixStack matrix, VertexConsumerProvider provider, int var5) {
        if (state.getRenderType() == BlockRenderType.MODEL) {
            BlockRenderManager renderManager = MinecraftClient.getInstance().getBlockRenderManager();
            BakedModel model = renderManager.getModel(state);
            matrix.push();
            renderManager.getModelRenderer().render(matrix.peek(), provider.getBuffer(TexturedRenderLayers.getEntitySolid()), state, model, 1.0F, 1.0F, 1.0F, var5, OverlayTexture.DEFAULT_UV);
            matrix.pop();
        }
    }

    private void renderBoxOutline(MatrixStack matrix, VertexConsumer consumer) {
        double minX = (4.0 / 16.0) - 0.005;
        double minY = (4.0 / 16.0) - 0.005;
        double minZ = (4.0 / 16.0) - 0.005;
        double maxX = (12.0 / 16.0) + 0.005;
        double maxY = (12.0 / 16.0) + 0.005;
        double maxZ = (12.0 / 16.0) + 0.005;
        matrix.push();
        WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 0.0f, 0.0f, 0.0f, 1.0f);
        WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 1.0f, 1.0f, 0.0f, 1.0f);
        matrix.pop();
    }

    private static final ItemStack doorwayItem = new ItemStack(BlockRegistry.BOSS_DOORWAY().asItem());

    private boolean playerIsHoldingDoorwayItem() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        return (player.isCreativeLevelTwoOp() || player.isSpectator()) && player.inventory.contains(UplandsMod.BOSSROOM_TECHNICAL_TAG());
    }
}
