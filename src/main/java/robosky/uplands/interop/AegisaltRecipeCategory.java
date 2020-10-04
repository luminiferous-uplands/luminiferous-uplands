package robosky.uplands.interop;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.Widget;
import org.jetbrains.annotations.NotNull;
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
    public @NotNull EntryStack getLogo() {
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
    public List<Widget> setupDisplay(AegisaltRecipeDisplay recipeDisplay, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 60, bounds.getCenterY() - 27);
        List<Widget> widgets = new ArrayList<>();
        List<List<EntryStack>> input = recipeDisplay.getInputEntries();
        // I'm pretty sure that EntryWidget is the replacement for SlotWidget, but I'm not sure.
        // It seems like SimpleRecipeEntry is the replacement for Renderer, but it doesn't appear
        // to have an equivalent to fromItemStacks, requiring an explicit input and output list.
        Point p1 = startPoint.getLocation();
        Point p2 = startPoint.getLocation();
        Point p3 = startPoint.getLocation();
        Point p4 = startPoint.getLocation();
        p1.translate(1, 1);
        p2.translate(37, 1);
        p3.translate(68, 19);
        p4.translate(98, 1);
        widgets.add(Widgets.createSlot(p1).entries(input.get(0)));
        widgets.add(Widgets.createSlot(p2).entries(input.get(1)));
        widgets.add(Widgets.createSlot(p3).entries(recipeDisplay.getFuel()));
        widgets.add(Widgets.createSlot(p4).entries(recipeDisplay.getOutputEntries()));
        return widgets;
    }
}
