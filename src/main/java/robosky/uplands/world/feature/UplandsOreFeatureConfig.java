package robosky.uplands.world.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.FeatureConfig;

public final class UplandsOreFeatureConfig implements FeatureConfig {

    private final int size;
    private final int minHeight;
    private final int maxHeight;
    private final BlockState state;

    public UplandsOreFeatureConfig(int size, int minHeight, int maxHeight, BlockState state) {
        this.size = size;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.state = state;
    }

    public static UplandsOreFeatureConfig deserialize(Dynamic<?> dyn) {
        int size = dyn.get("size").asInt(0);
        int min = dyn.get("min").asInt(0);
        int max = dyn.get("max").asInt(0);
        BlockState state = dyn.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        return new UplandsOreFeatureConfig(size, min, max, state);
    }

    public int getSize() {
        return size;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public BlockState getState() {
        return state;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
            ops.createString("size"), ops.createInt(size),
            ops.createString("min"), ops.createInt(minHeight),
            ops.createString("max"), ops.createInt(maxHeight),
            ops.createString("state"), BlockState.serialize(ops, state).getValue()
        )));
    }
}
