package robosky.ether.block

import net.minecraft.block.{Block, BlockPlacementEnvironment, BlockState, Blocks}
import net.minecraft.block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.{Entity, EntityContext, LivingEntity}
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.{BlockView, GameRules, World}


object UplanderFarmlandBlock {
    private val SHAPE: VoxelShape =
        Block.createCuboidShape(0.0D,
            0.0D,
            0.0D,
            16.0D,
            15.0D,
            16.0D
        )
}

class UplanderFarmlandBlock(val settings: Block.Settings) extends Block(settings) {
    override def getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, context: EntityContext): VoxelShape
        = UplanderFarmlandBlock.SHAPE

    override def canPlaceAtSide(blockState_1: BlockState, blockView_1: BlockView, blockPos_1: BlockPos,
                                blockPlacementEnvironment_1: BlockPlacementEnvironment): Boolean = false

    override def onLandedUpon (world_1: World, blockPos_1: BlockPos, entity_1: Entity, float_1: Float): Unit =  {
        if (!world_1.isClient && world_1.random.nextFloat < float_1 - 0.5F && entity_1.isInstanceOf[LivingEntity]
            && (entity_1.isInstanceOf[PlayerEntity] || world_1.getGameRules.getBoolean(GameRules.MOB_GRIEFING))
            && entity_1.getWidth * entity_1.getWidth * entity_1.getHeight > 0.512F) {
            setToDirt(world_1.getBlockState(blockPos_1), world_1, blockPos_1)
        }

        super.onLandedUpon(world_1, blockPos_1, entity_1, float_1)
    }

    def setToDirt(blockState_1: BlockState, world_1: World, blockPos_1: BlockPos) =
        world_1.setBlockState(blockPos_1, BlockRegistry.UPLANDER_DIRT.getDefaultState)
}
