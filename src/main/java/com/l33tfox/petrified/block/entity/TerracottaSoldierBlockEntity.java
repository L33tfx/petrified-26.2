package com.l33tfox.petrified.block.entity;

import com.l33tfox.petrified.block.PBlocks;
import com.l33tfox.petrified.block.TerracottaSoldierBlock;
import com.l33tfox.petrified.entity.TerracottaSoldierEntity;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.apache.logging.log4j.core.jmx.Server;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.Random;

// Only created for the bottom half block (logic for this in TerracottaSoldierBlock's newBlockEntity())
public class TerracottaSoldierBlockEntity extends BlockEntity implements GameEventListener.Provider<TerracottaSoldierBlockEntity.BlockChangeListener> {

    private float yaw = 0.0f;
    private boolean eyesActive = false;
    private TerracottaSoldierWeapon weapon;
    private final Random RANDOM = new Random();
    private final BlockChangeListener listener;
    public float leftArmXRot;
    public float rightArmXRot;
    public float headXRot;
    public float headYRot;
    public boolean awakening = false;

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
        setYaw(RANDOM.nextFloat() * 360.0F);
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
        if (block instanceof TerracottaSoldierBlock soldierBlock && getLevel() instanceof ServerLevel serverLevel) {
            soldierBlock.spawnAliveSoldier(serverLevel, getBlockPos(), this);
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

    public static void serverTick(TerracottaSoldierBlockEntity blockEntity) {
        if (blockEntity.awakening) {
            blockEntity.awaken();
        }

        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();

        Player player = blockEntity.getNearestPlayer((ServerLevel) level, pos);
        if (player != null) {
            double dx = pos.getX() + 0.5 - player.getX();
            double dz = pos.getZ() + 0.5 - player.getZ();
            blockEntity.setEyesActive(!isInPlayerView(pos, (ServerLevel) level, player, 1, false, true));
        } else if (player == null) {
            if (blockEntity.getEyesActive()) {
                blockEntity.setEyesActive(false);
            }
        }
    }

    private Player getNearestPlayer(ServerLevel level, BlockPos pos) {
        if (level == null) {
            return null;
        }

        return level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 24.0D, false);
    }

    // Adapted from LivingEntity.isLookingAtMe()
    public static boolean isInPlayerView(final BlockPos blockPos, final ServerLevel level, final Player target, final double coneSize,
                                         final boolean adjustForDistance, final boolean seeThroughTransparentBlocks) {
        Vec3 look = target.getViewVector(1.0F).multiply(1, 0, 1).normalize();

        Vec3 dir = new Vec3(blockPos.getX() - target.getX(), 0, blockPos.getZ() - target.getZ());
        double dist = dir.length();
        dir = dir.normalize();
        double dot = look.dot(dir);
        if (dot > 1.0 - coneSize / (adjustForDistance ? dist : 1.0)
                && hasLineOfSight(blockPos, level, target, seeThroughTransparentBlocks ? ClipContext.Block.VISUAL : ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE)) {
            return true;
        }

        return false;
    }

    // Adapted from LivingEntity.hasLineOfSight()
    public static boolean hasLineOfSight(final BlockPos blockPos, final ServerLevel level, final Player target, ClipContext.Block blockCollidingContext,
                                         final ClipContext.Fluid fluidCollidingContext) {
        if (target.level() != level) {
            return false;
        }

        Vec3 from = target.getEyePosition();
        Vec3 to = Vec3.atCenterOf(blockPos);

        return to.distanceTo(from) > 128.0
                ? false
                : level.isBlockInLine(new ClipBlockStateContext(from, to, state -> state.is(PBlocks.TERRACOTTA_SOLDIER))).getType() == HitResult.Type.BLOCK;
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

                if (destination.equals(sourcePosition) || !(context.sourceEntity() instanceof Player)) {
                    return false;
                }

                if (context.affectedState() != null && context.affectedState().is(PBlocks.TERRACOTTA_SOLDIER)) {
                    return false;
                }

                if (isBehindWall(level, sourcePosition, destination)) {
                    return false;
                }

                awakening = true;
            }

            return false;
        }

        private static boolean isBehindWall(final Level level, final Vec3 origin, final Vec3 dest) {
            Vec3 direction = dest.subtract(origin).normalize();
            Vec3 start = origin.add(direction.scale(1));

            HitResult result = level.isBlockInLine(
                    new ClipBlockStateContext(start, dest, state -> !state.is(PBlocks.TERRACOTTA_SOLDIER) && state.isSolid()));

            return result.getType() == HitResult.Type.BLOCK;
        }
    }

}
