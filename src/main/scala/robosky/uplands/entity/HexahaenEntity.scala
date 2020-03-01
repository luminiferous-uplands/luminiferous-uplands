package robosky.uplands.entity

import net.minecraft.entity.ai.goal._
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.{DataTracker, TrackedData, TrackedDataHandlerRegistry}
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.{EntityData, EntityGroup, EntityType, SpawnType}
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.{IWorld, LocalDifficulty, World}

object HexahaenEntity {
  val STRENGTH: TrackedData[Integer] = DataTracker.registerData(classOf[HexahaenEntity], TrackedDataHandlerRegistry.INTEGER)
}

class HexahaenEntity(typ: EntityType[HexahaenEntity], world: World) extends HostileEntity(typ, world) {

  def strength: Int = this.dataTracker.get(HexahaenEntity.STRENGTH)

  def strength_=(value: Int): Unit = {
    val strength = {
      if (value < 1) 1
      else if (value > 5) 5
      else value
    }
    this.dataTracker.set(HexahaenEntity.STRENGTH, Int.box(strength))
    this.experiencePoints = strength
    setMaxHealthFromData()
    setDamageFromData()
  }

  override def getGroup: EntityGroup = EntityGroup.UNDEAD

  private def initStrength(value: Int): Unit = {
    strength = value
    this.setHealth(this.getMaximumHealth)
  }

  private def setMaxHealthFromData(): Unit = {
    // health: 5,    8,     13,    20, 29    right handed
    //         5.56, 10.25, 18.06, 29, 43.06 left handed
    val baseHealth = 4.0
    val leftHandedStrengthBonus = if (this.isLeftHanded) 1.25 else 1.0
    val adjStrength = leftHandedStrengthBonus * strength
    val maxHealth = baseHealth + adjStrength * adjStrength
    this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(maxHealth)
  }

  private def setDamageFromData(): Unit = {
    // damage: 2.5,  3,   3.5,  4, 4.5  right handed
    //         2.75, 3.5, 4.25, 5, 5.75 left handed
    val baseDamage = 2.0f
    val damageCompressionFactor = 0.5f
    val leftHandedDamageBonus = if (this.isLeftHanded) 1.5f else 1.0f
    val damage = baseDamage + strength * damageCompressionFactor * leftHandedDamageBonus
    this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(damage)
  }

  override def initialize(world: IWorld, localDiff: LocalDifficulty, spawnType: SpawnType, data: EntityData, tag: CompoundTag): EntityData = {
    val res = super.initialize(world, localDiff, spawnType, data, tag)
    initStrength(this.random.nextInt(5) + 1)
    res
  }

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
    this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.23)
  }

  override protected def initDataTracker(): Unit = {
    super.initDataTracker()
    this.dataTracker.startTracking(HexahaenEntity.STRENGTH, Int.box(1))
  }

  override def writeCustomDataToTag(tag: CompoundTag): Unit = {
    super.writeCustomDataToTag(tag)
    tag.putInt("Strength", strength)
  }

  override def readCustomDataFromTag(tag: CompoundTag): Unit = {
    super.readCustomDataFromTag(tag)
    strength = tag.getInt("Strength")
  }
}
