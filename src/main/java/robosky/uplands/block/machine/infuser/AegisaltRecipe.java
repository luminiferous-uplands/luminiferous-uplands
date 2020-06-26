package robosky.uplands.block.machine.infuser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import robosky.uplands.block.machine.MachineRegistry;
import robosky.uplands.block.machine.RecipeRegistry;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class AegisaltRecipe implements Recipe<Inventory> {

    private final Identifier id;
    private final String group;
    private final List<Ingredient> ingredients;
    private final ItemStack output;

    public AegisaltRecipe(Identifier id, String group, List<Ingredient> ingredients, ItemStack output) {
        this.id = id;
        this.group = group;
        this.ingredients = ingredients;
        this.output = output;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        ItemStack one = inv.getInvStack(0);
        ItemStack two = inv.getInvStack(1);
        return ingredientsMatch(one, two) || ingredientsMatch(two, one);
    }

    private boolean ingredientsMatch(ItemStack one, ItemStack two) {
        return ingredients.get(0).test(one) && ingredients.get(1).test(two);
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return output.copy();
    }

    @Override
    public boolean fits(int i, int j) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.AEGISALT_INFUSION;
    }

    @Override
    public DefaultedList<Ingredient> getPreviewInputs() {
        DefaultedList<Ingredient> l = DefaultedList.of();
        l.addAll(ingredients);
        return l;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(MachineRegistry.AEGISALT_INFUSER.block);
    }

    public static class Serializer implements RecipeSerializer<AegisaltRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public AegisaltRecipe read(Identifier id, JsonObject json) {
            String group = JsonHelper.getString(json, "group");
            JsonArray array = JsonHelper.getArray(json, "ingredients");
            List<Ingredient> ingredients = new ArrayList<>(2);
            for(JsonElement elem : array) {
                Ingredient ingredient = Ingredient.fromJson(elem);
                if(!ingredient.isEmpty()) {
                    ingredients.add(ingredient);
                }
            }
            if(ingredients.size() != 2) {
                throw new JsonParseException("Wrong number of ingredients for aegisalt recipe!");
            } else {
                ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
                return new AegisaltRecipe(id, group, ingredients, output);
            }
        }

        @Override
        public AegisaltRecipe read(Identifier id, PacketByteBuf buf) {
            String group = buf.readString(32767);
            List<Ingredient> ingredients = Arrays.asList(Ingredient.fromPacket(buf), Ingredient.fromPacket(buf));
            ItemStack output = buf.readItemStack();
            return new AegisaltRecipe(id, group, ingredients, output);
        }

        @Override
        public void write(PacketByteBuf buf, AegisaltRecipe recipe) {
            buf.writeString(recipe.getGroup());
            recipe.ingredients.forEach(e -> e.write(buf));
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
