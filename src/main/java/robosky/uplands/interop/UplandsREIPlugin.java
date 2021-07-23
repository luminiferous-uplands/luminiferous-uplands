package robosky.uplands.interop;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import robosky.uplands.UplandsMod;
import robosky.uplands.block.machine.MachineRegistry;
import robosky.uplands.block.machine.infuser.AegisaltRecipe;

import net.minecraft.util.Identifier;

public final class UplandsREIPlugin implements REIPluginV0 {
    @Override
    public Identifier getPluginIdentifier() {
        return UplandsMod.id("uplands_rei");
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new AegisaltRecipeCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipes(UplandsMod.id("plugins/aegisalt_infusions"), AegisaltRecipe.class, AegisaltRecipeDisplay::new);
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(UplandsMod.id("plugins/aegisalt_infusions"), EntryStack.create(MachineRegistry.AEGISALT_INFUSER.block));
    }
}
