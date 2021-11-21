package robosky.uplands.advancement;

import com.google.gson.JsonObject;
import robosky.uplands.UplandsMod;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class FlyIntoUplandsCriterion extends AbstractCriterion<FlyIntoUplandsCriterion.Conditions> {

    public static final FlyIntoUplandsCriterion INSTANCE = new FlyIntoUplandsCriterion();
    private static final Identifier ID = UplandsMod.id("fly_into_uplands");

    private FlyIntoUplandsCriterion() {
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return Conditions.INSTANCE;
    }

    public void grant(ServerPlayerEntity player) {
        this.test(player, conditions -> true);
    }

    public static final class Conditions extends AbstractCriterionConditions {

        public static final Conditions INSTANCE = new Conditions();

        public Conditions() {
            super(ID, EntityPredicate.Extended.EMPTY);
        }
    }
}
