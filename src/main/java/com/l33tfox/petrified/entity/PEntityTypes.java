package com.l33tfox.petrified.entity;

import com.l33tfox.petrified.Petrified;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.illager.Pillager;

public class PEntityTypes {
    public static final EntityType<TerracottaSoldierEntity> TERRACOTTA_SOLDIER = register(
            "terracotta_soldier",
            EntityType.Builder.<TerracottaSoldierEntity>of(TerracottaSoldierEntity::new, MobCategory.MONSTER)
                    .canSpawnFarFromPlayer().sized(0.6F, 1.95F)
                    .passengerAttachments(new float[]{2.0F})
                    .ridingOffset(-0.6F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Petrified.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(TERRACOTTA_SOLDIER, TerracottaSoldierEntity.createAttributes());
    }
}
