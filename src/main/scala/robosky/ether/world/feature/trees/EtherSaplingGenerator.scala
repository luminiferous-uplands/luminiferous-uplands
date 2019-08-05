package robosky.ether.world.feature.trees

import java.util.Random

import com.mojang.datafixers.Dynamic
import javax.annotation.Nullable
import net.minecraft.block.{BlockState, Blocks}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.gen.feature.{DefaultFeatureConfig, FeatureConfig}

abstract class EtherSaplingGenerator {
  def generate(iWorld_1: IWorld, blockPos_1: BlockPos, blockState_1: BlockState, random_1: Random): Boolean = {
    val abstractTreeFeature_1 = this.createTreeFeature(random_1)
    if (abstractTreeFeature_1 == null) false
    else {
      iWorld_1.setBlockState(blockPos_1, Blocks.AIR.getDefaultState, 4)
      if (abstractTreeFeature_1.generate(iWorld_1, iWorld_1.getChunkManager.getChunkGenerator, random_1, blockPos_1,
        FeatureConfig.DEFAULT)) true
      else {
        iWorld_1.setBlockState(blockPos_1, blockState_1, 4)
        false
      }
    }
  }

  @Nullable protected def createTreeFeature(var1: Random): AbstractEtherTree[DefaultFeatureConfig]
}

object EtherSaplingGenerator {

  object SkyrootSaplingGenerator extends EtherSaplingGenerator {
    @Nullable override protected def createTreeFeature(random_1: Random) = new SkyrootTreeFeature((t: Dynamic[_]) =>
      DefaultFeatureConfig.deserialize(t), true)
  }

}
