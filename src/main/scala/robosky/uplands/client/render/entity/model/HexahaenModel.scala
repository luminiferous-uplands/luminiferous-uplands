package robosky.uplands.client.render.entity.model

import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.model.Cuboid

import robosky.uplands.entity.HexahaenEntity

object HexahaenModel extends EntityModel[HexahaenEntity] {

  // must be set before creating cuboids
  this.textureWidth = 128
  this.textureHeight = 64

  private val model: Cuboid = new Cuboid(this, 0, 0)
  model.addBox(-10, 4, -10, 20, 20, 20)

  override def render(entity: HexahaenEntity, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float, scale: Float): Unit = {
    model.render(scale)
  }
}
