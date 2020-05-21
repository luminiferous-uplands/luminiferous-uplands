package robosky.uplands.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.IWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import scala.Int;

import javax.annotation.Nullable;

public class HexahaenEntity extends HostileEntity {

    private static final TrackedData<Integer> STRENGTH;
    public int strength;

    static {
        STRENGTH = DataTracker.registerData(HexahaenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    public HexahaenEntity(EntityType<? extends HexahaenEntity> entityType, World world) {
        super(entityType, world);
        this.strength = this.dataTracker.get(STRENGTH);
    }

    public HexahaenEntity(World world) {
        this(EntityRegistry.HEXAHAEN, world);
    }

    @Override @Nullable
    public EntityData initialize(IWorld world, LocalDifficulty difficulty, SpawnType spawnType, EntityData data, CompoundTag tag) {
        setStrength(this.random.nextInt(5) + 1);
        this.setHealth(this.getMaximumHealth());
        super.initialize(world, difficulty, spawnType, data, tag);
        return data;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.25, false));
        this.goalSelector.add(5, new GoToWalkTargetGoal(this, 1.0));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0, 0.0f));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HexahaenEntity.STRENGTH, 1);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("Strength", this.strength);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        tag.putInt("Strength", this.strength);
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    // Use this when setting the strength value except in the constructor obviously
    private void setStrength(int value) {
        if (value < 1)
            this.strength = 1;
        else
            this.strength = Math.min(value, 5);

        this.dataTracker.set(HexahaenEntity.STRENGTH, this.strength);
        this.experiencePoints = this.strength;
        this.setMaxHealthFromData();
        this.setDamageFromData();
    }

    private void setMaxHealthFromData() {
        // health: 5,    8,     13,    20, 29    right handed
        //         5.56, 10.25, 18.06, 29, 43.06 left handed
        float baseHealth = 4.0F;
        float leftHandedStrengthBonus;
        if (this.isLeftHanded())
            leftHandedStrengthBonus = 1.25F;
        else
            leftHandedStrengthBonus = 1.0F;

        float adjStrength = leftHandedStrengthBonus * this.strength;
        float maxHealth = baseHealth + adjStrength * adjStrength;
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(maxHealth);
    }

    private void setDamageFromData() {
        // damage: 2.5,  3,   3.5,  4, 4.5  right handed
        //         2.75, 3.5, 4.25, 5, 5.75 left handed
        float baseDamage = 2.0F;
        float damageCompressionFactor = 0.5F;
        float leftHandedDamageBonus;
        if (this.isLeftHanded())
            leftHandedDamageBonus = 1.5F;
        else
            leftHandedDamageBonus = 1.0F;
        float damage = baseDamage + this.strength * damageCompressionFactor * leftHandedDamageBonus;
        this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(damage);
    }
}
