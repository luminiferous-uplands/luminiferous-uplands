package robosky.uplands.block;

import net.fabricmc.fabric.api.tag.TagRegistry;
import robosky.uplands.UplandsMod;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

public final class UplandsBlockTags {
	public static final Tag<Block> AZOTE_MUSHROOM_SPREADABLE = register("azote_mushroom_spreadable");
	public static final Tag<Block> PLANTABLE_ON = register("plantable_on");

	public static void init() {}

	private static Tag<Block> register(String name) {
		return TagRegistry.block(UplandsMod.id(name));
	}

	private UplandsBlockTags() {}
}
