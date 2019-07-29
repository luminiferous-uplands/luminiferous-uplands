package robosky.ether.block.machine

import net.minecraft.recipe.{Recipe, RecipeSerializer, RecipeType}
import net.minecraft.util.registry.Registry
import robosky.ether.UplandsMod
import robosky.ether.block.machine.infuser.AegisaltRecipe

object RecipeRegistry {
  val aegisaltRecipe: RecipeType[AegisaltRecipe] =
    register[AegisaltRecipe]("aegisalt_infusion", AegisaltRecipe.Serializer)

  def register[A <: Recipe[_]](name: String, ser: RecipeSerializer[A]): RecipeType[A] = {
    val identifier = UplandsMod :/ name
    Registry.register[RecipeSerializer[A]](Registry.RECIPE_SERIALIZER, identifier, ser)
    Registry.register[RecipeType[A]](Registry.RECIPE_TYPE, identifier, new RecipeType[A] {
      override def toString: String = name
    })
  }
}
