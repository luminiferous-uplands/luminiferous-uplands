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

  override protected def getTexture(entity: HexahaenEntity): Identifier = TEXTURES(entity.strength - 1)
}
