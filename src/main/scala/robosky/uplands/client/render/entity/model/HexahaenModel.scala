package robosky.uplands.client.render.entity.model

import com.mojang.blaze3d.platform.GlStateManager

import java.util.Collections

import net.minecraft.client.render.entity.model.CompositeEntityModel
import net.minecraft.client.model.ModelPart

import robosky.uplands.entity.HexahaenEntity

object HexahaenModel extends CompositeEntityModel[HexahaenEntity] {

  // must be set before creating cuboids
  this.textureWidth = 128
  this.textureHeight = 64

  private val model: ModelPart = new ModelPart(this, 0, 0)
  model.addCuboid(-10, 4, -10, 20, 20, 20)

  override def getParts: java.lang.Iterable[ModelPart] = Collections.singletonList(model)
}
