package com.l33tfox.petrified.block.entity.state;

import com.l33tfox.petrified.util.TerracottaSoldierWeapon;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class TerracottaSoldierBlockEntityState extends BlockEntityRenderState {
    public float yaw;
    public boolean eyesActive;
    public TerracottaSoldierWeapon weapon;
    public final ItemStackRenderState weaponRenderState = new ItemStackRenderState();
    public float leftArmXRot;
    public float rightArmXRot;
    public float headXRot;
    public float headYRot;

    public float getLeftArmXRot() {
        return leftArmXRot;
    }

    public float getRightArmXRot() {
        return rightArmXRot;
    }

    public float getHeadXRot() {
        return headXRot;
    }

    public float getHeadYRot() {
        return headYRot;
    }
}
