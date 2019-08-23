package robosky.ether.block.machine.base

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{Block, BlockEntityProvider}
import net.minecraft.util.registry.Registry
import net.minecraft.world.BlockView
import robosky.ether.block.machine.MachineRegistry

abstract class BaseMachineBlock(s: Block.Settings) extends Block(s) with BlockEntityProvider {
  override def createBlockEntity(var1: BlockView): BlockEntity =
    MachineRegistry
      .MACHINES(
        Registry.BLOCK
          .getId(this)
      )
      .instantiate()
}
