package robosky.uplands.client.render.blockentity

import com.mojang.blaze3d.platform.GlStateManager

import net.minecraft.block.{BlockRenderType, Blocks}
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.{BufferBuilder, Tessellator, VertexFormats, WorldRenderer}
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.item.ItemStack

import robosky.uplands.UplandsMod
import robosky.uplands.block.BlockRegistry
import robosky.uplands.block.bossroom.{DoorwayBlock, DoorwayBlockEntity, DoorwayState}

object DoorwayBlockEntityRenderer extends BlockEntityRenderer[DoorwayBlockEntity] {

  override def render(be: DoorwayBlockEntity, camX: Double, camY: Double, camZ: Double, partialTicks: Float, crackIdx: Int): Unit = {
    val doorwayState = if (be.hasWorld) be.getCachedState else BlockRegistry.BOSS_DOORWAY.getDefaultState
    val forceRender = be.getWorld.getTime - be.lastMimicUpdate <= 20
    if (doorwayState.get(DoorwayBlock.STATE) == DoorwayState.BLOCKED) {
      renderBlockModel(be, camX, camY, camZ)
    } else if (playerIsHoldingDoorwayItem) {
      if (be.mimicState.getRenderType == BlockRenderType.MODEL &&
          (forceRender || (be.getWorld.getTime / 20) % 2 == 0)) {
        renderBlockModel(be, camX, camY, camZ)
      } else {
        renderBoxOutline(camX, camY, camZ)
      }
    }
  }

  private def renderBlockModel(be: DoorwayBlockEntity, camX: Double, camY: Double, camZ: Double): Unit = {
    val state = be.mimicState
    if (state.getRenderType == BlockRenderType.MODEL) {
      val renderManager = MinecraftClient.getInstance.getBlockRenderManager
      val model = renderManager.getModel(state)
      GlStateManager.pushMatrix()
      GlStateManager.translated(camX, camY, camZ)
      this.bindTextureInner(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
      GlStateManager.rotatef(-90.0f, 0.0f, 1.0f, 0.0f)
      renderManager.getModelRenderer.render(model, state, 1.0f, true)
      GlStateManager.popMatrix()
    }
  }

  private def renderBoxOutline(camX: Double, camY: Double, camZ: Double): Unit = {
    val minX = camX + (4.0 / 16.0) - 0.005
    val minY = camY + (4.0 / 16.0) - 0.005
    val minZ = camZ + (4.0 / 16.0) - 0.005
    val maxX = camX + (12.0 / 16.0) + 0.005
    val maxY = camY + (12.0 / 16.0) + 0.005
    val maxZ = camZ + (12.0 / 16.0) + 0.005
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
    buffer.begin(3, VertexFormats.POSITION_COLOR);
    WorldRenderer.drawBox(buffer, minX, minY, minZ, maxX, maxY, maxZ, 0.0f, 0.0f, 0.0f, 1.0f)
    WorldRenderer.drawBox(buffer, minX, minY, minZ, maxX, maxY, maxZ, 1.0f, 1.0f, 0.0f, 1.0f)
    tez.draw()
    this.disableLightmap(false)
    GlStateManager.lineWidth(1.0F)
    GlStateManager.enableLighting()
    GlStateManager.enableTexture()
    GlStateManager.enableDepthTest()
    GlStateManager.depthMask(true)
    GlStateManager.enableFog()
  }

  private val doorwayItem = new ItemStack(BlockRegistry.BOSS_DOORWAY.asItem)

  private def playerIsHoldingDoorwayItem: Boolean = {
    val player = MinecraftClient.getInstance.player
    (player.isCreativeLevelTwoOp || player.isSpectator) && player.inventory.contains(UplandsMod.BOSSROOM_TECHNICAL_TAG)
  }
}
