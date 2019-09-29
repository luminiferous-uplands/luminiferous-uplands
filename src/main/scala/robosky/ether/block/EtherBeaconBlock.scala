package robosky.ether.block

import java.util.Random
import java.lang.{Boolean => JBoolean}

import net.fabricmc.api.{EnvType, Environment}
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block.{Block, BlockState, Material}
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.{SoundCategory, SoundEvents}
import net.minecraft.state.StateFactory.Builder
import net.minecraft.state.property.{BooleanProperty, IntProperty}
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction.Axis
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.{BlockView, ViewableWorld, World}
import robosky.ether.iface.UplanderBeaconUser

object EtherBeaconBlock extends Block(FabricBlockSettings.of(Material.STONE).strength(3, 3)
  .breakByTool(FabricToolTags.PICKAXES, 2).ticksRandomly().build()) {
  def SMOKING: BooleanProperty = BooleanProperty.of("smoking")

  setDefaultState(getStateFactory.getDefaultState.`with`(SMOKING,JBoolean.FALSE))

  override def getOutlineShape(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos,
    entityContext_1: EntityContext): VoxelShape =
    Block.createCuboidShape(2, 0, 2, 14, 16, 14)

  override def activate(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand,
    result: BlockHitResult): Boolean = {
    if (player.world.dimension.getType == DimensionType.OVERWORLD) {
      player.asInstanceOf[UplanderBeaconUser].uplands_setUsingBeacon(true)
      true
    } else {
      if (state.get(SMOKING).booleanValue()) {
        return false
      }

      world.setBlockState(pos, BlockRegistry.UPLANDER_BEACON.getDefaultState.`with`(SMOKING, JBoolean.TRUE))
      world.getBlockTickScheduler.schedule(pos, this, this.getTickRate(world))
      world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3f, 0.6f)
      true
    }
  }

  override def getTickRate(viewableWorld_1: ViewableWorld): Int = 35

  override def appendProperties(builder: Builder[Block, BlockState]): Unit = {
    builder.add(SMOKING)
  }

  override def onScheduledTick(blockstate: BlockState, world: World, blockPos: BlockPos, random: Random): Unit = {
    if (!world.isClient && blockstate.get(SMOKING).booleanValue()) {
      world.setBlockState(blockPos, blockstate.`with`(SMOKING, JBoolean.FALSE))
    }
  }

  @Environment(EnvType.CLIENT)
  override def randomDisplayTick(blockState_1: BlockState, world_1: World, blockPos_1: BlockPos, random_1: Random): Unit = {
    if (blockState_1.get(SMOKING).asInstanceOf[Boolean]) {
      val baseX = blockPos_1.getX.toDouble + 0.5D
      val baseY = blockPos_1.getY.toDouble + 0.5D
      val baseZ = blockPos_1.getZ.toDouble + 0.5D

      world_1.addParticle(ParticleTypes.SMOKE, baseX, baseY, baseZ, 0.0D, 0.0D, 0.0D)
    }
  }
}
