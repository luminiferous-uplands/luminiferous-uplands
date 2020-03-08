package robosky.uplands.client.render.entity

import net.minecraft.client.render.entity.{EntityRenderDispatcher, MobEntityRenderer}
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

import robosky.uplands.UplandsMod
import robosky.uplands.entity.MegadungeonBossEntity
import robosky.uplands.client.render.entity.model.MegadungeonBossModel

class MegadungeonBossRender(dispatcher: EntityRenderDispatcher)
    extends MobEntityRenderer[MegadungeonBossEntity, MegadungeonBossModel.type](dispatcher, MegadungeonBossModel, 1.0f) {

  private val TEXTURE: Identifier = UplandsMod :/ "textures/entity/megadungeon_boss.png"

  override def render(entity: MegadungeonBossEntity, f1: Float, f2: Float, matrix: MatrixStack, provider: VertexConsumerProvider, var1: Int): Unit = {
    // set shadow size to 87.5% of normal
    this.shadowSize = 0.875f
    super.render(entity, f1, f2, matrix, provider, var1)
  }

  override protected def getTexture(entity: MegadungeonBossEntity): Identifier = TEXTURE
}
