package com.l33tfox.petrified;

import com.l33tfox.petrified.block.entity.renderer.PBlockEntityRenderers;
import com.l33tfox.petrified.entity.model.PModelLayers;
import net.fabricmc.api.ClientModInitializer;

public class PetrifiedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PModelLayers.init();
        PBlockEntityRenderers.init();
    }
}
