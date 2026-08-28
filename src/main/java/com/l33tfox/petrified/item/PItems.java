package com.l33tfox.petrified.item;

import com.l33tfox.petrified.Petrified;
import com.l33tfox.petrified.entity.PEntityTypes;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Function;

public class PItems {
    public static final Item TERRACOTTA_SOLDIER_SPAWN_EGG = register(
            PItemIds.TERRACOTTA_SOLDIER_SPAWN_EGG,
            SpawnEggItem::new,
            new Item.Properties().spawnEgg(PEntityTypes.TERRACOTTA_SOLDIER)
    );

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(creativeTab -> {
            creativeTab.accept(PItems.TERRACOTTA_SOLDIER_SPAWN_EGG);
        });
    }
}
