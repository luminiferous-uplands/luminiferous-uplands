package robosky.uplands.client.render.entity

import net.minecraft.client.render.entity.{EntityRenderDispatcher, MobEntityRenderer}
import net.minecraft.util.Identifier

import robosky.uplands.UplandsMod
import robosky.uplands.entity.HexahaenEntity
import robosky.uplands.client.render.entity.model.HexahaenModel

class HexahaenRender(dispatcher: EntityRenderDispatcher)
    extends MobEntityRenderer[HexahaenEntity, HexahaenModel.type](dispatcher, HexahaenModel, 1.0f) {

  private val TEXTURE: Identifier = UplandsMod :/ "textures/entity/hexahaen.png"

  override protected def getTexture(entity: HexahaenEntity): Identifier = {
    TEXTURE
  }
}
