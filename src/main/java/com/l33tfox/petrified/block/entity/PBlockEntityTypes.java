package com.l33tfox.petrified.block.entity;

import com.l33tfox.petrified.Petrified;
import com.l33tfox.petrified.block.PBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PBlockEntityTypes {
    public static final BlockEntityType<TerracottaSoldierBlockEntity> TERRACOTTA_SOLDIER_BLOCK_ENTITY =
            register("terracotta_soldier_block_entity", TerracottaSoldierBlockEntity::new, PBlocks.TERRACOTTA_SOLDIER);

    public static void init() {

    }

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Petrified.id(name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
