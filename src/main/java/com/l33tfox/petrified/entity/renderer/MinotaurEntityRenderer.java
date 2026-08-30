package com.l33tfox.petrified.entity.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;
import com.l33tfox.petrified.entity.MinotaurEntity;
import com.l33tfox.petrified.entity.PEntityTypes;
import com.l33tfox.petrified.entity.renderer.layers.MinotaurGlowingGeoLayer;
import com.l33tfox.petrified.entity.renderer.layers.TerracottaSoldierEyesLayer;
import com.l33tfox.petrified.entity.state.MinotaurEntityState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EntityType;

public class MinotaurEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<MinotaurEntity, R> {

    public MinotaurEntityRenderer(EntityRendererProvider.Context context) {
        super(context, PEntityTypes.MINOTAUR);

        withRenderLayer(new MinotaurGlowingGeoLayer<>(this));
    }
}
