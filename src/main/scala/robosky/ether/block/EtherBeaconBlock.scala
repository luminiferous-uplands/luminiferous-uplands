package robosky.ether.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block.{Block, BlockState, Material}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import robosky.ether.world.WorldRegistry

object EtherBeaconBlock extends Block(FabricBlockSettings.of(Material.STONE).strength(3, 3)
  .breakByTool(FabricToolTags.PICKAXES, 2).build()) {
  override def activate(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, result: BlockHitResult): Boolean = {
    if (player.world.dimension.getType eq WorldRegistry.ETHER_DIMENSION) player.changeDimension(DimensionType.OVERWORLD)
    else { // going to our custom dimension
      player.changeDimension(WorldRegistry.ETHER_DIMENSION)
    }
    true
  }
}
