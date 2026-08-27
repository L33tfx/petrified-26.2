package com.l33tfox.petrified.entity;

import com.l33tfox.petrified.block.PBlocks;
import com.l33tfox.petrified.block.TerracottaSoldierBlock;
import com.l33tfox.petrified.block.entity.PBlockEntityTypes;
import com.l33tfox.petrified.block.entity.TerracottaSoldierBlockEntity;
import com.l33tfox.petrified.block.entity.renderer.PBlockEntityRenderers;
import com.l33tfox.petrified.util.TerracottaSoldierWeapon;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

// Some code based on Pillager and Vindicator classes
public class TerracottaSoldierEntity extends AbstractIllager implements CrossbowAttackMob {
    private TerracottaSoldierWeapon weapon;
    private final Random RANDOM = new Random();
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW =
            SynchedEntityData.defineId(TerracottaSoldierEntity.class, EntityDataSerializers.BOOLEAN);

    public TerracottaSoldierEntity(EntityType<? extends AbstractIllager> type, Level level) {
        super(type, level);

        int weaponIndex = RANDOM.nextInt(TerracottaSoldierWeapon.values().length);
        weapon = TerracottaSoldierWeapon.values()[weaponIndex];
        setWeaponItemSlot();
    }

    public TerracottaSoldierEntity(EntityType<? extends AbstractIllager> type, Level level, TerracottaSoldierWeapon weapon) {
        super(type, level);

        this.weapon = weapon;
        setCanJoinRaid(false);
        setWeaponItemSlot();
    }

    private void setWeaponItemSlot() {
        if (weapon == TerracottaSoldierWeapon.CROSSBOW) {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
        } else if (weapon == TerracottaSoldierWeapon.DIA_AXE) {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
        } else if (weapon == TerracottaSoldierWeapon.DIA_SPEAR) {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SPEAR));
        } else if (weapon == TerracottaSoldierWeapon.DIA_SWORD) {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        }
    }

    public static enum SoldierArmPose {
        CROSSED,
        ATTACKING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        CELEBRATING,
        NEUTRAL,
        SPEAR;

        private SoldierArmPose() {
        }
    }

    protected void defineSynchedData(final SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    public void setWeapon(TerracottaSoldierWeapon weapon) {
        this.weapon = weapon;
        setWeaponItemSlot();
    }

    public TerracottaSoldierWeapon getWeapon() {
        return weapon;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Creaking.class, 8.0F, 1.0, 1.2));
        this.goalSelector.addGoal(2, new SpearUseGoal(this, 1.0, 1.0, 10.0F, 2.0F));
        this.goalSelector.addGoal(2, new RangedCrossbowAttackGoal(this, 1.0, 8.0F));
        this.goalSelector.addGoal(4, new HoldGroundAttackGoal(this, 10.0F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{Raider.class})).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355)
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData spawnGroupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
        this.getNavigation().setCanOpenDoors(true);
        this.populateDefaultEquipmentSlots(random, difficulty);
        this.populateDefaultEquipmentEnchantments(level, random, difficulty);
        return spawnGroupData;
    }

    @Override
    protected void populateDefaultEquipmentSlots(final RandomSource random, final DifficultyInstance difficulty) {
        setWeaponItemSlot();
    }

    public SoldierArmPose getSoldierArmPose() {
        if (this.isChargingCrossbow()) {
            return SoldierArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(Items.CROSSBOW)) {
            return SoldierArmPose.CROSSBOW_HOLD;
        } else if (isHolding(itemStack -> itemStack.is(ItemTags.SPEARS))) {
            return SoldierArmPose.SPEAR;
        } else {
            return this.isAggressive() ? SoldierArmPose.ATTACKING : SoldierArmPose.NEUTRAL;
        }
    }

    // Modified from Mob's checkDespawn() to add block entity spawning mechanics
    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && !this.getType().isAllowedInPeaceful()) {
            this.discard();
        } else if (!this.isPersistenceRequired() && !this.requiresCustomPersistence()) {
            Entity player = this.level().getNearestPlayer(this, -1.0);
            if (player != null) {
                double distSqr = player.distanceToSqr(this);
                int instantDespawnDistance = this.getType().getCategory().getDespawnDistance();
                int despawnDistanceSqr = instantDespawnDistance * instantDespawnDistance;
                if (distSqr > (double)despawnDistanceSqr && this.removeWhenFarAway(distSqr)) {
                    revertToBlock();
                }

                int noDespawnDistance = this.getType().getCategory().getNoDespawnDistance();
                int noDespawnDistanceSqr = noDespawnDistance * noDespawnDistance;
                if (this.noActionTime > 600 && this.random.nextInt(800) == 0 && distSqr > (double)noDespawnDistanceSqr && this.removeWhenFarAway(distSqr)) {
                    revertToBlock();
                } else if (distSqr < (double)noDespawnDistanceSqr) {
                    this.noActionTime = 0;
                }
            }

        } else {
            this.noActionTime = 0;
        }
    }

    public void revertToBlock() {
        Level level = level();
        if (!level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            BlockState statueBlock = PBlocks.TERRACOTTA_SOLDIER.defaultBlockState();
            serverLevel.setBlock(blockPosition(), statueBlock, 3);
            serverLevel.gameEvent(GameEvent.BLOCK_PLACE, blockPosition(), GameEvent.Context.of(this, statueBlock));

            Optional<TerracottaSoldierBlockEntity> optionalBlockEntity = serverLevel.getBlockEntity(blockPosition(), PBlockEntityTypes.TERRACOTTA_SOLDIER_BLOCK_ENTITY);
            if (optionalBlockEntity.isPresent()) {
                TerracottaSoldierBlockEntity statueBlockEntity = optionalBlockEntity.orElseThrow();
                statueBlockEntity.setYaw(yBodyRot);
                statueBlockEntity.setWeapon(weapon);
            }

            discard();
        }
    }

    public boolean isChargingCrossbow() {
        return (Boolean)this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(final boolean isCharging) {
        this.entityData.set(IS_CHARGING_CROSSBOW, isCharging);
    }

    @Override
    public void onCrossbowAttackPerformed() {

    }

    @Override
    public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {

    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }

    @Override
    public boolean canJoinRaid() {
        return false;
    }

    @Override
    public boolean canJoinPatrol() {
        return false;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        performCrossbowAttack(this, 1.6F);
    }
}
