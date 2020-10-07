package robosky.uplands.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.Identifier;

@Mixin(SkyProperties.class)
public interface SkyPropertiesAccessor {
    @Accessor
    static Object2ObjectMap<Identifier, SkyProperties> getBY_IDENTIFIER() {
        throw new UnsupportedOperationException();
    }
}
