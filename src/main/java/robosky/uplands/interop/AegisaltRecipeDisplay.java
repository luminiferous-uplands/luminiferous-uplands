package robosky.uplands.interop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import robosky.uplands.UplandsMod;
import robosky.uplands.block.machine.infuser.AegisaltRecipe;
import robosky.uplands.item.ItemRegistry;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public final class AegisaltRecipeDisplay implements RecipeDisplay {

    private final AegisaltRecipe recipe;
    private final List<EntryStack> fuel;
    private final List<List<EntryStack>> input;

    public AegisaltRecipeDisplay(AegisaltRecipe recipe) {
        this.recipe = recipe;
        this.fuel = Collections.singletonList(EntryStack.create(ItemRegistry.AEGISALT_CRYSTAL));
        List<List<EntryStack>> ls = new ArrayList<>();
        for(Ingredient ingredient : recipe.getIngredients()) {
            List<EntryStack> stacks = new ArrayList<>();
            for(ItemStack stack : ingredient.getMatchingStacksClient()) {
                stacks.add(EntryStack.create(stack));
            }
            ls.add(Collections.unmodifiableList(stacks));
        }
        ls.add(fuel);
        this.input = Collections.unmodifiableList(ls);
    }

    public List<EntryStack> getFuel() {
        return fuel;
    }

    @Override
    public List<EntryStack> getOutputEntries() {
        return Collections.singletonList(EntryStack.create(this.recipe.getOutput()));
    }

    @Override
    public Identifier getRecipeCategory() {
        return UplandsMod.id("plugins/aegisalt_infusions");
    }

    @Override
    public List<List<EntryStack>> getRequiredEntries() {
        return this.getInputEntries();
    }

    @Override
    public List<List<EntryStack>> getInputEntries() {
        return this.input;
    }
}
