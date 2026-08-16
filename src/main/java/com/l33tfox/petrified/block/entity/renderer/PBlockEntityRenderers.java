package com.l33tfox.petrified.block.entity.renderer;

import com.l33tfox.petrified.block.entity.PBlockEntityTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class PBlockEntityRenderers {
    public static void init() {
        BlockEntityRenderers.register(PBlockEntityTypes.TERRACOTTA_SOLDIER_BLOCK_ENTITY, TerracottaSoldierBlockRenderer::new);
    }
}
