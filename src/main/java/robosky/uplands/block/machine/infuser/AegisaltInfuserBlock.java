package robosky.uplands.block.machine.infuser;

import robosky.uplands.block.machine.base.BaseMachineBlock;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.tools.FabricToolTags;

public final class AegisaltInfuserBlock extends BaseMachineBlock {

    public AegisaltInfuserBlock() {
        super(FabricBlockSettings.of(Material.STONE)
            .strength(1.5f, 6f)
            .breakByTool(FabricToolTags.PICKAXES, 1)
            .sounds(BlockSoundGroup.STONE)
            .build());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if(!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if(be instanceof AegisaltInfuser) {
                ContainerProviderRegistry.INSTANCE.openContainer(
                    Registry.BLOCK.getId(this),
                    player,
                    buf -> buf.writeBlockPos(pos)
                );
            }
        }
        return ActionResult.SUCCESS;
    }
}
