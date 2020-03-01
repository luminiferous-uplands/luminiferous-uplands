package robosky.uplands.client.render.blockentity

import com.mojang.blaze3d.platform.GlStateManager

import net.minecraft.block.{BlockRenderType, Blocks, BlockState}
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.{BufferBuilder, OverlayTexture, RenderLayer, Tessellator, TexturedRenderLayers, VertexConsumer, VertexConsumerProvider, VertexFormats, WorldRenderer}
import net.minecraft.client.render.block.entity.{BlockEntityRenderDispatcher, BlockEntityRenderer}
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack

import robosky.uplands.UplandsMod
import robosky.uplands.block.BlockRegistry
import robosky.uplands.block.bossroom.{DoorwayBlock, DoorwayBlockEntity, DoorwayState}

class DoorwayBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher)
    extends BlockEntityRenderer[DoorwayBlockEntity](dispatcher) {

  override def render(be: DoorwayBlockEntity, partialTicks: Float, matrix: MatrixStack, provider: VertexConsumerProvider, var5: Int, var6: Int): Unit = {
    val doorwayState = if (be.hasWorld) be.getCachedState else BlockRegistry.BOSS_DOORWAY.getDefaultState
    val forceRender = be.getWorld.getTime - be.lastMimicUpdate <= 20
    if (doorwayState.get(DoorwayBlock.STATE) == DoorwayState.BLOCKED) {
      renderBlockModel(be.mimicState, matrix, provider, var5)
    } else if (playerIsHoldingDoorwayItem) {
      if (be.mimicState.getRenderType == BlockRenderType.MODEL &&
          (forceRender || (be.getWorld.getTime / 20) % 2 == 0)) {
        renderBlockModel(be.mimicState, matrix, provider, var5)
      } else {
        renderBoxOutline(matrix, provider.getBuffer(RenderLayer.getLines))
      }
    }
  }

  private def renderBlockModel(state: BlockState, matrix: MatrixStack, provider: VertexConsumerProvider, var5: Int): Unit = {
    if (state.getRenderType == BlockRenderType.MODEL) {
      val renderManager = MinecraftClient.getInstance.getBlockRenderManager
      val model = renderManager.getModel(state)
      matrix.push()
      renderManager.getModelRenderer.render(matrix.peek, provider.getBuffer(TexturedRenderLayers.getEntitySolid), state, model, 1.0F, 1.0F, 1.0F, var5, OverlayTexture.DEFAULT_UV)
      matrix.pop()
    }
  }

  private def renderBoxOutline(matrix: MatrixStack, consumer: VertexConsumer): Unit = {
    val minX = (4.0 / 16.0) - 0.005
    val minY = (4.0 / 16.0) - 0.005
    val minZ = (4.0 / 16.0) - 0.005
    val maxX = (12.0 / 16.0) + 0.005
    val maxY = (12.0 / 16.0) + 0.005
    val maxZ = (12.0 / 16.0) + 0.005
    matrix.push()
    WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 0.0f, 0.0f, 0.0f, 1.0f)
    WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 1.0f, 1.0f, 0.0f, 1.0f)
    matrix.pop()
  }

  private val doorwayItem = new ItemStack(BlockRegistry.BOSS_DOORWAY.asItem)

  private def playerIsHoldingDoorwayItem: Boolean = {
    val player = MinecraftClient.getInstance.player
    (player.isCreativeLevelTwoOp || player.isSpectator) && player.inventory.contains(UplandsMod.BOSSROOM_TECHNICAL_TAG)
  }
}
