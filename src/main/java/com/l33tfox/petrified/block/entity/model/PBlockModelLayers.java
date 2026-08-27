package com.l33tfox.petrified.block.entity.model;

import com.l33tfox.petrified.Petrified;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class PBlockModelLayers {
    public static final ModelLayerLocation TERRACOTTA_SOLDIER_BLOCK_LAYER =
            new ModelLayerLocation(
                    Petrified.id("terracotta_soldier_block"),
                    "main"
            );

    public static final ModelLayerLocation TERRACOTTA_SOLDIER_BLOCK_EYES_LAYER =
            new ModelLayerLocation(
                    Petrified.id("terracotta_soldier_block_eyes"),
                    "main"
            );

    public static void init() {
        ModelLayerRegistry.registerModelLayer(
                TERRACOTTA_SOLDIER_BLOCK_LAYER,
                TerracottaSoldierBlockModel::getTexturedModelData
        );

        ModelLayerRegistry.registerModelLayer(
                TERRACOTTA_SOLDIER_BLOCK_EYES_LAYER,
                TerracottaSoldierBlockEyesModel::getTexturedModelData
        );
    }
}

