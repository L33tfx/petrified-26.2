package com.l33tfox.petrified.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.constant.DefaultAnimations;
import com.geckolib.util.GeckoLibUtil;
import com.l33tfox.petrified.entity.ai.goal.MinotaurNearestTargetGoal;
import com.l33tfox.petrified.sounds.PSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.stream.Stream;

public class MinotaurEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    protected MinotaurEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>("Walk/Idle", test -> {
                    test.setControllerSpeed(getAnimationSpeed());
                    return test.setAndContinue(test.isMoving() ? DefaultAnimations.WALK : DefaultAnimations.IDLE);
                }));
        controllerRegistrar.add(DefaultAnimations.genericDeathController());
        controllerRegistrar.add(DefaultAnimations.genericAttackAnimation(DefaultAnimations.ATTACK_SWING));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, (double)100.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.4F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)1.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)1.5F)
                .add(Attributes.ATTACK_DAMAGE, (double)15.0F)
                .add(Attributes.STEP_HEIGHT, 2.2)
                .add(Attributes.FOLLOW_RANGE, (double)32.0F);
    }

    private float getAnimationSpeed() {
        double speed = getDeltaMovement().length();

        return (float) speed * 5;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected boolean shouldTakeDrowningDamage() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    protected float getSoundVolume() {
        return 4.0F;
    }

    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(final DamageSource source) {
        return PSounds.MINOTAUR_GRUNT;
    }

    protected SoundEvent getDeathSound() {
        return PSounds.MINOTAUR_ROAR;
    }

    protected void playStepSound(final BlockPos pos, final BlockState blockState) {
        this.playSound(PSounds.MINOTAUR_STEP, 4.0F, 0.6F + this.random.nextFloat() * 0.2F);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void playAttackSound() {
        super.playAttackSound();
        this.playSound(SoundEvents.RAVAGER_ATTACK, 2.0F, 0.5F);
        this.playSound(PSounds.MINOTAUR_SWING, 2.0F, 1.0F);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.3, true));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(1, new MinotaurNearestTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new MinotaurNearestTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new MinotaurNearestTargetGoal<>(this, IronGolem.class, true));
    }
}
