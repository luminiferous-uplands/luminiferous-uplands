package robosky.uplands.interop;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import me.shedaniel.math.api.Point;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.EntryWidget;
import me.shedaniel.rei.gui.widget.Widget;
import robosky.uplands.UplandsMod;
import robosky.uplands.block.machine.MachineRegistry;

import net.minecraft.util.Identifier;

public final class AegisaltRecipeCategory implements RecipeCategory<AegisaltRecipeDisplay> {

    @Override
    public Identifier getIdentifier() {
        return UplandsMod.id("plugins/aegisalt_infusions");
    }

    @Override
    public String getCategoryName() {
        return "Aegisalt Infusion";
    }

    // Is there a way to cast a MachineEntry to an ItemStack?
    @Override
    public EntryStack getLogo() {
        return EntryStack.create(MachineRegistry.AEGISALT_INFUSER.block);
    }

    @Override
    public RecipeEntry getSimpleRenderer(AegisaltRecipeDisplay recipe) {
        return SimpleRecipeEntry.create(
            recipe::getInputEntries,
            recipe::getOutputEntries
        );
    }

    @Override
    public List<Widget> setupDisplay(Supplier<AegisaltRecipeDisplay> recipeDisplaySupplier, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 60, bounds.getCenterY() - 27);
        List<Widget> widgets = new ArrayList<>();
        List<List<EntryStack>> input = recipeDisplaySupplier.get().getInputEntries();
        // I'm pretty sure that EntryWidget is the replacement for SlotWidget, but I'm not sure.
        // It seems like SimpleRecipeEntry is the replacement for Renderer, but it doesn't appear
        // to have an equivalent to fromItemStacks, requiring an explicit input and output list.
        widgets.add(EntryWidget.create(startPoint.x + 1, startPoint.y + 1).entries(input.get(0)));
        widgets.add(EntryWidget.create(startPoint.x + 37, startPoint.y + 1).entries(input.get(1)));
        widgets.add(EntryWidget.create(startPoint.x + 68, startPoint.y + 19).entries(recipeDisplaySupplier.get().getFuel()));
        widgets.add(EntryWidget.create(startPoint.x + 98, startPoint.y + 1).entries(recipeDisplaySupplier.get().getOutputEntries()));
        return widgets;
    }
}
