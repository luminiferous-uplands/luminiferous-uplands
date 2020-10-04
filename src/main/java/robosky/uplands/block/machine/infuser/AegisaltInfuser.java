package robosky.uplands.block.machine.infuser;

import robosky.uplands.block.machine.MachineRegistry;
import robosky.uplands.block.machine.RecipeRegistry;
import robosky.uplands.block.machine.base.BaseMachineBlockEntity;
import robosky.uplands.item.ItemRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

public class AegisaltInfuser extends BaseMachineBlockEntity
    implements InventoryProvider, RecipeInputProvider, Tickable, BlockEntityClientSerializable {

    public AegisaltInfuser() {
        super(MachineRegistry.AEGISALT_INFUSER.type);
    }

    private final ItemStack[] inputStacks = { ItemStack.EMPTY, ItemStack.EMPTY };
    private static final int MAX_BURN_TIME = 100;
    private ItemStack crystalStack = ItemStack.EMPTY;
    ItemStack outputStack = ItemStack.EMPTY;
    int burnTime = 0;
    AegisaltRecipe recipe = null;
    boolean burning = false;

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return new InfuserInventory();
    }

    @Override
    public void provideRecipeInputs(RecipeFinder finder) {
        for(ItemStack stack : inputStacks) {
            finder.addItem(stack);
        }
    }

    @Override
    public void tick() {
        if(!world.isClient) {
            if(recipe == null) {
                recipe = world.getRecipeManager()
                    .getFirstMatch(RecipeRegistry.AEGISALT_INFUSION, new InfuserInventory(), world)
                    .orElse(null);
            }
            boolean shouldMark = false;
            if(!burning && canAcceptOutput(recipe) && !crystalStack.isEmpty()) {
                System.out.format("Started crafting %s%n", recipe.getId());
                for(ItemStack stack : inputStacks) {
                    stack.decrement(1);
                }
                crystalStack.decrement(1);
                burnTime = 0;
                shouldMark = true;
            }
            if(burning && canAcceptOutput(recipe)) {
                burnTime += 1;
                if(burnTime > MAX_BURN_TIME) {
                    ItemStack out = this.recipe.getOutput();
                    if(outputStack.isEmpty()) {
                        outputStack = out.copy();
                    } else if(outputStack.isItemEqual(out)) {
                        outputStack.increment(out.getCount());
                    }
                    shouldMark = true;
                    this.recipe = null;
                }
            } else {
                shouldMark = true;
                burnTime = 0;
            }
            if(shouldMark) {
                markDirty();
            }
        }
    }

    private boolean canAcceptOutput(Recipe<Inventory> recipe_1) {
        if((!inputStacks[0].isEmpty() || !inputStacks[1].isEmpty()) && recipe_1 != null) {
            ItemStack output = recipe_1.getOutput();
            if(output.isEmpty()) {
                return false;
            } else {
                if(outputStack.isEmpty()) {
                    return true;
                } else if(!outputStack.isItemEqualIgnoreDamage(output)) {
                    return false;
                } else if(outputStack.getCount() < 64 && outputStack.getCount() < outputStack.getMaxCount()) {
                    return true;
                } else {
                    return outputStack.getCount() < output.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    private final class InfuserInventory implements SidedInventory {

        @Override
        public int[] getAvailableSlots(Direction dir) {
            return new int[] { 0, 1, 2, 3 };
        }

        @Override
        public boolean canInsert(int slot, ItemStack stack, Direction dir) {
            if(slot == 2 && dir == Direction.UP) {
                return false;
            } else {
                return isValid(slot, stack);
            }
        }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            switch(slot) {
            case 3:
                return false;
            case 2:
                return stack.getItem() == ItemRegistry.AEGISALT_CRYSTAL;
            default:
                return true;
            }
        }

        @Override
        public boolean canExtract(int slot, ItemStack var2, Direction var3) {
            return slot == 3;
        }

        @Override
        public int size() {
            return 4;
        }

        @Override
        public boolean isEmpty() {
            return crystalStack.isEmpty() && inputStacks[0].isEmpty() && inputStacks[1].isEmpty() && outputStack.isEmpty();
        }

        @Override
        public ItemStack removeStack(int slot, int count) {
            ItemStack s1;
            if(getStack(slot).isEmpty() || count <= 0) {
                s1 = ItemStack.EMPTY;
            } else {
                s1 = getStack(slot).split(count);
            }
            if(!s1.isEmpty()) {
                markDirty();
            }
            return s1;
        }

        @Override
        public void markDirty() {
            AegisaltInfuser.this.markDirty();
        }

        @Override
        public ItemStack getStack(int slot) {
            switch(slot) {
            case 3:
                return outputStack;
            case 2:
                return crystalStack;
            default:
                return inputStacks[slot];
            }
        }

        @Override
        public ItemStack removeStack(int slot) {
            ItemStack s1 = getStack(slot);
            if(s1.isEmpty()) {
                return ItemStack.EMPTY;
            }
            setStack(slot, ItemStack.EMPTY);
            return s1;
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            switch(slot) {
            case 3:
                outputStack = stack;
                break;
            case 2:
                crystalStack = stack;
                break;
            default:
                inputStacks[slot] = stack;
                break;
            }
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            return player.squaredDistanceTo(
                pos.getX() + 0.5D,
                pos.getY() + 0.5D,
                pos.getZ() + 0.5D
            ) <= 64.0D;
        }

        @Override
        public void clear() {
            outputStack = ItemStack.EMPTY;
            inputStacks[0] = inputStacks[1] = ItemStack.EMPTY;
            crystalStack = ItemStack.EMPTY;
        }
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        if(tag.contains("Recipe")) {
            recipe = (AegisaltRecipe)world.getRecipeManager().get(new Identifier(tag.getString("Recipe"))).orElse(null);
        } else {
            recipe = null;
        }
        inputStacks[0] = ItemStack.fromTag(tag.getCompound("InputStack0"));
        inputStacks[1] = ItemStack.fromTag(tag.getCompound("InputStack1"));
        outputStack = ItemStack.fromTag(tag.getCompound("OutputStack"));
        crystalStack = ItemStack.fromTag(tag.getCompound("CrystalStack"));
        burnTime = tag.getInt("BurnTime");
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        if(recipe != null) {
            tag.putString("Recipe", recipe.getId().toString());
        }
        tag.put("InputStack0", inputStacks[0].toTag(new CompoundTag()));
        tag.put("InputStack1", inputStacks[1].toTag(new CompoundTag()));
        tag.put("OutputStack", outputStack.toTag(new CompoundTag()));
        tag.put("CrystalStack", crystalStack.toTag(new CompoundTag()));
        tag.putInt("BurnTime", burnTime);
        return tag;
    }
}
