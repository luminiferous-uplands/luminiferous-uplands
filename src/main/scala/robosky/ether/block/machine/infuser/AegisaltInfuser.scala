package robosky.ether.block.machine.infuser

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.fabricmc.fabric.api.tools.FabricToolTags
import net.minecraft.block.{BlockState, InventoryProvider, Material}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.{Inventory, SidedInventory}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.recipe.{Recipe, RecipeFinder, RecipeInputProvider}
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.util.{Identifier, Tickable}
import net.minecraft.world.IWorld
import robosky.ether.block.machine.base.{BaseMachineBlock, BaseMachineBlockEntity, MachineGUIBlockActivationSkeleton}
import robosky.ether.block.machine.{MachineRegistry, RecipeRegistry}
import robosky.ether.item.ItemRegistry

import scala.reflect.{ClassTag, classTag}

class AegisaltInfuser extends BaseMachineBlockEntity(MachineRegistry.aegisaltInfuser.blockEntityType)
  with InventoryProvider with RecipeInputProvider with Tickable with BlockEntityClientSerializable {
  self =>
  val inputStacks: Array[ItemStack] = Array(ItemStack.EMPTY, ItemStack.EMPTY)
  private val MAX_BURN_TIME = 100
  var crystalStack: ItemStack = ItemStack.EMPTY
  var outputStack: ItemStack = ItemStack.EMPTY
  var burnTime: Int = 0
  var recipe: Option[AegisaltRecipe] = None

  override def getInventory(var1: BlockState,
    var2: IWorld,
    var3: BlockPos): SidedInventory = InfuserInventory

  override def provideRecipeInputs(var1: RecipeFinder): Unit =
    inputStacks.foreach(var1.addItem)

  override def tick(): Unit = if (!world.isClient) {
    val recipe = this.recipe.getOrElse(
      world.getRecipeManager
        .getFirstMatch[Inventory, AegisaltRecipe](
          RecipeRegistry.aegisaltRecipe,
          InfuserInventory,
          world
        )
        .orElse(null)
    )
    var shouldMark = false
    if (!burning && canAcceptOutput(recipe) && !crystalStack.isEmpty) {
      this.recipe = Some(recipe)
      println(s"Started crafting ${recipe.getId}")
      inputStacks.foreach(_.decrement(1))
      crystalStack.decrement(1)
      burnTime = 0
      shouldMark = true
    }
    if (burning && canAcceptOutput(recipe)) {
      burnTime += 1
      if (burnTime > MAX_BURN_TIME) {
        val out = this.recipe.get.getOutput
        if (outputStack.isEmpty) {
          outputStack = out.copy()
        } else if (outputStack.isItemEqual(out)) {
          outputStack.increment(out.getCount)
        }
        shouldMark = true
        this.recipe = None
      }
    } else {
      shouldMark = true
      burnTime = 0
    }
    if (shouldMark) markDirty()
  }

  def burning: Boolean = recipe.nonEmpty


  def canAcceptOutput(recipe_1: Recipe[Inventory]): Boolean =
    if (!inputStacks.forall(_.isEmpty) && recipe_1 != null) {
      val output: ItemStack = recipe_1.getOutput
      if (output.isEmpty) false
      else {
        if (outputStack.isEmpty) true
        else if (!outputStack.isItemEqualIgnoreDamage(output)) false
        else if (outputStack.getCount < 64 && outputStack.getCount < outputStack.getMaxCount)
          true
        else outputStack.getCount < output.getMaxCount
      }
    } else false

  object InfuserInventory extends SidedInventory {
    override def getInvAvailableSlots(var1: Direction): Array[Int] =
      (0 to 3).toArray

    override def canInsertInvStack(var1: Int,
      var2: ItemStack,
      var3: Direction): Boolean =
      if (var1 == 2 && var3 == Direction.UP) false
      else isValidInvStack(var1, var2)

    override def isValidInvStack(var1: Int, stack: ItemStack): Boolean =
      var1 match {
        case 3 => false
        case 2 => stack.getItem == ItemRegistry.AEGISALT_CRYSTAL
        case _ => true
      }

    override def canExtractInvStack(var1: Int,
      var2: ItemStack,
      var3: Direction): Boolean = var1 == 3

    override def getInvSize: Int = 4

    override def isInvEmpty: Boolean =
      crystalStack.isEmpty && inputStacks.forall(_.isEmpty) && outputStack.isEmpty

    override def takeInvStack(var1: Int, var2: Int): ItemStack = {
      val s1 =
        if (getInvStack(var1).isEmpty || var2 <= 0) ItemStack.EMPTY
        else getInvStack(var1).split(var2)
      if (!s1.isEmpty) markDirty()
      s1
    }

    override def markDirty(): Unit = self.markDirty()

    override def getInvStack(var1: Int): ItemStack = var1 match {
      case 3 => outputStack
      case 2 => crystalStack
      case _ => inputStacks(var1)
    }

    override def removeInvStack(var1: Int): ItemStack = {
      val s1 = getInvStack(var1)
      if (s1.isEmpty)
        return ItemStack.EMPTY
      setInvStack(var1, ItemStack.EMPTY)
      s1
    }

    override def setInvStack(var1: Int, var2: ItemStack): Unit = var1 match {
      case 3 => outputStack = var2
      case 2 => crystalStack = var2
      case _ => inputStacks(var1) = var2
    }

    override def canPlayerUseInv(player: PlayerEntity): Boolean =
      player.squaredDistanceTo(
        pos.getX.toDouble + 0.5D,
        pos.getY.toDouble + 0.5D,
        pos.getZ.toDouble + 0.5D
      ) <= 64.0D

    override def clear(): Unit = {
      outputStack = ItemStack.EMPTY
      (0 to 1).foreach(inputStacks(_) = ItemStack.EMPTY)
      crystalStack = ItemStack.EMPTY
    }
  }

  override def fromClientTag(tag: CompoundTag): Unit = {
    recipe = if (tag.containsKey("Recipe"))
      Some(world.getRecipeManager.get(new Identifier(tag.getString("Recipe"))).get().asInstanceOf[AegisaltRecipe])
    else
      None

    (0 to 1).map(i => s"InputStack$i").map(tag.getCompound).map(ItemStack.fromTag).zipWithIndex
      .foreach { case (s, i) => inputStacks(i) = s }
    outputStack = ItemStack.fromTag(tag.getCompound("OutputStack"))
    crystalStack = ItemStack.fromTag(tag.getCompound("CrystalStack"))
    burnTime = tag.getInt("BurnTime")
  }

  override def toClientTag(tag: CompoundTag): CompoundTag = {
    recipe.foreach(r => tag.putString("Recipe", r.getId.toString))
    inputStacks.zipWithIndex.foreach { case (s, i) => tag.put(s"InputStack$i", s.toTag(new CompoundTag)) }
    tag.put("OutputStack", outputStack.toTag(new CompoundTag))
    tag.put("CrystalStack", crystalStack.toTag(new CompoundTag))
    tag.putInt("BurnTime", burnTime)
    tag
  }
}

object AegisaltInfuserBlock extends BaseMachineBlock(FabricBlockSettings.of(Material.STONE)
  .strength(1.5f, 6f).breakByTool(FabricToolTags.PICKAXES, 1)
  .sounds(BlockSoundGroup.STONE).build()) with MachineGUIBlockActivationSkeleton[AegisaltInfuser] {
  override protected def beCTag: ClassTag[AegisaltInfuser] = classTag
}
