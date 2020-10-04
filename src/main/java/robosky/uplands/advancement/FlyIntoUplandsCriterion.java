package robosky.uplands.advancement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;
import robosky.uplands.UplandsMod;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

// TODO: use AbstractCriterion
public final class FlyIntoUplandsCriterion implements Criterion<FlyIntoUplandsCriterion.Conditions> {

    public static final FlyIntoUplandsCriterion INSTANCE = new FlyIntoUplandsCriterion();
    private static final Identifier ID = UplandsMod.id("fly_into_uplands");

    private final Map<PlayerAdvancementTracker, Handler> handlers = new HashMap<>();

    private FlyIntoUplandsCriterion() {
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public void beginTrackingCondition(
        PlayerAdvancementTracker tracker,
        Criterion.ConditionsContainer<Conditions> conditions) {
        handlers.computeIfAbsent(tracker, Handler::new).conditions.add(conditions);
    }

    @Override
    public void endTrackingCondition(
        PlayerAdvancementTracker tracker,
        Criterion.ConditionsContainer<Conditions> conditions) {
        Handler handler = handlers.get(tracker);
        if(handler != null) {
            handler.conditions.remove(conditions);
            if(handler.conditions.isEmpty()) {
                handlers.remove(tracker);
            }
        }
    }

    @Override
    public void endTracking(PlayerAdvancementTracker tracker) {
        handlers.remove(tracker);
    }

    @Override
    public Conditions conditionsFromJson(JsonObject json, AdvancementEntityPredicateDeserializer ctx) {
        return Conditions.INSTANCE;
    }

    public void handle(ServerPlayerEntity player) {
        Handler handler = handlers.get(player.getAdvancementTracker());
        if(handler != null) {
            handler.handle();
        }
    }

    public static final class Conditions extends AbstractCriterionConditions {

        public static final Conditions INSTANCE = new Conditions();

        public Conditions() {
            super(ID, EntityPredicate.Extended.EMPTY);
        }
    }

    private static final class Handler {

        final PlayerAdvancementTracker manager;

        final Set<Criterion.ConditionsContainer<Conditions>> conditions = new HashSet<>();

        Handler(PlayerAdvancementTracker manager) {
            this.manager = manager;
        }

        public void handle() {
            for(Criterion.ConditionsContainer<Conditions> condition : conditions) {
                condition.grant(manager);
            }
        }
    }
}
