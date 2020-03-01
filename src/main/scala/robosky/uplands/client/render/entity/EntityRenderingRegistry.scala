package robosky.uplands.client.render.entity

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.{EntityRenderDispatcher, EntityRenderer}
import net.minecraft.entity.{Entity, EntityType}

import robosky.uplands.entity.EntityRegistry

import scala.reflect.{ClassTag, classTag}

object EntityRenderingRegistry {

  private def register[E <: Entity](tpe: EntityType[E], f: (EntityRenderDispatcher, EntityRendererRegistry.Context) => EntityRenderer[E]): Unit = {
    EntityRendererRegistry.INSTANCE.register(tpe, f(_, _))
  }

  def init(): Unit = {
    register(EntityRegistry.HEXAHAEN, (d, c) => new HexahaenRender(d))
  }
}
