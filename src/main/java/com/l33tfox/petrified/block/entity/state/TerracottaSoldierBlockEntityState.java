package com.l33tfox.petrified.block.entity.state;

import com.l33tfox.petrified.TerracottaSoldierWeapon;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class TerracottaSoldierBlockEntityState extends BlockEntityRenderState {
    public boolean isTopBlock;
    public float yaw;
    public boolean eyesActive;
    public TerracottaSoldierWeapon weapon;
    public final ItemStackRenderState weaponRenderState = new ItemStackRenderState();
}
