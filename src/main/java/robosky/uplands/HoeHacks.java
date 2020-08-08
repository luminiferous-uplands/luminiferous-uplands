package robosky.uplands;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;

public final class HoeHacks extends HoeItem {

    private HoeHacks() {
        super(ToolMaterials.WOOD, 0f, new Item.Settings());
    }

    public static void addHoeable(Block source, BlockState tgt) {
        HoeItem.TILLED_BLOCKS.put(source, tgt);
    }
}
