package robosky.uplands.entity

import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.boss.{BossBar, ServerBossBar}
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.World

class MegadungeonBossEntity(typ: EntityType[MegadungeonBossEntity], world: World)
    extends HostileEntity(typ, world) {

  val bossBar: ServerBossBar = new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)

  override protected def initGoals(): Unit = {

  }

  override protected def initAttributes(): Unit = {
    super.initAttributes()
    this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.23)
    this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(20.0f)
    this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(5.0f)
  }

  override def onStartedTrackingBy(player: ServerPlayerEntity): Unit = {
    super.onStartedTrackingBy(player)
    bossBar.addPlayer(player)
  }

  override def onStoppedTrackingBy(player: ServerPlayerEntity): Unit = {
    super.onStoppedTrackingBy(player)
    bossBar.removePlayer(player)
  }

  override protected def mobTick(): Unit = {
    bossBar.setPercent(this.getHealth / this.getMaximumHealth)
  }
}
