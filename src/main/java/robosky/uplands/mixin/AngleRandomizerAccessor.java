package robosky.uplands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;

@Mixin(ModelPredicateProviderRegistry.AngleRandomizer.class)
@Environment(EnvType.CLIENT)
public interface AngleRandomizerAccessor {
    @Accessor
    double getValue();
}
