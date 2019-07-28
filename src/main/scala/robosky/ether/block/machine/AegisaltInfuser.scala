package robosky.ether.block.machine

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block.Material
import net.minecraft.sound.BlockSoundGroup
import robosky.ether.block.machine.base.{BaseMachineBlock, BaseMachineBlockEntity, MachineGUIBlockActivationSkeleton}

import scala.reflect.{ClassTag, classTag}

case class AegisaltInfuser() extends BaseMachineBlockEntity(MachineRegistry.aegisaltInfuser.blockEntityType) {

}

object AegisaltInfuserBlock extends BaseMachineBlock(FabricBlockSettings.of(Material.STONE).strength(1.5f, 6f)
  .breakByTool(FabricToolTags.PICKAXES, 1).sounds(BlockSoundGroup.STONE).build())
  with MachineGUIBlockActivationSkeleton[AegisaltInfuser] {
  override protected def beCTag: ClassTag[AegisaltInfuser] = classTag
}