package robosky.ether.interop

import java.util
import java.util.Optional

import com.google.common.collect.Lists
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.util.Identifier
import robosky.ether.UplandsMod
import robosky.ether.block.machine.infuser.AegisaltRecipe
import robosky.ether.item.ItemRegistry

import scala.collection.JavaConverters._

case class AegisaltRecipeDisplay(recipe: AegisaltRecipe) extends RecipeDisplay[AegisaltRecipe] {
  private val fuel: util.ArrayList[ItemStack] = Lists.newArrayList(new ItemStack(ItemRegistry.AEGISALT_CRYSTAL))
  private val input: util.List[util.List[ItemStack]] = Lists.newArrayList(recipe.ingredients
    .map(_.getStackArray.toList.asJava).asJava)
  input.add(fuel)

  override def getRecipe: Optional[_ <: Recipe[_ <: Inventory]] = Optional.of(recipe)

  def getFuel: util.List[ItemStack] = fuel

  override def getOutput: util.List[ItemStack] = Lists.newArrayList(recipe.getOutput)

  override def getRecipeCategory: Identifier = UplandsMod :/ "plugins/aegisalt_infusion"

  override def getRequiredItems: util.List[util.List[ItemStack]] = getInput

  override def getInput: util.List[util.List[ItemStack]] = input
}
