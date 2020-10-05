package robosky.uplands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;

@Mixin(Criteria.class)
public abstract class CriterionsMixin {

    @Shadow
    private static native <T extends Criterion<?>> T register(T criterion_1);

    static {
//        register(UplandsTeleporter.getUplandsCriterion());
    }
}
