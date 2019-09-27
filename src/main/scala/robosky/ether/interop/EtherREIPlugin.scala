package robosky.ether.interop

import me.shedaniel.rei.api.RecipeHelper
import me.shedaniel.rei.api.plugins.REIPluginV0
import net.fabricmc.loader.api.SemanticVersion
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import robosky.ether.UplandsMod
import robosky.ether.block.machine.MachineRegistry
import robosky.ether.block.machine.infuser.AegisaltRecipe

class EtherREIPlugin extends REIPluginV0 {
  override def getPluginIdentifier: Identifier = UplandsMod :/ "uplands_rei"

  override def registerPluginCategories(recipeHelper: RecipeHelper): Unit = {
    recipeHelper.registerCategory(AegisaltRecipeCategory)
  }

  override def registerRecipeDisplays(recipeHelper: RecipeHelper): Unit = {
    recipeHelper.registerRecipes[AegisaltRecipe](UplandsMod :/ "plugins/aegisalt_infusions", classOf[AegisaltRecipe],
      recipe => AegisaltRecipeDisplay.apply(recipe))
  }

  override def registerOthers(recipeHelper: RecipeHelper): Unit = {
    recipeHelper.registerWorkingStations(UplandsMod :/ "plugins/aegisalt_infusions", new ItemStack(MachineRegistry.aegisaltInfuser.block))
  }

  override def getMinimumVersion: SemanticVersion = SemanticVersion.parse("3.0-pre")
}
