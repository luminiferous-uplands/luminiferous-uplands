package robosky.uplands.client.render.blockentity

import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry

import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.render.block.entity.BlockEntityRenderer

import scala.reflect.{ClassTag, classTag}

object BlockEntityRenderingRegistry {

  private def register[T <: BlockEntity: ClassTag](renderer: BlockEntityRenderer[T]): Unit = {
    BlockEntityRendererRegistry.INSTANCE.register(classTag[T].runtimeClass.asInstanceOf[Class[_ <: BlockEntity]], renderer)
  }

  def init(): Unit = {
    register(ControlBlockEntityRenderer)
    register(DoorwayBlockEntityRenderer)
  }
}
