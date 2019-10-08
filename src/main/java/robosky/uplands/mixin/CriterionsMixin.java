package robosky.uplands.mixin;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.Criterions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import robosky.uplands.UplandsTeleporter;

@Mixin(Criterions.class)
public abstract class CriterionsMixin {

    @Shadow
    private static native <T extends Criterion<?>> T register(T criterion_1);

    static {
        register(UplandsTeleporter.getUplandsCriterion());
    }
}
