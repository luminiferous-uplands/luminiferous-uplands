package robosky.uplands.block.machine

import net.minecraft.recipe.{Recipe, RecipeSerializer, RecipeType}
import net.minecraft.util.registry.Registry
import robosky.uplands.UplandsMod
import robosky.uplands.block.machine.infuser.AegisaltRecipe

object RecipeRegistry {
  def init(): Unit = {}

  val aegisaltRecipe: RecipeType[AegisaltRecipe] =
    register[AegisaltRecipe]("aegisalt_infusion", AegisaltRecipe.Serializer.INSTANCE)

  def register[A <: Recipe[_]](name: String, ser: RecipeSerializer[A]): RecipeType[A] = {
    val identifier = UplandsMod :/ name
    Registry.register[RecipeSerializer[A]](Registry.RECIPE_SERIALIZER, identifier, ser)
    Registry.register[RecipeType[A]](Registry.RECIPE_TYPE, identifier, new RecipeType[A] {
      override def toString: String = name
    })
  }
}
