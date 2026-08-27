package com.l33tfox.petrified.block.entity;

import com.l33tfox.petrified.block.TerracottaSoldierBlock;
import com.l33tfox.petrified.util.TerracottaSoldierWeapon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.jmx.Server;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.Random;

public class TerracottaSoldierBlockEntity extends BlockEntity implements GameEventListener.Provider<TerracottaSoldierBlockEntity.BlockChangeListener>{

    private float yaw = 0.0f;
    private boolean eyesActive = false;
    private TerracottaSoldierWeapon weapon;
    private final Random RANDOM = new Random();
    private final BlockChangeListener listener;
    public float leftArmXRot;
    public float rightArmXRot;
    public float headXRot;
    public float headYRot;

    public TerracottaSoldierBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
        super(PBlockEntityTypes.TERRACOTTA_SOLDIER_BLOCK_ENTITY, worldPosition, blockState);
        listener = new BlockChangeListener();
        randomizeWeapon();
        randomizePose();
    }

    private void randomizePose() {
        leftArmXRot = (float) ((RANDOM.nextFloat() * 2 * Math.PI / 3) - Math.PI / 3);
        rightArmXRot = -1 * leftArmXRot;
        headXRot = (float) ((RANDOM.nextFloat() * 2 * Math.PI / 3) - Math.PI / 3);
        headYRot = (float) ((RANDOM.nextFloat() * Math.PI / 2) - Math.PI / 4);
    }

    private void randomizeWeapon() {
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

    public void setWeapon(TerracottaSoldierWeapon weapon) {
        this.weapon = weapon;
        setChanged();
    }

    public void awaken() {
        Block block = getBlockState().getBlock();
        if (block instanceof TerracottaSoldierBlock soldierBlock && !level.isClientSide()) {
            soldierBlock.spawnAliveSoldier((ServerLevel) getLevel(), getBlockPos(), this);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.putFloat("yaw", yaw);
        output.putString("weapon", weapon.name());
        output.putBoolean("eyesActive", eyesActive);
        output.putFloat("leftarmxrot", leftArmXRot);
        output.putFloat("rightarmxrot", rightArmXRot);
        output.putFloat("headxrot", headXRot);
        output.putFloat("headyrot", headYRot);
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
        leftArmXRot = input.getFloatOr("leftarmxrot", 0);
        rightArmXRot = input.getFloatOr("rightarmxrot", 0);
        headXRot = input.getFloatOr("headxrot", 0);
        headYRot = input.getFloatOr("headyrot", 0);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public BlockChangeListener getListener() {
        return listener;
    }

    public class BlockChangeListener implements GameEventListener {
        public static final int LISTENER_RANGE = 8;
        protected final BlockPos blockPos;
        private final PositionSource positionSource;

        public BlockChangeListener() {
            blockPos = getBlockPos();
            positionSource = new BlockPositionSource(blockPos);
        }

        @Override
        public PositionSource getListenerSource() {
            return positionSource;
        }

        @Override
        public int getListenerRadius() {
            return LISTENER_RANGE;
        }

        @Override
        public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
            if (event.is(GameEvent.BLOCK_PLACE) || event.is(GameEvent.BLOCK_OPEN) || event.is(GameEvent.BLOCK_DESTROY)) {
                Optional<Vec3> listenerSourcePos = getListenerSource().getPosition(level);
                if (listenerSourcePos.isEmpty()) {
                    return false;
                }

                Vec3 destination = (Vec3)listenerSourcePos.get();
                if (isOccluded(level, sourcePosition, destination)) {
                    return false;
                }

                awaken();
                return true;
            }

            return false;
        }

        // Adapted from VibrationSystem.isOccluded()
        private static boolean isOccluded(final Level level, final Vec3 origin, final Vec3 dest) {
            Vec3 from = new Vec3((double) Mth.floor(origin.x) + 0.5, (double)Mth.floor(origin.y) + 0.5, (double)Mth.floor(origin.z) + 0.5);
            Vec3 to = new Vec3((double)Mth.floor(dest.x) + 0.5, (double)Mth.floor(dest.y) + 0.5, (double)Mth.floor(dest.z) + 0.5);
            Direction[] var5 = Direction.values();
            int var6 = var5.length;

            for (Direction direction : var5) {
                Vec3 nudgedSource = from.relative(direction, 9.999999747378752E-6);
                if (level.isBlockInLine(new ClipBlockStateContext(nudgedSource, to, BlockBehaviour.BlockStateBase::isSolid)).getType() != HitResult.Type.BLOCK) {
                    return false;
                }
            }

            return true;
        }
    }

}
