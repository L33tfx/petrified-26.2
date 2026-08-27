package com.l33tfox.petrified.entity.state;

import com.l33tfox.petrified.entity.TerracottaSoldierEntity;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;

public class TerracottaSoldierEntityState extends IllagerRenderState {
    public TerracottaSoldierEntity.SoldierArmPose armPose;

    public TerracottaSoldierEntityState() {
        armPose = TerracottaSoldierEntity.SoldierArmPose.NEUTRAL;
    }
}
