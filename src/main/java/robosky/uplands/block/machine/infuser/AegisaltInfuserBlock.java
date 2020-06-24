package robosky.uplands.block.machine.infuser;

import robosky.uplands.block.machine.base.BaseMachineBlock;
import robosky.uplands.block.machine.base.MachineGUIBlockActivationSkeleton;
import scala.reflect.ClassTag;

import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;

public final class AegisaltInfuserBlock extends BaseMachineBlock implements MachineGUIBlockActivationSkeleton<AegisaltInfuser> {

    public AegisaltInfuserBlock() {
        super(FabricBlockSettings.of(Material.STONE)
            .strength(1.5f, 6f)
            .breakByTool(FabricToolTags.PICKAXES, 1)
            .sounds(BlockSoundGroup.STONE)
            .build());
    }

    @Override
    public ClassTag<AegisaltInfuser> beCTag() {
        return ClassTag.apply(AegisaltInfuser.class);
    }
}
