package com.l33tfox.petrified.block.entity.renderer;

import com.l33tfox.petrified.Petrified;
import com.l33tfox.petrified.block.TerracottaSoldierBlock;
import com.l33tfox.petrified.block.entity.TerracottaSoldierBlockEntity;
import com.l33tfox.petrified.block.entity.state.TerracottaSoldierBlockEntityState;
import com.l33tfox.petrified.entity.model.PModelLayers;
import com.l33tfox.petrified.entity.model.TerracottaSoldierModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class TerracottaSoldierBlockRenderer implements BlockEntityRenderer<TerracottaSoldierBlockEntity, TerracottaSoldierBlockEntityState> {

    private static final Identifier TEXTURE = Petrified.id("textures/block/terracotta_soldier.png");
    private final TerracottaSoldierModel<TerracottaSoldierBlockEntityState> model;

    public TerracottaSoldierBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new TerracottaSoldierModel<>(
                context.bakeLayer(PModelLayers.TERRACOTTA_SOLDIER_LAYER)
        );
    }

    @Override
    public TerracottaSoldierBlockEntityState createRenderState() {
        return new TerracottaSoldierBlockEntityState();
    }

    @Override
    public void extractRenderState(
            TerracottaSoldierBlockEntity blockEntity,
            TerracottaSoldierBlockEntityState state,
            float tickProgress,
            Vec3 cameraPos,
            @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

        state.isTopBlock = blockEntity.getBlockState()
                .getValue(TerracottaSoldierBlock.HALF) == DoubleBlockHalf.UPPER;
    }

    @Override
    public void submit(TerracottaSoldierBlockEntityState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        // Don't render 2 statue models
        if (state.isTopBlock) {
            return;
        }

        poseStack.pushPose();

        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        submitNodeCollector.submitModel(
                this.model,
                state,
                poseStack,
                RenderTypes.entityCutout(TEXTURE),
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF,
                null,
                0,
                null
        );

        poseStack.popPose();
    }

}
