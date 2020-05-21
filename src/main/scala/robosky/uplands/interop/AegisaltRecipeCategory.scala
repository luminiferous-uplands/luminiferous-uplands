package robosky.uplands.interop

import java.awt.Point
import java.util
import java.util.function.Supplier

import com.google.common.collect.Lists
import me.shedaniel.math.api
import me.shedaniel.rei.api.{EntryStack, RecipeCategory}
import me.shedaniel.rei.gui.entries.{RecipeEntry, SimpleRecipeEntry}
import me.shedaniel.rei.gui.widget.{EntryWidget, Widget}
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import robosky.uplands.UplandsMod
import robosky.uplands.block.machine.MachineRegistry

import scala.collection.JavaConverters._

object AegisaltRecipeCategory extends RecipeCategory[AegisaltRecipeDisplay] {
  override def getIdentifier: Identifier = UplandsMod :/ "plugins/aegisalt_infusions"

  override def getCategoryName: String = "Aegisalt Infusion"

  // Is there a way to cast a MachineEntry to an ItemStack?
  override def getLogo: EntryStack = EntryStack.create(MachineRegistry.aegisaltInfuser.block)

  override def getSimpleRenderer(recipe: AegisaltRecipeDisplay): RecipeEntry =
    SimpleRecipeEntry.create(
      () => (0 to 1).map(recipe.getInputEntries.get(_)).toList.asJava,
      () => recipe.getOutputEntries
    )

  override def setupDisplay(recipeDisplaySupplier: Supplier[AegisaltRecipeDisplay], bounds: api.Rectangle): util.List[Widget] = {
    val startPoint: Point = new Point(bounds.getCenterX.toInt - 60, bounds.getCenterY.toInt - 27)
    val widgets: util.List[Widget] = Lists.newArrayList()
    val input: util.List[util.List[EntryStack]] = recipeDisplaySupplier.get.getInputEntries
    // I'm pretty sure that EntryWidget is the replacement for SlotWidget, but I'm not sure.
    // It seems like SimpleRecipeEntry is the replacement for Renderer, but it doesn't appear
    // to have an equivalent to fromItemStacks, requiring an explicit input and output list.
    widgets.add(EntryWidget.create(startPoint.x + 1, startPoint.y + 1).entries(input.get(0)))
    widgets.add(EntryWidget.create(startPoint.x + 37, startPoint.y + 1).entries(input.get(1)))
    widgets.add(EntryWidget.create(startPoint.x + 68, startPoint.y + 19).entries(recipeDisplaySupplier.get.getFuel))
    widgets.add(EntryWidget.create(startPoint.x + 98, startPoint.y + 1).entries(recipeDisplaySupplier.get.getOutputEntries))
    widgets
  }
}
