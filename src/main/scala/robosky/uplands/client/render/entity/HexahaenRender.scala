package robosky.uplands.client.render.entity

import net.minecraft.client.render.entity.{EntityRenderDispatcher, MobEntityRenderer}
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

  /**
   * Shadow rendering method.
   */
  override def method_4072(entity: HexahaenEntity, x: Double, y: Double, z: Double, bOop: Float, partialTicks: Float): Unit = {
    // set shadow size to 87.5% of normal
    this.field_4673 = 0.875f
    super.method_4072(entity, x, y, z, bOop, partialTicks)
  }

  override protected def getTexture(entity: HexahaenEntity): Identifier = TEXTURES(entity.strength - 1)
}
