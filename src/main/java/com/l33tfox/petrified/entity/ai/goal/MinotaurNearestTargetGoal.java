package com.l33tfox.petrified.entity.ai.goal;

import com.l33tfox.petrified.entity.MinotaurEntity;
import com.l33tfox.petrified.sounds.PSounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class MinotaurNearestTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public MinotaurNearestTargetGoal(Mob mob, Class<T> targetType, boolean mustSee) {
        super(mob, targetType, mustSee);
    }

    @Override
    protected void findTarget() {
        super.findTarget();

        if (target instanceof Player) {
            mob.playSound(PSounds.MINOTAUR_ROAR, 4.0F, 1.0F);
        }
    }
}
