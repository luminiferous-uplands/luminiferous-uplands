package robosky.ether;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public interface TickableItem {
    void tick(PlayerEntity entity, ItemStack stack, Hand hand);
}
