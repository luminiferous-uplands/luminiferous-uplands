package robosky.uplands.block;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UplandsOreBlock extends Block {

    private final UplandsOreType oreType;
    public static final OreTypeAegisalt oreTypeAegisalt = new OreTypeAegisalt();
    public static final Map<UplandsOreBlock.UplandsOreType, UplandsOreBlock> oreTypes;

    static  {
        Map<UplandsOreBlock.UplandsOreType, UplandsOreBlock> map = new HashMap<>();
        map.put(oreTypeAegisalt, new UplandsOreBlock(oreTypeAegisalt));
        oreTypes = map;
    }

    public UplandsOreBlock(UplandsOreType oreType) {
        super(oreType.blockSettings);
        this.oreType = oreType;
    }

    @Override
    public void onStacksDropped(BlockState blockState, World world, BlockPos blockPos, ItemStack itemStack) {
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, itemStack) == 0) {
            int xp = this.getExperienceWhenMined(world.random);
            if (xp > 0) {
                this.dropExperience(world, blockPos, xp);
            }
        }
    }

    private int getExperienceWhenMined(Random random) {
        return MathHelper.nextInt(random, oreType.start, oreType.end);
    }

    public static class OreTypeAegisalt extends UplandsOreType {

        public OreTypeAegisalt() {
            super("aegisalt", 2, 5, FabricBlockSettings.of(Material.STONE).strength(3.0F, 3.0F).breakByTool(FabricToolTags.PICKAXES, 2).build());
        }
    }

    public static abstract class UplandsOreType {

        public final String name;
        public final int start;
        public final int end;
        public final Block.Settings blockSettings;

        public UplandsOreType(String name, int start, int end, Block.Settings blockSettings) {
            this.name = name;
            this.start = start;
            this.end = end;
            this.blockSettings = blockSettings;
        }
    }
}
