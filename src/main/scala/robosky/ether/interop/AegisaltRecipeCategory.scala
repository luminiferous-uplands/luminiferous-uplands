package robosky.ether.interop

import java.awt.Point
import java.util
import java.util.function.Supplier

import com.google.common.collect.Lists
import me.shedaniel.math.api
import me.shedaniel.rei.api.{RecipeCategory, Renderer}
import me.shedaniel.rei.gui.renderers.RecipeRenderer
import me.shedaniel.rei.gui.widget.{SlotWidget, Widget}
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import robosky.ether.UplandsMod
import robosky.ether.block.machine.MachineRegistry

import scala.collection.JavaConverters._

object AegisaltRecipeCategory extends RecipeCategory[AegisaltRecipeDisplay] {
  override def getIdentifier: Identifier = UplandsMod :/ "plugins/aegisalt_infusions"

  override def getCategoryName: String = "Aegisalt Infusion"

  override def getIcon: Renderer = Renderer.fromItemStack(new ItemStack(MachineRegistry.aegisaltInfuser.block))

  override def getSimpleRenderer(recipe: AegisaltRecipeDisplay): RecipeRenderer =
    Renderer.fromRecipe(() => (0 to 1).map(recipe.getInput.get(_)).toList.asJava, () => recipe.getOutput)

  override def setupDisplay(recipeDisplaySupplier: Supplier[AegisaltRecipeDisplay], bounds: api.Rectangle): util.List[Widget] = {
    val startPoint: Point = new Point(bounds.getCenterX.toInt - 60, bounds.getCenterY.toInt - 27)
    val widgets: util.List[Widget] = Lists.newArrayList()
    val input: util.List[util.List[ItemStack]] = recipeDisplaySupplier.get.getInput
    widgets.add(new SlotWidget(startPoint.x + 1, startPoint.y + 1, Renderer.fromItemStacks(input.get(0)), true,
      true, true))
    widgets.add(new SlotWidget(startPoint.x + 37, startPoint.y + 1, Renderer.fromItemStacks(input.get(1)), true,
      true, true))
    widgets.add(new SlotWidget(startPoint.x + 68, startPoint.y + 19, Renderer.fromItemStacks(recipeDisplaySupplier.get.getFuel),
      true, true, true))
    widgets.add(new SlotWidget(startPoint.x + 98, startPoint.y + 1, Renderer.fromItemStacks(recipeDisplaySupplier.get.getOutput),
      true, true, true))
    widgets
  }
}
