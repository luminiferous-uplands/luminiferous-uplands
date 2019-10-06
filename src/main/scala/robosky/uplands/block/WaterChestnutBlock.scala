package robosky.uplands.block

import net.fabricmc.api.{EnvType, Environment}
import net.minecraft.block.{Block, BlockState, CropBlock, Material}
import net.minecraft.entity.EntityContext
import net.minecraft.fluid.{FluidState, Fluids}
import net.minecraft.item.ItemConvertible
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import robosky.uplands.item.ItemRegistry

object WaterChestnutBlock {
    private val AGE_TO_SHAPE = Array[VoxelShape](
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
        Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D)
    )
}

class WaterChestnutBlock(val settings: Block.Settings) extends CropBlock(settings) {
    @Environment(EnvType.CLIENT) override protected def getSeedsItem: ItemConvertible = ItemRegistry.WATER_CHESTNUT_SEEDS_ITEM

    override def getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, context: EntityContext): VoxelShape =
        WaterChestnutBlock.AGE_TO_SHAPE(state.get(this.getAgeProperty))

    override protected def canPlantOnTop(blockState_1: BlockState, blockView_1: BlockView,
                                         blockPos_1: BlockPos): Boolean = {
        val fluidState_1 = blockView_1.getFluidState(blockPos_1)
        (fluidState_1.getFluid eq Fluids.WATER)
    }
}
