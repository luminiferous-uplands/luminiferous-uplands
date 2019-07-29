package robosky.ether.block.machine.infuser

import com.google.gson.{JsonObject, JsonParseException}
import net.fabricmc.api.{EnvType, Environment}
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe._
import net.minecraft.util.{DefaultedList, Identifier, JsonHelper, PacketByteBuf}
import net.minecraft.world.World
import robosky.ether.block.machine.{MachineRegistry, RecipeRegistry}

object AegisaltRecipe {

  object Serializer extends RecipeSerializer[AegisaltRecipe] {

    override def read(identifier_1: Identifier, jsonObject_1: JsonObject): AegisaltRecipe = {
      val group = JsonHelper.getString(jsonObject_1, "group")
      val array = JsonHelper.getArray(jsonObject_1, "ingredients")
      val ingredients = for {
        i <- 0 until array.size()
        ingredient_1 = Ingredient.fromJson(
          array.get(i)
        )
        if !ingredient_1.isEmpty
      } yield ingredient_1
      if (ingredients.size != 2)
        throw new JsonParseException(
          "Wrong number of ingredients for aegisalt recipe!"
        )
      else {
        val output = ShapedRecipe.getItemStack(
          JsonHelper.getObject(jsonObject_1, "result")
        )
        new AegisaltRecipe(identifier_1, group, ingredients, output)
      }
    }

    override def read(id: Identifier, buf: PacketByteBuf): AegisaltRecipe = {
      val group = buf.readString(32767)
      val ingredients = for (_ <- 0 to 1) yield Ingredient.fromPacket(buf)
      val output = buf.readItemStack()
      new AegisaltRecipe(id, group, ingredients, output)
    }

    override def write(var1: PacketByteBuf, var2: AegisaltRecipe): Unit = {
      var1.writeString(var2.getGroup)
      var2.ingredients.foreach(_.write(var1))
      var1.writeItemStack(var2.getOutput)
    }
  }

}

class AegisaltRecipe(id: Identifier, group: String, val ingredients: IndexedSeq[Ingredient], output: ItemStack)
  extends Recipe[Inventory] {
  override def matches(var1: Inventory, var2: World): Boolean =
    (0 to 1).forall(i => ingredients(i).method_8093(var1.getInvStack(i)))

  override def craft(var1: Inventory): ItemStack = output.copy()

  override def fits(var1: Int, var2: Int): Boolean = true

  override def getOutput: ItemStack = output

  override def getId: Identifier = id

  override def getSerializer: RecipeSerializer[_] = AegisaltRecipe.Serializer

  override def getType: RecipeType[_] = RecipeRegistry.aegisaltRecipe

  override def getRemainingStacks(
    inventory_1: Inventory
  ): DefaultedList[ItemStack] = super.getRemainingStacks(inventory_1)

  override def getPreviewInputs: DefaultedList[Ingredient] = {
    val l = DefaultedList.of[Ingredient]
    ingredients.foreach(l.add)
    l
  }

  override def isIgnoredInRecipeBook: Boolean = true

  @Environment(EnvType.CLIENT)
  override def getGroup: String = group

  @Environment(EnvType.CLIENT)
  override def getRecipeKindIcon: ItemStack =
    new ItemStack(MachineRegistry.aegisaltInfuser.machine.b)
}
