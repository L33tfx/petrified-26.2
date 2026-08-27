package com.l33tfox.petrified.block;

import com.l33tfox.petrified.block.entity.PBlockEntityTypes;
import com.l33tfox.petrified.block.entity.TerracottaSoldierBlockEntity;
import com.l33tfox.petrified.entity.PEntityTypes;
import com.l33tfox.petrified.entity.TerracottaSoldierEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Random;

public class TerracottaSoldierBlock extends BaseEntityBlock {

    public static final MapCodec<TerracottaSoldierBlock> CODEC = simpleCodec(TerracottaSoldierBlock::new);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Block.column(8.0, 0.0, 16.0);
    private static final Random RANDOM = new Random();

    @Override
    public @NonNull MapCodec<? extends TerracottaSoldierBlock> codec() {
        return CODEC;
    }

    public TerracottaSoldierBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected @NonNull BlockState updateShape(
            final BlockState state,
            final LevelReader level,
            final ScheduledTickAccess ticks,
            final BlockPos pos,
            final Direction directionToNeighbour,
            final BlockPos neighbourPos,
            final BlockState neighbourState,
            final RandomSource random
    ) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        DoubleBlockHalf half = state.getValue(HALF);
        if (directionToNeighbour.getAxis() != Direction.Axis.Y
                || half == DoubleBlockHalf.LOWER != (directionToNeighbour == Direction.UP)
                || neighbourState.is(this) && neighbourState.getValue(HALF) != half) {
            return half == DoubleBlockHalf.LOWER && directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)
                    ? Blocks.AIR.defaultBlockState()
                    : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createBlockEntityTicker(level, type, PBlockEntityTypes.TERRACOTTA_SOLDIER_BLOCK_ENTITY);
    }

    protected static <T extends BlockEntity> @Nullable BlockEntityTicker<T> createBlockEntityTicker(
            final Level level, final BlockEntityType<T> actualType, final BlockEntityType<? extends TerracottaSoldierBlockEntity> expectedType
    ) {
        return level instanceof ServerLevel serverLevel
                ? createTickerHelper(actualType, expectedType, (innerLevel, pos, state, entity) -> TerracottaSoldierBlockEntity.serverTick(entity))
                : null;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        return pos.getY() < level.getMaxY() && level.getBlockState(pos.above()).canBeReplaced(context)
                ? this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER))
                : null;
    }

    @Override
    protected @NonNull VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(final Level level, final BlockPos pos, final BlockState state, final @Nullable LivingEntity by, final ItemStack itemStack) {
        BlockPos abovePos = pos.above();
        level.setBlockAndUpdate(abovePos, copyWaterloggedFrom(level, abovePos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)));

        if (by != null && level.getBlockEntity(pos) instanceof TerracottaSoldierBlockEntity blockEntity) {
            blockEntity.setYaw(by.getYRot() + 180.0F);
        }
    }

    @Override
    protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(state, level, pos);
        }

        BlockState belowState = level.getBlockState(pos.below());
        return belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public void onPlace(final BlockState state, final Level level, final BlockPos pos, final BlockState oldState, final boolean movedByPiston) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            level.scheduleTick(pos, this, 5);
        }
    }

    public static void placeAt(final LevelAccessor level, final BlockState state, final BlockPos lowerPos, final @Block.UpdateFlags int updateType) {
        BlockPos upperPos = lowerPos.above();
        level.setBlock(lowerPos, copyWaterloggedFrom(level, lowerPos, state.setValue(HALF, DoubleBlockHalf.LOWER)), updateType);
        level.setBlock(upperPos, copyWaterloggedFrom(level, upperPos, state.setValue(HALF, DoubleBlockHalf.UPPER)), updateType);
    }

    public static BlockState copyWaterloggedFrom(final LevelReader level, final BlockPos pos, final BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, level.isWaterAt(pos)) : state;
    }

    @Override
    public BlockState playerWillDestroy(final Level level, final BlockPos pos, final BlockState state, final Player player) {
        if (!level.isClientSide()) {
            if (player.preventsBlockDrops()) {
                preventDropFromBottomPart(level, pos, state, player);
            } else {
                dropResources(state, level, pos, null, player, player.getMainHandItem());
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(
            final Level level, final Player player, final BlockPos pos, final BlockState state, final @Nullable BlockEntity blockEntity, final ItemStack destroyedWith
    ) {
        super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, destroyedWith);

        Holder<Enchantment> silkTouch = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.SILK_TOUCH);

        if (level instanceof ServerLevel serverLevel && !player.isCreative() && destroyedWith.getEnchantments().getLevel(silkTouch) == 0) {
            spawnAliveSoldier(serverLevel, pos, blockEntity);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
        if(entity instanceof Player && level instanceof ServerLevel serverLevel) {
            spawnAliveSoldier(serverLevel, pos, level.getBlockEntity(pos.below()));
        }
    }

    public void spawnAliveSoldier(ServerLevel serverLevel, BlockPos pos, @Nullable BlockEntity blockEntity) {
        for (int i = 0; i < 100; i++) {
            serverLevel.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK_CRUMBLE, Blocks.MUD_BRICKS.defaultBlockState()),
                    pos.getX() + 0.5 + (RANDOM.nextDouble() - 0.5) * 2,
                    pos.getY() + 1 + (RANDOM.nextDouble() - 0.5) * 2,
                    pos.getZ() + 0.5 + (RANDOM.nextDouble() - 0.5) * 2,
                    10, 0.5, 0.5, 0.5, 0.1
            );
        }

        TerracottaSoldierEntity aliveSoldier = new TerracottaSoldierEntity(PEntityTypes.TERRACOTTA_SOLDIER, serverLevel);
        if (blockEntity instanceof TerracottaSoldierBlockEntity soldierBlockEntity) {
            aliveSoldier.setWeapon(soldierBlockEntity.getWeapon());
            aliveSoldier.setYHeadRot(soldierBlockEntity.getYaw());
            aliveSoldier.setYRot(soldierBlockEntity.getYaw());
        }
        aliveSoldier.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        serverLevel.addFreshEntity(aliveSoldier);
        serverLevel.playSound(null, pos, SoundEvents.MUD_BRICKS_FALL, SoundSource.BLOCKS,5.0F, 0.5F);
        serverLevel.playSound(null, pos, SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.HOSTILE,0.5F, 0.5F);
        serverLevel.removeBlock(pos, false);
    }

    protected static void preventDropFromBottomPart(final Level level, final BlockPos pos, final BlockState state, final Player player) {
        DoubleBlockHalf part = state.getValue(HALF);
        if (part == DoubleBlockHalf.UPPER) {
            BlockPos bottomPos = pos.below();
            BlockState bottomState = level.getBlockState(bottomPos);
            if (bottomState.is(state.getBlock()) && bottomState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockState = bottomState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(bottomPos, blockState, 35);
                level.levelEvent(player, 2001, bottomPos, Block.getId(bottomState));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
        builder.add(WATERLOGGED);
    }

    @Override
    protected long getSeed(final BlockState state, final BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return null;
        }
        return new TerracottaSoldierBlockEntity(worldPosition, blockState);
    }

    @Override
    protected FluidState getFluidState(final BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        Player player = getNearestPlayer(level, pos);
        if (player != null && level.getBlockEntity(pos) instanceof TerracottaSoldierBlockEntity blockEntity) {
            double dx = pos.getX() + 0.5 - player.getX();
            double dz = pos.getZ() + 0.5 - player.getZ();
            blockEntity.setEyesActive(!isInPlayerView(pos, level, player, 1, false, true));
        } else if (player == null && level.getBlockEntity(pos) instanceof TerracottaSoldierBlockEntity blockEntity) {
            if (blockEntity.getEyesActive()) {
                blockEntity.setEyesActive(false);
            }
        }

        level.scheduleTick(pos, this, 5);
    }

    private Player getNearestPlayer(ServerLevel level, BlockPos pos) {
        if (level == null) {
            return null;
        }

        return level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 24.0D, false);
    }

    // Adapted from LivingEntity.isLookingAtMe()
    public boolean isInPlayerView(final BlockPos blockPos, final ServerLevel level, final Player target, final double coneSize,
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
    public boolean hasLineOfSight(final BlockPos blockPos, final ServerLevel level, final Player target, ClipContext.Block blockCollidingContext,
                                  final ClipContext.Fluid fluidCollidingContext) {
        if (target.level() != level) {
            return false;
        }

        Vec3 from = new Vec3(blockPos.getX(), 0, blockPos.getZ());
        Vec3 to = new Vec3(target.getX(), 0, target.getZ());
        return to.distanceTo(from) > 128.0
                ? false
                : level.clip(new ClipContext(from, to, blockCollidingContext, fluidCollidingContext, target)).getType() == HitResult.Type.MISS;
    }
}
