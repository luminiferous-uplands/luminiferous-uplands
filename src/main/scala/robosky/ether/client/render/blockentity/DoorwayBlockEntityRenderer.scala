package robosky.ether.client.render.blockentity

import com.mojang.blaze3d.platform.GlStateManager

import net.minecraft.block.{BlockRenderType, Blocks}
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.{BufferBuilder, Tessellator, VertexFormats, WorldRenderer}
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.texture.SpriteAtlasTexture

import robosky.ether.block.BlockRegistry
import robosky.ether.block.bossroom.{DoorwayBlock, DoorwayBlockEntity, DoorwayState}

object DoorwayBlockEntityRenderer extends BlockEntityRenderer[DoorwayBlockEntity] {

  override def render(be: DoorwayBlockEntity, camX: Double, camY: Double, camZ: Double, partialTicks: Float, crackIdx: Int): Unit = {
    val doorwayState = if (be.hasWorld) be.getCachedState else BlockRegistry.BOSS_DOORWAY.getDefaultState
    if (doorwayState.get(DoorwayBlock.STATE) == DoorwayState.BLOCKED) {
      val state = be.mimicState
      if (state.getRenderType == BlockRenderType.MODEL) {
        val renderManager = MinecraftClient.getInstance.getBlockRenderManager
        val model = renderManager.getModel(state)
        GlStateManager.pushMatrix()
        GlStateManager.translated(camX, camY, camZ)
        this.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        GlStateManager.rotatef(-90.0f, 0.0f, 1.0f, 0.0f)
        renderManager.getModelRenderer.render(model, state, 1.0f, true)
        GlStateManager.popMatrix()
      }
    } else if (MinecraftClient.getInstance.player.getMainHandStack.getItem == BlockRegistry.BOSS_DOORWAY.asItem) {
      val minX = camX + (4.0 / 16.0) - 0.005
      val minY = camY + (4.0 / 16.0) - 0.005
      val minZ = camZ + (4.0 / 16.0) - 0.005
      val maxX = camX + (12.0 / 16.0) + 0.005
      val maxY = camY + (12.0 / 16.0) + 0.005
      val maxZ = camZ + (12.0 / 16.0) + 0.005
      val tez = Tessellator.getInstance()
      val buffer = tez.getBufferBuilder()
      GlStateManager.disableFog()
      GlStateManager.disableLighting()
      GlStateManager.disableTexture()
      GlStateManager.enableBlend()
      GlStateManager.blendFuncSeparate(
        GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
        GlStateManager.SourceFactor.ONE,
        GlStateManager.DestFactor.ZERO)
      this.disableLightmap(true)
      GlStateManager.lineWidth(2.0F)
      buffer.begin(3, VertexFormats.POSITION_COLOR);
      WorldRenderer.buildBoxOutline(buffer, minX, minY, minZ, maxX, maxY, maxZ, 0.0f, 0.0f, 0.0f, 1.0f)
      WorldRenderer.buildBoxOutline(buffer, minX, minY, minZ, maxX, maxY, maxZ, 1.0f, 1.0f, 0.0f, 1.0f)
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
