package robosky.ether.block.machine.infuser

import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.widget.{WGridPanel, WItemSlot, WLabel}
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.TranslatableText
import robosky.ether.block.machine.RecipeRegistry

class InfuserContainer(syncId: Int, playerInventory: PlayerInventory, ctx: BlockContext)
  extends CottonScreenController(RecipeRegistry.aegisaltRecipe, syncId, playerInventory,
    CottonScreenController.getBlockInventory(ctx), CottonScreenController.getBlockPropertyDelegate(ctx)) {

  private val be: AegisaltInfuser =
    ctx.run((w, p) => w.getBlockEntity(p).asInstanceOf[AegisaltInfuser]).get()

  def init() {
    val gridRoot = rootPanel.asInstanceOf[WGridPanel]
    gridRoot.add(
      new WLabel(
        new TranslatableText("block.luminiferous_uplands.aegisalt_infuser"),
        WLabel.DEFAULT_TEXT_COLOR
      ),
      0,
      0
    )

    gridRoot.add(WItemSlot.of(blockInventory, 0, 2, 1), 2, 1)
    gridRoot.add(WItemSlot.of(blockInventory, 2), 5, 2)
    gridRoot.add(WItemSlot.outputOf(blockInventory, 3), 7, 1)
    gridRoot.add(this.createPlayerInventoryPanel(), 0, 4)

    gridRoot.validate(this)
  }

  init()

  override def getCraftingResultSlotIndex: Int = -1
}
