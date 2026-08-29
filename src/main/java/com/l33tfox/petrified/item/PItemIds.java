package com.l33tfox.petrified.item;

import com.l33tfox.petrified.Petrified;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class PItemIds {
    public static final ResourceKey<Item> TERRACOTTA_SOLDIER_SPAWN_EGG = create("terracotta_soldier_spawn_egg");
    public static final ResourceKey<Item> MINOTAUR_SPAWN_EGG = create("minotaur_spawn_egg");

    public static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Petrified.MOD_ID, name));
    }
}
