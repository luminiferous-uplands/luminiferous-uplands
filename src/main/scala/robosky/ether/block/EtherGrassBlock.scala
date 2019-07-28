package robosky.ether.block

import java.util.Random

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.{Block, BlockState, Material}
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.tag.FluidTags
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.world.chunk.light.ChunkLightProvider
import net.minecraft.world.{ViewableWorld, World}

object EtherGrassBlock extends Block(FabricBlockSettings.of(Material.ORGANIC).ticksRandomly
  .strength(0.6f, 0.6f).sounds(BlockSoundGroup.GRASS).build()) {

  override def onScheduledTick(state: BlockState, world: World, pos: BlockPos, rand: Random): Unit = {
    if (!world.isClient) if (!canSurvive(state, world, pos))
      world.setBlockState(pos, BlockRegistry.ETHER_DIRT.getDefaultState)
    else if (world.getLightLevel(pos.up) >= 4) if (world.getLightLevel(pos.up) >= 9) {
      for (_ <- 0 until 4) {
        val blockPos_2 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1)
        if ((world.getBlockState(blockPos_2).getBlock == BlockRegistry.ETHER_DIRT) && canSpread(getDefaultState, world, blockPos_2))
          world.setBlockState(blockPos_2, getDefaultState)
      }
    }
  }

  private def canSpread(state: BlockState, world: ViewableWorld, pos: BlockPos) = {
    val blockPos_2 = pos.up
    canSurvive(state, world, pos) && !world.getFluidState(blockPos_2).matches(FluidTags.WATER)
  }

  private def canSurvive(state: BlockState, world: ViewableWorld, pos: BlockPos) = {
    val blockPos_2 = pos.up
    val blockState_2 = world.getBlockState(blockPos_2)
    val int_1 = ChunkLightProvider.method_20049(world, state, pos, blockState_2, blockPos_2,
      Direction.UP, blockState_2.getLightSubtracted(world, blockPos_2))
    int_1 < world.getMaxLightLevel
  }
}
