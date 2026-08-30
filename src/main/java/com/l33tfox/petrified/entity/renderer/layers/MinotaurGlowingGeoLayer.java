package com.l33tfox.petrified.entity.renderer.layers;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class MinotaurGlowingGeoLayer<T extends GeoAnimatable, O, R extends GeoRenderState> extends AutoGlowingGeoLayer<T, O, R> {
    public MinotaurGlowingGeoLayer(GeoRenderer<T, O, R> renderer) {
        super(renderer);
    }

    // Override for shader emissive texture compatibility
    // With Geckolib's default getRenderType(), shaders didn't recognize emissive texture layer
    @Override
    protected @Nullable RenderType getRenderType(R renderState) {
        Identifier texture = getTextureResource(renderState);
        return RenderTypes.eyes(texture);
    }
}
