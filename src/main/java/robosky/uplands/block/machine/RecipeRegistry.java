package robosky.uplands.block.machine;

import robosky.uplands.UplandsMod;
import robosky.uplands.block.machine.infuser.AegisaltRecipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class RecipeRegistry {

    private RecipeRegistry() {
    }

    public static void init() {
    }

    public static final RecipeType<AegisaltRecipe> AEGISALT_INFUSION = register("aegisalt_infusion", AegisaltRecipe.Serializer.INSTANCE);

    public static <A extends Recipe<?>> RecipeType<A> register(String name, RecipeSerializer<A> ser) {
        Identifier identifier = UplandsMod.id(name);
        Registry.register(Registry.RECIPE_SERIALIZER, identifier, ser);
        return Registry.register(Registry.RECIPE_TYPE, identifier, new RecipeType<A>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
}
