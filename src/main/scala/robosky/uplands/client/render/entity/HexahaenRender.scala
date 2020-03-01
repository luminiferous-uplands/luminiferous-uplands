package robosky.uplands.client.render.entity

import net.minecraft.client.render.entity.{EntityRenderDispatcher, MobEntityRenderer}
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

import robosky.uplands.UplandsMod
import robosky.uplands.entity.HexahaenEntity
import robosky.uplands.client.render.entity.model.HexahaenModel

class HexahaenRender(dispatcher: EntityRenderDispatcher)
    extends MobEntityRenderer[HexahaenEntity, HexahaenModel.type](dispatcher, HexahaenModel, 1.0f) {

  private val TEXTURES: Array[Identifier] = Array(
    UplandsMod :/ "textures/entity/hexahaen/level_1.png",
    UplandsMod :/ "textures/entity/hexahaen/level_2.png",
    UplandsMod :/ "textures/entity/hexahaen/level_3.png",
    UplandsMod :/ "textures/entity/hexahaen/level_4.png",
    UplandsMod :/ "textures/entity/hexahaen/level_5.png"
  )

  override def render(entity: HexahaenEntity, f1: Float, f2: Float, matrix: MatrixStack, provider: VertexConsumerProvider, var1: Int): Unit = {
    // set shadow size to 87.5% of normal
    this.shadowSize = 0.875f
    super.render(entity, f1, f2, matrix, provider, var1)
  }

  // TODO: reflecting causes weird lighting artifacts
  override protected def scale(entity: HexahaenEntity, matrix: MatrixStack, f1: Float): Unit = {
    if (entity.isLeftHanded) {
      matrix.scale(-1.0f, 1.0f, 1.0f)
    }
    super.scale(entity, matrix, f1)
  }

  override protected def getTexture(entity: HexahaenEntity): Identifier = TEXTURES(entity.strength - 1)
}
