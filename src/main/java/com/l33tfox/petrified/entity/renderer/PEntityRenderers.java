package com.l33tfox.petrified.entity.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.l33tfox.petrified.entity.PEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class PEntityRenderers {
    public static void init() {
        EntityRenderers.register(PEntityTypes.TERRACOTTA_SOLDIER, TerracottaSoldierEntityRenderer::new);
        EntityRenderers.register(PEntityTypes.MINOTAUR, context -> new GeoEntityRenderer<>(context, PEntityTypes.MINOTAUR));
    }
}
