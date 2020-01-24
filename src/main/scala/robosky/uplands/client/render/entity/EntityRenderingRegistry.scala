package robosky.uplands.client.render.entity

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.{EntityRenderDispatcher, EntityRenderer}
import net.minecraft.entity.Entity

import scala.reflect.{ClassTag, classTag}

object EntityRenderingRegistry {

  private def register[E <: Entity: ClassTag](f: (EntityRenderDispatcher, EntityRendererRegistry.Context) => EntityRenderer[E]): Unit = {
    EntityRendererRegistry.INSTANCE.register(classTag[E].runtimeClass.asInstanceOf[Class[_ <: Entity]], f(_, _))
  }

  def init(): Unit = {
    register((d, c) => new HexahaenRender(d))
  }
}
