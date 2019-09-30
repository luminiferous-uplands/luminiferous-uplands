package robosky.ether.block

import java.util.Random

import robosky.ether.UplandsBlockTags
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.{Block, BlockState, MushroomPlantBlock, PlantBlock}
import net.minecraft.entity.EntityContext
import net.minecraft.tag.BlockTags
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.{BlockView, ViewableWorld, World}

object AzoteMushroomBlock {
  val SHAPE: VoxelShape = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D)
}

class AzoteMushroomBlock(val settings: Block.Settings) extends MushroomPlantBlock(settings) {
  override def canPlantOnTop(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos): Boolean =
    blockState_1.matches(UplandsBlockTags.AzoteMushroomSpreadable) ||
      blockState_1.matches(TagRegistry.block(new Identifier("luminiferous_uplands:azote_mushroom_spreadable")))

  override def canPlaceAt(blockState_1: BlockState, viewableWorld_1: ViewableWorld, blockPos_1: BlockPos): Boolean = {
    val blockState_2 = viewableWorld_1.getBlockState(blockPos_1.down())
    blockState_2.matches(BlockTags.DIRT_LIKE) ||
      blockState_2.matches(TagRegistry.block(new Identifier("luminiferous_uplands:azote_mushroom_spreadable")))
  }


  override def getOutlineShape(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos,
                               entityContext_1: EntityContext): VoxelShape = AzoteMushroomBlock.SHAPE

  override def isFertilizable(blockView_1: BlockView, blockPos_1: BlockPos, blockState_1: BlockState, boolean_1: Boolean): Boolean = false

  override def onScheduledTick(blockState_1: BlockState, world_1: World, blockPos_1: BlockPos, random_1: Random): Unit = {
    // If it should spread this frame
    if (random_1.nextInt(25) == 0) {
      // The number of mushrooms that can be in close proximity, before it stops spreading.
      var mushroomThreshold = 5

      // A box representing what the mushroom counts as "close proximity"
      val nearbyMushroomRadius = BlockPos.iterate(blockPos_1.add(-4, -1, -4), blockPos_1.add(4, 1, 4)).iterator

      // Iterate over the box
      while ( {
        nearbyMushroomRadius.hasNext
      }) {
        // For each block in the box, check if it's a mushroom.
        val blockPos_2 = nearbyMushroomRadius.next
        if (world_1.getBlockState(blockPos_2).getBlock eq this) {
          // If it is, count down by one, and stop if there are too many.
          mushroomThreshold -= 1
          if (mushroomThreshold <= 0) return
        }
      }

      // Vary the spawn position a little bit
      val mushroomSpawnLocation = blockPos_1.add(random_1.nextInt(3) - 1, random_1.nextInt(2) - random_1.nextInt(2), random_1.nextInt(3) - 1)

      // If this is a valid position, spawn there
      if (world_1.isAir(mushroomSpawnLocation) && world_1.getBlockState(mushroomSpawnLocation.down()).matches(UplandsBlockTags.AzoteMushroomSpreadable))
        world_1.setBlockState(mushroomSpawnLocation, blockState_1, 2)
    }
  }
}
