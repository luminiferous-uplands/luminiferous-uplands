package robosky.ether;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public interface MixinHackHooks {
    DimensionType getDimensionType();

    boolean checkParachute(ItemStack stack);

    boolean usePortalHookTo(final Entity entity, final World world);

    default boolean usePortalHookFrom(final Entity entity, final World world) {
        return usePortalHookTo(entity, world);
    }
}
