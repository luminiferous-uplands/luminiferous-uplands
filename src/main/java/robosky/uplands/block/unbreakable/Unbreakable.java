package robosky.uplands.block.unbreakable;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

/**
 * An interface that denotes a block as unbreakable. Unbreakable blocks in a boss room
 * are converted to equivalent breakable blocks when a boss is defeated.
 */
public interface Unbreakable {

    /**
     * The breakable version of this block.
     */
    Block base();

    /**
     * Returns the breakable state equivalent of this unbreakable state.
     */
    default BlockState toBreakable(BlockState state) {
        ImmutableMap<Property<?>, Comparable<?>> propValues = state.getEntries();
        BlockState res = this.base().getDefaultState();
        for (Map.Entry<Property<?>, Comparable<?>> entry : propValues.entrySet()) {
            res = with(res, entry.getKey(), entry.getValue());
        }
        return res;
    }

    // public because interface, static because interface static methods are not inherited
    // make private when Minecraft starts using Java 9+
    static <T extends Comparable<T>> BlockState with(BlockState res, Property<T> key, Comparable<?> value) {
        return res.with(key, key.getType().cast(value));
    }
}
