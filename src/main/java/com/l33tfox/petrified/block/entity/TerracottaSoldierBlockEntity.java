package com.l33tfox.petrified.block.entity;

import com.l33tfox.petrified.TerracottaSoldierWeapon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

import java.util.Random;

public class TerracottaSoldierBlockEntity extends BlockEntity {

    private float yaw = 0.0f;
    private boolean eyesActive = false;
    private TerracottaSoldierWeapon weapon;
    private final Random RANDOM = new Random();

    public TerracottaSoldierBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
        super(PBlockEntityTypes.TERRACOTTA_SOLDIER_BLOCK_ENTITY, worldPosition, blockState);
        int weaponIndex = RANDOM.nextInt(TerracottaSoldierWeapon.values().length);
        weapon = TerracottaSoldierWeapon.values()[weaponIndex];
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        setChanged();
    }

    public float getYaw() {
        return yaw;
    }

    public void setEyesActive(boolean eyesActive) {
        this.eyesActive = eyesActive;
        setChanged();

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendBlockUpdated(
                    worldPosition,
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_CLIENTS
            );
        }
    }

    public boolean getEyesActive() {
        return eyesActive;
    }

    public TerracottaSoldierWeapon getWeapon() {
        return weapon;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.putFloat("yaw", yaw);
        output.putString("weapon", weapon.name());
        output.putBoolean("eyesActive", eyesActive);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        yaw = input.getFloatOr("yaw", 0.0f);
        try {
            weapon = TerracottaSoldierWeapon.valueOf(input.getStringOr("weapon", "DIA_SWORD"));
        } catch (IllegalArgumentException e) {
            weapon = TerracottaSoldierWeapon.DIA_SWORD;
        }
        eyesActive = input.getBooleanOr("eyesActive", false);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

}
