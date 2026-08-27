package com.l33tfox.petrified;

import com.l33tfox.petrified.block.entity.model.PBlockModelLayers;
import com.l33tfox.petrified.block.entity.renderer.PBlockEntityRenderers;
import com.l33tfox.petrified.entity.model.PEntityModelLayers;
import com.l33tfox.petrified.entity.renderer.PEntityRenderers;
import net.fabricmc.api.ClientModInitializer;

public class PetrifiedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PBlockModelLayers.init();
        PEntityModelLayers.init();
        PBlockEntityRenderers.init();
        PEntityRenderers.init();
    }
}
