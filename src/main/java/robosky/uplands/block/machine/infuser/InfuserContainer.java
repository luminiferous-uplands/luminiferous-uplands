package robosky.uplands.block.machine.infuser;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import robosky.uplands.block.machine.MachineRegistry;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.TranslatableText;

public class InfuserContainer extends SyncedGuiDescription {

    public InfuserContainer(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx) {
        super(MachineRegistry.AEGISALT_INFUSER.gui, syncId, playerInventory,
            SyncedGuiDescription.getBlockInventory(ctx), SyncedGuiDescription.getBlockPropertyDelegate(ctx));
        init();
    }

    private void init() {
        WGridPanel gridRoot = (WGridPanel)rootPanel;
        gridRoot.add(
            new WLabel(
                new TranslatableText("block.luminiferous_uplands.aegisalt_infuser"),
                WLabel.DEFAULT_TEXT_COLOR
            ),
            0,
            0
        );

        gridRoot.add(WItemSlot.of(blockInventory, 0, 2, 1), 2, 1);
        gridRoot.add(WItemSlot.of(blockInventory, 2), 5, 2);
        gridRoot.add(WItemSlot.outputOf(blockInventory, 3), 7, 1);
        gridRoot.add(this.createPlayerInventoryPanel(), 0, 4);

        gridRoot.validate(this);
    }

    @Deprecated
    public int getCraftingResultSlotIndex() {
        return -1;
    }
}
