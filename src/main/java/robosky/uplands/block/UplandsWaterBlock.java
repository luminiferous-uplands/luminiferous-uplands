package robosky.uplands.block;

import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;

public class UplandsWaterBlock {

    public static int MAX_FALL = 40;
    public static Property<Integer> FALL = IntProperty.of("fall", 0, MAX_FALL);
}
