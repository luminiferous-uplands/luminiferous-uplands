package robosky.uplands.interop

import java.util
import java.util.Optional

import com.google.common.collect.Lists
import me.shedaniel.rei.api.{EntryStack, RecipeDisplay}
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.util.Identifier
import robosky.uplands.UplandsMod
import robosky.uplands.block.machine.infuser.AegisaltRecipe
import robosky.uplands.item.ItemRegistry

import scala.collection.JavaConverters._

case class AegisaltRecipeDisplay(recipe: AegisaltRecipe) extends RecipeDisplay {
  private val fuel: util.ArrayList[EntryStack] = Lists.newArrayList(EntryStack.create(ItemRegistry.AEGISALT_CRYSTAL))
  private val input: util.List[util.List[EntryStack]] = Lists.newArrayList(recipe.ingredients
    .map(_.getMatchingStacksClient.toList.map(EntryStack.create).asJava).asJava)
  input.add(fuel)

  def getFuel: util.List[EntryStack] = fuel

  override def getOutputEntries: util.List[EntryStack] = Lists.newArrayList(EntryStack.create(recipe.getOutput))

  override def getRecipeCategory: Identifier = UplandsMod :/ "plugins/aegisalt_infusions"

  override def getRequiredEntries: util.List[util.List[EntryStack]] = getInputEntries

  override def getInputEntries: util.List[util.List[EntryStack]] = input
}
