package robosky.uplands.client.render.blockentity

import com.mojang.blaze3d.platform.GlStateManager

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.{BufferBuilder, Tessellator, VertexFormats, WorldRenderer}

import robosky.uplands.UplandsMod
import robosky.uplands.block.BlockRegistry
import robosky.uplands.block.bossroom.ControlBlockEntity

object ControlBlockEntityRenderer extends BlockEntityRenderer[ControlBlockEntity] {

  override def render(be: ControlBlockEntity, camX: Double, camY: Double, camZ: Double, partialTicks: Float, crackIdx: Int): Unit = {
    val player = MinecraftClient.getInstance.player
    if ((player.isCreativeLevelTwoOp || player.isSpectator) && player.inventory.contains(UplandsMod.BOSSROOM_TECHNICAL_TAG)) {
      val minX = camX + be.bounds.minX
      val minY = camY + be.bounds.minY
      val minZ = camZ + be.bounds.minZ
      val maxX = camX + be.bounds.maxX
      val maxY = camY + be.bounds.maxY
      val maxZ = camZ + be.bounds.maxZ
      val tez = Tessellator.getInstance()
      val buffer = tez.getBuffer()
      GlStateManager.disableFog()
      GlStateManager.disableLighting()
      GlStateManager.disableTexture()
      GlStateManager.enableBlend()
      GlStateManager.blendFuncSeparate(
        GlStateManager.SrcFactor.SRC_ALPHA.value,
        GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value,
        GlStateManager.SrcFactor.ONE.value,
        GlStateManager.DstFactor.ZERO.value)
      this.disableLightmap(true)
      GlStateManager.lineWidth(2.0F)
      buffer.begin(3, VertexFormats.POSITION_COLOR)
      WorldRenderer.drawBox(buffer, camX, camY, camZ, camX + 1, camY + 1, camZ + 1, 1.0f, 1.0f, 0.0f, 1.0f)
      WorldRenderer.drawBox(buffer, minX, minY, minZ, maxX, maxY, maxZ, 0.0f, 0.0f, 0.0f, 1.0f)
      WorldRenderer.drawBox(buffer, minX, minY, minZ, maxX, maxY, maxZ, 1.0f, 1.0f, 1.0f, 1.0f)
      tez.draw()
      this.disableLightmap(false)
      GlStateManager.lineWidth(1.0F)
      GlStateManager.enableLighting()
      GlStateManager.enableTexture()
      GlStateManager.enableDepthTest()
      GlStateManager.depthMask(true)
      GlStateManager.enableFog()
    }
  }
}
