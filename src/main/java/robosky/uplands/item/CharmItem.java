package robosky.uplands.item;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import robosky.uplands.TickableItem;
import robosky.uplands.UplandsMod;

public class CharmItem extends Item implements TickableItem {

    private final StatusEffect effect;
    private final int level;

    public CharmItem(StatusEffect effect, int level) {
        super(new Item.Settings().group(UplandsMod.GROUP).maxCount(1).maxDamage(2400));
        this.effect = effect;
        this.level = level;
    }

    @Override
    public void tick(PlayerEntity entity, ItemStack stack, Hand hand) {
        stack.damage(1, entity, (playerEntity -> { }));
        entity.addStatusEffect(new StatusEffectInstance(effect, 10, level, false, false,
                false));
    }
}
