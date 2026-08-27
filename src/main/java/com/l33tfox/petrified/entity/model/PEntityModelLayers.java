package com.l33tfox.petrified.entity.model;

import com.l33tfox.petrified.Petrified;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class PEntityModelLayers {
    public static final ModelLayerLocation TERRACOTTA_SOLDIER_LAYER =
            new ModelLayerLocation(
                    Petrified.id("terracotta_soldier"),
                    "main"
            );

    public static final ModelLayerLocation TERRACOTTA_SOLDIER_EYES_LAYER =
            new ModelLayerLocation(
                    Petrified.id("terracotta_soldier_eyes"),
                    "main"
            );

    public static void init() {
        ModelLayerRegistry.registerModelLayer(
                TERRACOTTA_SOLDIER_LAYER,
                TerracottaSoldierModel::getTexturedModelData
        );

        ModelLayerRegistry.registerModelLayer(
                TERRACOTTA_SOLDIER_EYES_LAYER,
                TerracottaSoldierEyesModel::getTexturedModelData
        );
    }
}
