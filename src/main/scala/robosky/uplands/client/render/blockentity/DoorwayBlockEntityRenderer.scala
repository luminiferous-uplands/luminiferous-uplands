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
    val pos = be.getPos
    if (doorwayState.get(DoorwayBlock.STATE) == DoorwayState.BLOCKED) {
      renderBlockModel(be.mimicState, matrix, provider, var5)
    } else if (playerIsHoldingDoorwayItem) {
      if (be.mimicState.getRenderType == BlockRenderType.MODEL &&
          (forceRender || (be.getWorld.getTime / 20) % 2 == 0)) {
        renderBlockModel(be.mimicState, matrix, provider, var5)
      } else {
        renderBoxOutline(matrix, provider.getBuffer(RenderLayer.getLines), pos.getX, pos.getY, pos.getZ)
      }
    }
  }

  private def renderBlockModel(state: BlockState, matrix: MatrixStack, provider: VertexConsumerProvider, var5: Int): Unit = {
    if (state.getRenderType == BlockRenderType.MODEL) {
      val renderManager = MinecraftClient.getInstance.getBlockRenderManager
      val model = renderManager.getModel(state)
      // GlStateManager.pushMatrix()
      // GlStateManager.translated(camX, camY, camZ)
      matrix.push()
      // this.bindTextureInner(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
      // matrix.rotate(-90.0f, 0.0f, 1.0f, 0.0f) // fix with quaternion
      renderManager.getModelRenderer.render(matrix.peek, provider.getBuffer(TexturedRenderLayers.getEntitySolid), state, model, 1.0F, 1.0F, 1.0F, var5, OverlayTexture.DEFAULT_UV)
      matrix.pop()
      // GlStateManager.popMatrix()
    }
  }

  private def renderBoxOutline(matrix: MatrixStack, consumer: VertexConsumer, camX: Double, camY: Double, camZ: Double): Unit = {
    val minX = camX + (4.0 / 16.0) - 0.005
    val minY = camY + (4.0 / 16.0) - 0.005
    val minZ = camZ + (4.0 / 16.0) - 0.005
    val maxX = camX + (12.0 / 16.0) + 0.005
    val maxY = camY + (12.0 / 16.0) + 0.005
    val maxZ = camZ + (12.0 / 16.0) + 0.005
    // val tez = Tessellator.getInstance()
    // val buffer = tez.getBuffer()
    // GlStateManager.disableFog()
    // GlStateManager.disableLighting()
    // GlStateManager.disableTexture()
    // GlStateManager.enableBlend()
    // GlStateManager.blendFuncSeparate(
    //   GlStateManager.SrcFactor.SRC_ALPHA.value,
    //   GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value,
    //   GlStateManager.SrcFactor.ONE.value,
    //   GlStateManager.DstFactor.ZERO.value)
    // this.disableLightmap(true)
    // GlStateManager.lineWidth(2.0F)
    // buffer.begin(3, VertexFormats.POSITION_COLOR);
    matrix.push()
    WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 0.0f, 0.0f, 0.0f, 1.0f)
    WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 1.0f, 1.0f, 0.0f, 1.0f)
    matrix.pop()
    // tez.draw()
    // this.disableLightmap(false)
    // GlStateManager.lineWidth(1.0F)
    // GlStateManager.enableLighting()
    // GlStateManager.enableTexture()
    // GlStateManager.enableDepthTest()
    // GlStateManager.depthMask(true)
    // GlStateManager.enableFog()
  }

  private val doorwayItem = new ItemStack(BlockRegistry.BOSS_DOORWAY.asItem)

  private def playerIsHoldingDoorwayItem: Boolean = {
    val player = MinecraftClient.getInstance.player
    (player.isCreativeLevelTwoOp || player.isSpectator) && player.inventory.contains(UplandsMod.BOSSROOM_TECHNICAL_TAG)
  }
}
