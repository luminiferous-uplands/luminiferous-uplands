package robosky.ether.interop

import java.awt.{Point, Rectangle}
import java.util
import java.util.Collections
import java.util.function.Supplier

import com.google.common.collect.Lists
import me.shedaniel.rei.api.{RecipeCategory, Renderable, Renderer}
import me.shedaniel.rei.gui.renderables.RecipeRenderer
import me.shedaniel.rei.gui.widget.{SlotWidget, Widget}
import net.minecraft.client.resource.language.I18n
import net.minecraft.item.ItemStack
import net.minecraft.util.{Formatting, Identifier}
import robosky.ether.UplandsMod
import robosky.ether.block.machine.MachineRegistry

import scala.collection.JavaConverters._

object AegisaltRecipeCategory extends RecipeCategory[AegisaltRecipeDisplay] {
  override def getIdentifier: Identifier = UplandsMod :/ "plugins/aegisalt_infusion"

  override def getCategoryName: String = "Aegisalt Infusion"

  override def getIcon: Renderer = Renderable.fromItemStack(new ItemStack(MachineRegistry.aegisaltInfuser.block))

  override def getSimpleRenderer(recipe: AegisaltRecipeDisplay): RecipeRenderer =
    Renderable.fromRecipe(() => (0 to 1).map(recipe.getInput.get(_)).toList.asJava, () => recipe.getOutput)

  override def setupDisplay(recipeDisplaySupplier: Supplier[AegisaltRecipeDisplay], bounds: Rectangle): util.List[Widget] = {
    val startPoint: Point = new Point(bounds.getCenterX.toInt - 41, bounds.getCenterY.toInt - 27)
    val widgets: util.List[Widget] = Lists.newArrayList()
    val input: util.List[util.List[ItemStack]] = recipeDisplaySupplier.get.getInput
    widgets.add(new SlotWidget(startPoint.x + 1, startPoint.y + 1, input.get(0), true,
      true, true))
    widgets.add(new SlotWidget(startPoint.x + 37, startPoint.y + 1, input.get(0), true,
      true, true))
    widgets.add(new SlotWidget(startPoint.x + 1, startPoint.y + 37, recipeDisplaySupplier.get.getFuel,
      true, true, true) {
      override protected def getExtraToolTips(stack: ItemStack): util.List[String] = Collections.singletonList(
        Formatting.YELLOW.toString + I18n.translate("luminiferous_uplands.rei.category.aegisalt"))
    })
    widgets.add(new SlotWidget(startPoint.x + 98, startPoint.y + 1, recipeDisplaySupplier.get.getOutput,
      false, true, true))
    widgets
  }
}
