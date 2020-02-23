package robosky.uplands.advancement

import com.google.gson.{JsonDeserializationContext, JsonElement, JsonObject}

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.{AbstractCriterionConditions, Criterion}
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

import robosky.uplands.UplandsMod

import scala.collection.mutable

object FlyIntoUplandsConditions extends AbstractCriterionConditions(FlyIntoUplandsCriterion.getId)

object FlyIntoUplandsCriterion extends Criterion[FlyIntoUplandsConditions.type] {

  private val handlers: mutable.Map[PlayerAdvancementTracker, Handler] = mutable.HashMap()

  override val getId: Identifier = UplandsMod :/ "fly_into_uplands"

  override def beginTrackingCondition(
      tracker: PlayerAdvancementTracker,
      conditions: Criterion.ConditionsContainer[FlyIntoUplandsConditions.type]): Unit = {
    handlers.getOrElseUpdate(tracker, new Handler(tracker)).conditions += conditions
  }

  override def endTrackingCondition(
      tracker: PlayerAdvancementTracker,
      conditions: Criterion.ConditionsContainer[FlyIntoUplandsConditions.type]): Unit = {
    handlers.get(tracker) foreach { handler =>
      handler.conditions -= conditions
      if (handler.conditions.isEmpty) {
        handlers -= tracker
      }
    }
  }

  override def endTracking(tracker: PlayerAdvancementTracker): Unit = {
    handlers -= tracker
  }

  override def conditionsFromJson(json: JsonObject, ctx: JsonDeserializationContext):
    FlyIntoUplandsConditions.type = FlyIntoUplandsConditions

  def handle(player: ServerPlayerEntity): Unit = {
    handlers.get(player.getAdvancementTracker) foreach { _.handle() }
  }

  private class Handler(val manager: PlayerAdvancementTracker) {

    val conditions: mutable.Set[Criterion.ConditionsContainer[FlyIntoUplandsConditions.type]] = mutable.HashSet()

    def handle(): Unit = {
      conditions foreach { _.grant(manager) }
    }
  }
}
