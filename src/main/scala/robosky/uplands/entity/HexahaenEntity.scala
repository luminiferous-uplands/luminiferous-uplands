package robosky.uplands.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.ai.goal._
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.world.World

class HexahaenEntity(typ: EntityType[HexahaenEntity], world: World) extends HostileEntity(typ, world) {

  override protected def initGoals(): Unit = {
    this.goalSelector.add(4, new MeleeAttackGoal(this, 1.25, false))
    this.goalSelector.add(5, new GoToWalkTargetGoal(this, 1.0))
    this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0, 0.0f))
    this.goalSelector.add(8, new LookAtEntityGoal(this, classOf[PlayerEntity], 8.0f))
    this.goalSelector.add(8, new LookAroundGoal(this))
    this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge())
    this.targetSelector.add(2, new FollowTargetGoal(this, classOf[PlayerEntity], true))
  }

  override protected def initAttributes(): Unit = {
    super.initAttributes()
    this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.23);
  }
}
