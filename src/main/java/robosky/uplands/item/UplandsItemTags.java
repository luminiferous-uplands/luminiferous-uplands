package robosky.uplands.item;

import net.fabricmc.fabric.api.tag.TagRegistry;
import robosky.uplands.UplandsMod;

import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public final class UplandsItemTags {
	public static final Tag<Item> BOSSROOM_TECHNICAL = register("bossroom_technical");

	private static Tag<Item> register(String name) {
		return TagRegistry.item(UplandsMod.id(name));
	}

	public static void init() {}

	private UplandsItemTags() {}
}
