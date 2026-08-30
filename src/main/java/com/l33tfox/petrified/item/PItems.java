package com.l33tfox.petrified.item;

import com.l33tfox.petrified.Petrified;
import com.l33tfox.petrified.block.PBlockItemIds;
import com.l33tfox.petrified.block.PBlocks;
import com.l33tfox.petrified.entity.PEntityTypes;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.waypoints.Waypoint;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PItems {
    public static final Item TERRACOTTA_SOLDIER_SPAWN_EGG = register(
            PItemIds.TERRACOTTA_SOLDIER_SPAWN_EGG,
            SpawnEggItem::new,
            new Item.Properties().spawnEgg(PEntityTypes.TERRACOTTA_SOLDIER)
    );

    public static final Item MINOTAUR_SPAWN_EGG = register(
            PItemIds.MINOTAUR_SPAWN_EGG,
            SpawnEggItem::new,
            new Item.Properties().spawnEgg(PEntityTypes.MINOTAUR)
    );

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    private static Item registerBlock(
            final BlockItemId id, final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory, final Item.Properties properties
    ) {
        return register(id.item(), p -> itemFactory.apply(block, p), properties.useBlockDescriptionPrefix().requiredFeatures(block.requiredFeatures()));
    }

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(creativeTab -> {
            creativeTab.accept(PItems.TERRACOTTA_SOLDIER_SPAWN_EGG);
            creativeTab.accept(PItems.MINOTAUR_SPAWN_EGG);
        });
    }
}
