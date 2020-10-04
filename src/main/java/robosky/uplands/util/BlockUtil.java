package robosky.uplands.util;

import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Utility functions for the block package with no suitable class.
 */
public final class BlockUtil {

    private BlockUtil() {
    }

    private static final BlockStateArgumentType blockStateParser = BlockStateArgumentType.blockState();

    public static BlockState parseState(String str) {
        StringReader rd = new StringReader(str);
        try {
            return blockStateParser.parse(rd).getBlockState();
        } catch(CommandSyntaxException e) {
            return Blocks.AIR.getDefaultState();
        }
    }

    /**
     * Stringifies a block state into a form parsable by commands.
     */
    public static String stringifyState(BlockState state) {
        Identifier id = Registry.BLOCK.getId(state.getBlock());
        ImmutableMap<Property<?>, Comparable<?>> props = state.getEntries();
        String propValues;
        if(!props.isEmpty()) {
            propValues = props.entrySet()
                .stream()
                .map((e) -> stringifyEntry(e.getKey(), e.getValue()))
                .collect(Collectors.joining(",", "[", "]"));
        } else {
            propValues = "";
        }
        return id + propValues;
    }

    private static <T extends Comparable<T>> String stringifyEntry(Property<T> p, Comparable<?> v) {
        return String.format("%s=%s", p.getName(), p.name(p.getType().cast(v)));
    }
}
