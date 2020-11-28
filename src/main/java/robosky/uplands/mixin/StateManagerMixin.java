package robosky.uplands.mixin;

import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;

// Makes properties in state serialized forms optional.
// This allows LU to add a property to water without breaking data packs that use the water block.
@Mixin(StateManager.class)
public class StateManagerMixin {

    @Redirect(
        method = "method_30040",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;fieldOf(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;",
            remap = false
        )
    )
    private static <S extends State<?, S>, T extends Comparable<T>> MapCodec<Property.Value<T>> proxyPropertyCodec(
        Codec<Property.Value<T>> codec,
        String name,
        MapCodec<S> accumulatingCodec,
        Supplier<S> defaultState,
        String nameCopy,
        Property<T> property) {
        return Codec.optionalField(name, codec)
            .xmap(
                // fall back to the corresponding properly in the default state
                opt -> opt.orElse(property.createValue(defaultState.get())),
                // always persist, to be compatible with vanilla potentially reading it back in
                Optional::of
            );
    }
}
