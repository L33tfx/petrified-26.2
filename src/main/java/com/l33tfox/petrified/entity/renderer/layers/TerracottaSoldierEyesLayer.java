package com.l33tfox.petrified.entity.renderer.layers;

import com.l33tfox.petrified.entity.model.TerracottaSoldierEyesModel;
import com.l33tfox.petrified.entity.model.TerracottaSoldierModel;
import com.l33tfox.petrified.entity.state.TerracottaSoldierEntityState;
import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class TerracottaSoldierEyesLayer<M extends TerracottaSoldierModel<TerracottaSoldierEntityState>> extends EyesLayer<TerracottaSoldierEntityState, M> {
    private static final RenderType SOLDIER_EYES = RenderTypes.eyes(Identifier.fromNamespaceAndPath("petrified","textures/entity/terracotta_soldier_eyes_blue.png"));

    public TerracottaSoldierEyesLayer(RenderLayerParent<TerracottaSoldierEntityState, M> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return SOLDIER_EYES;
    }
}
