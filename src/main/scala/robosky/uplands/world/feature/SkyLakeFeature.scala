package robosky.uplands.world.feature

import java.util.Random
import java.util.function.Function

import com.mojang.datafixers.Dynamic
import net.minecraft.world.gen.feature.{Feature, SingleStateFeatureConfig}
import robosky.uplands.block.BlockRegistry
import robosky.uplands.UplandsBlockTags

class SkyLakeFeature($f: Function[Dynamic[_], SingleStateFeatureConfig])
  extends Feature[SingleStateFeatureConfig]($f) {

  import net.minecraft.block.{Block, Blocks, Material}
  import net.minecraft.util.math.{BlockPos, ChunkPos}
  import net.minecraft.world.chunk.ChunkStatus
  import net.minecraft.world.gen.chunk.{ChunkGenerator, ChunkGeneratorConfig}
  import net.minecraft.world.gen.feature.{Feature, SingleStateFeatureConfig}
  import net.minecraft.world.{IWorld, LightType}

  def generate(world: IWorld, generator: ChunkGenerator[_ <: ChunkGeneratorConfig], random: Random, st: BlockPos,
    config: SingleStateFeatureConfig): Boolean = {
    var start = st
    while ( {
      start.getY > 5 && world.isAir(start)
    }) start = start.down

    if (start.getY <= 4) return false
    start = start.down(4)

    val chunkPos = new ChunkPos(start)
    if (!world
      .getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_REFERENCES)
      .getStructureReferences(Feature.VILLAGE.getName)
      .isEmpty) return false

    val bitset = new Array[Boolean](2048)
    val size = random.nextInt(4) + 4

    for (_ <- 0 until size) {
      val xSize = random.nextDouble * 6.0D + 3.0D
      val ySize = random.nextDouble * 4.0D + 2.0D
      // double ySize = random.nextDouble() * 2.0D + 2.0D;
      val zSize = random.nextDouble * 6.0D + 3.0D
      val centerX = random.nextDouble * (16.0D - xSize - 2.0D) + 1.0D + xSize / 2.0D
      val centerY = random.nextDouble * (8.0D - ySize - 4.0D) + 2.0D + ySize / 2.0D
      val centerZ = random.nextDouble * (16.0D - zSize - 2.0D) + 1.0D + zSize / 2.0D
      for {
        dx <- 1 until 15
        dz <- 1 until 15
        dy <- 1 until 7
      } {
        val xDist = (dx - centerX) / (xSize / 2.0D)
        val yDist = (dy - centerY) / (ySize / 2.0D)
        val zDist = (dz - centerZ) / (zSize / 2.0D)
        val distSq = xDist * xDist + yDist * yDist + zDist * zDist
        if (distSq < 1.0D) bitset((dx * 16 + dz) * 8 + dy) = true
      }
    }

    def bitsetCheck(dx: Int, dz: Int, dy: Int): Boolean = {
      !bitset((dx * 16 + dz) * 8 + dy) && (dx < 15 && bitset(
        ((dx + 1) * 16 + dz) * 8 + dy
      )
        || dx > 0 && bitset(((dx - 1) * 16 + dz) * 8 + dy) || dz < 15 && bitset(
        (dx * 16 + dz + 1) * 8 + dy
      )
        || dz > 0 && bitset((dx * 16 + dz - 1) * 8 + dy) || dy < 7 && bitset(
        (dx * 16 + dz) * 8 + dy + 1
      )
        || dy > 0 && bitset((dx * 16 + dz) * 8 + dy - 1))
    }

    for {
      dx <- 0 until 16
      dz <- 0 until 16
      dy <- 0 until 8
    } {
      if (bitsetCheck(dx, dz, dy)) {
        val material_1 = world.getBlockState(start.add(dx, dy, dz)).getMaterial
        if (dy >= 4 && material_1.isLiquid) return false
        if (dy < 4 && !material_1.isSolid && (world.getBlockState(
          start.add(dx, dy, dz)
        ) != config.state)) return false
      }
    }
    for {
      i <- 0 until 16
      dz <- 0 until 16
      dy <- 0 until 8
    } {
      if (bitset((i * 16 + dz) * 8 + dy))
        world.setBlockState(
          start.add(i, dy, dz),
          if (dy >= 4)
            Blocks.CAVE_AIR.getDefaultState
          else config.state,
          2
        )
    }
    for {
      dx <- 0 until 16
      dz <- 0 until 16
      dy <- 4 until 8
      if bitset((dx * 16 + dz) * 8 + dy)
    } {
      val pos = start.add(dx, dy - 1, dz)
      if (world.getBlockState(pos).getBlock.matches(UplandsBlockTags.PlantableOn) && world
        .getLightLevel(LightType.SKY, start.add(dx, dy, dz)) > 0)
        world.setBlockState(pos, BlockRegistry.UPLANDER_GRASS.getDefaultState, 2)
    }
    if (config.state.getMaterial == Material.LAVA) {
      for {
        dx <- 0 until 16
        dz <- 0 until 16
        dy <- 0 until 8
      } {
        if (bitsetCheck(dx, dz, dy) && (dy < 4 || (random.nextInt(2) != 0))
          && world.getBlockState(start.add(dx, dy, dz)).getMaterial.isSolid)
          world.setBlockState(
            start.add(dx, dy, dz),
            BlockRegistry.UPLANDER_STONE.getDefaultState,
            2
          )
      }
    }
    if (config.state.getMaterial == Material.WATER) {
      for {
        dx <- 0 until 16
        dz <- 0 until 16
      } {
        val pos = start.add(dx, 4, dz)
        if (world.getBiome(pos).canSetSnow(world, pos, false))
          world.setBlockState(pos, Blocks.ICE.getDefaultState, 2)
      }
    }
    true
  }
}
