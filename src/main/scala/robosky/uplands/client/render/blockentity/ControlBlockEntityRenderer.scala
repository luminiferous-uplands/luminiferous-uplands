package robosky.uplands.client.render.blockentity

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.{BlockEntityRenderDispatcher, BlockEntityRenderer}
import net.minecraft.client.render.{BufferBuilder, RenderLayer, Tessellator, VertexConsumer, VertexConsumerProvider, VertexFormats, WorldRenderer}
import net.minecraft.client.util.math.MatrixStack

import robosky.uplands.UplandsMod
import robosky.uplands.block.BlockRegistry
import robosky.uplands.block.bossroom.ControlBlockEntity

class ControlBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher)
    extends BlockEntityRenderer[ControlBlockEntity](dispatcher) {
  override def render(be: ControlBlockEntity, partialTicks: Float, matrix: MatrixStack, provider: VertexConsumerProvider, var5: Int, var6: Int): Unit = {
    val player = MinecraftClient.getInstance.player
    if ((player.isCreativeLevelTwoOp || player.isSpectator) && player.inventory.contains(UplandsMod.BOSSROOM_TECHNICAL_TAG)) {
      matrix.push()
      val pos = be.getPos
      val minX: Double = pos.getX + be.bounds.minX
      val minY: Double = pos.getY + be.bounds.minY
      val minZ: Double = pos.getZ + be.bounds.minZ
      val maxX: Double = pos.getX + be.bounds.maxX
      val maxY: Double = pos.getY + be.bounds.maxY
      val maxZ: Double = pos.getZ + be.bounds.maxZ
      val tez = Tessellator.getInstance()
      val buffer = tez.getBuffer()
      val consumer: VertexConsumer = provider.getBuffer(RenderLayer.getLines)
      // RenderSystem.disableFog()
      // RenderSystem.disableLighting()
      // RenderSystem.disableTexture()
      // RenderSystem.enableBlend()
      // RenderSystem.blendFuncSeparate(
      //   GlStateManager.SrcFactor.SRC_ALPHA.value,
      //   GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value,
      //   GlStateManager.SrcFactor.ONE.value,
      //   GlStateManager.DstFactor.ZERO.value)
      // RenderSystem.disableLighting()
      // RenderSystem.lineWidth(2.0F)
      // buffer.begin(3, VertexFormats.POSITION_COLOR)
      WorldRenderer.drawBox(matrix, consumer, pos.getX, pos.getY, pos.getZ, pos.getX + 1, pos.getY + 1, pos.getZ + 1, 1.0f, 1.0f, 0.0f, 1.0f)
      WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 0.0f, 0.0f, 0.0f, 1.0f)
      WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 1.0f, 1.0f, 1.0f, 1.0f)
      // tez.draw()
      // RenderSystem.enableLighting()
      // RenderSystem.lineWidth(1.0F)
      // RenderSystem.enableLighting()
      // RenderSystem.enableTexture()
      // RenderSystem.enableDepthTest()
      // RenderSystem.depthMask(true)
      // RenderSystem.enableFog()
      matrix.pop()
    }
  }
}
