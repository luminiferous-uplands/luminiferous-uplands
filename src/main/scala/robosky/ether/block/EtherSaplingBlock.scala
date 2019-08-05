package robosky.ether.block

import java.util.Random

import net.minecraft.block.{Block, BlockState, Fertilizable, PlantBlock}
import net.minecraft.entity.EntityContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.{IntProperty, Properties}
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.{BlockView, IWorld, World}
import robosky.ether.world.feature.trees.EtherSaplingGenerator

object EtherSaplingBlock {
  val STAGE: IntProperty = Properties.STAGE
  val SHAPE: VoxelShape =
    Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D)
}

class EtherSaplingBlock(val generator: EtherSaplingGenerator, val settings: Block.Settings) extends PlantBlock(settings)
  with Fertilizable {
  this.setDefaultState(this.stateFactory.getDefaultState.`with`[Integer, Integer](EtherSaplingBlock.STAGE, 0))

  override def canPlantOnTop(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos): Boolean =
    blockState_1.getBlock == BlockRegistry.ETHER_DIRT || blockState_1.getBlock == BlockRegistry.ETHER_GRASS

  override def getOutlineShape(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos,
    entityContext_1: EntityContext): VoxelShape = EtherSaplingBlock.SHAPE

  override def onScheduledTick(blockState_1: BlockState, world_1: World, blockPos_1: BlockPos, random_1: Random): Unit = {
    if (world_1.getLightLevel(blockPos_1.up) >= 9 && random_1.nextInt(7) == 0)
      this.generate(world_1, blockPos_1, blockState_1, random_1)
  }

  override def isFertilizable(blockView_1: BlockView, blockPos_1: BlockPos, blockState_1: BlockState,
    boolean_1: Boolean) = true

  override def canGrow(world_1: World, random_1: Random, blockPos_1: BlockPos, blockState_1: BlockState): Boolean =
    world_1.random.nextFloat.toDouble < 0.45D

  override def grow(world_1: World, random_1: Random, blockPos_1: BlockPos, blockState_1: BlockState): Unit = {
    this.generate(world_1, blockPos_1, blockState_1, random_1)
  }

  def generate(iWorld_1: IWorld, blockPos_1: BlockPos, blockState_1: BlockState, random_1: Random): Unit = {
    if (blockState_1.get(EtherSaplingBlock.STAGE).intValue() == 0)
      iWorld_1.setBlockState(blockPos_1, blockState_1.cycle(EtherSaplingBlock.STAGE), 4)
    else this.generator.generate(iWorld_1, blockPos_1, blockState_1, random_1)
  }

  override protected def appendProperties(builder: StateFactory.Builder[Block, BlockState]): Unit =
    builder.add(EtherSaplingBlock.STAGE)

}
