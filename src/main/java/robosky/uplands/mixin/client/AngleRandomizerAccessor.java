package robosky.uplands.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;

@Environment(EnvType.CLIENT)
@Mixin(ModelPredicateProviderRegistry.AngleInterpolator.class)
public interface AngleRandomizerAccessor {
    @Accessor
    double getValue();
}
