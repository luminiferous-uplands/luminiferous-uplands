package robosky.ether;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public interface MixinHackHooks {
    DimensionType getDimensionType();

    Criterion<?> getUplandsCriterion();

    boolean usePortalHookTo(final Entity entity, final World world);

    default boolean usePortalHookFrom(final Entity entity, final World world) {
        return usePortalHookTo(entity, world);
    }
}
