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
      val minX: Double = be.bounds.minX
      val minY: Double = be.bounds.minY
      val minZ: Double = be.bounds.minZ
      val maxX: Double = be.bounds.maxX
      val maxY: Double = be.bounds.maxY
      val maxZ: Double = be.bounds.maxZ
      val consumer: VertexConsumer = provider.getBuffer(RenderLayer.getLines)
      WorldRenderer.drawBox(matrix, consumer, 0, 0, 0, 1, 1, 1, 1.0f, 1.0f, 0.0f, 1.0f)
      WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 0.0f, 0.0f, 0.0f, 1.0f)
      WorldRenderer.drawBox(matrix, consumer, minX, minY, minZ, maxX, maxY, maxZ, 1.0f, 1.0f, 1.0f, 1.0f)
      matrix.pop()
    }
  }
}
