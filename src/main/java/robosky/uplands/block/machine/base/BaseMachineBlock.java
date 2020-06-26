package robosky.uplands.block.machine.base;

import robosky.uplands.block.machine.MachineRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public abstract class BaseMachineBlock extends Block implements BlockEntityProvider {

    public BaseMachineBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return MachineRegistry.MACHINES.get(Registry.BLOCK.getId(this)).instantiate();
    }
}
