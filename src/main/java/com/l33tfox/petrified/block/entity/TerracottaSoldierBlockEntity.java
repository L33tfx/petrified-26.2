package com.l33tfox.petrified.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.state.BlockState;

public class TerracottaSoldierBlockEntity extends BlockEntity {
    public TerracottaSoldierBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
        super(PBlockEntityTypes.TERRACOTTA_SOLDIER_BLOCK_ENTITY, worldPosition, blockState);
    }
}
