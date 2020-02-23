package robosky.uplands.client.render.blockentity

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry

import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.client.render.block.entity.{BlockEntityRenderDispatcher, BlockEntityRenderer}

import robosky.uplands.block.bossroom.{ControlBlockEntity, DoorwayBlockEntity}

import scala.reflect.{ClassTag, classTag}

object BlockEntityRenderingRegistry {

  private def register[T <: BlockEntity](tpe: BlockEntityType[T], renderer: BlockEntityRenderDispatcher => BlockEntityRenderer[T]): Unit = {
    BlockEntityRendererRegistry.INSTANCE.register(tpe, renderer(_))
  }

  def init(): Unit = {
    register(ControlBlockEntity.TYPE, new ControlBlockEntityRenderer(_))
    register(DoorwayBlockEntity.TYPE, new DoorwayBlockEntityRenderer(_))
  }
}
