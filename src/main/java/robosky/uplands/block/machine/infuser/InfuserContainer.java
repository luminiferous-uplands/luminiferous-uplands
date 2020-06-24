package robosky.uplands.block.machine.infuser;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import robosky.uplands.block.machine.RecipeRegistry;

import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TranslatableText;

public class InfuserContainer extends CottonCraftingController {

    public InfuserContainer(int syncId, PlayerInventory playerInventory, BlockContext ctx) {
        super(RecipeRegistry.aegisaltRecipe(), syncId, playerInventory,
            CottonCraftingController.getBlockInventory(ctx), CottonCraftingController.getBlockPropertyDelegate(ctx));
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


    @Override
    public int getCraftingResultSlotIndex() {
        return -1;
    }
}
