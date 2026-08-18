package com.l33tfox.petrified.block.entity.renderer;

import com.l33tfox.petrified.Petrified;
import com.l33tfox.petrified.block.TerracottaSoldierBlock;
import com.l33tfox.petrified.block.entity.TerracottaSoldierBlockEntity;
import com.l33tfox.petrified.block.entity.state.TerracottaSoldierBlockEntityState;
import com.l33tfox.petrified.entity.model.PModelLayers;
import com.l33tfox.petrified.entity.model.TerracottaSoldierEyesModel;
import com.l33tfox.petrified.entity.model.TerracottaSoldierModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class TerracottaSoldierBlockRenderer implements BlockEntityRenderer<TerracottaSoldierBlockEntity, TerracottaSoldierBlockEntityState> {

    private static final Identifier TEXTURE = Petrified.id("textures/block/terracotta_soldier.png");
    private static final Identifier OG_EYES_TEXTURE = Petrified.id("textures/block/terracotta_soldier_eyes_original.png");
    private static final Identifier GLOWING_EYES_TEXTURE = Petrified.id("textures/entity/terracotta_soldier_eyes_blue.png");
    private final TerracottaSoldierModel<TerracottaSoldierBlockEntityState> model;
    private final TerracottaSoldierEyesModel<TerracottaSoldierBlockEntityState> eyesModel;
    private final ItemModelResolver itemModelResolver;

    public TerracottaSoldierBlockRenderer(BlockEntityRendererProvider.Context context) {
        model = new TerracottaSoldierModel<>(
                context.bakeLayer(PModelLayers.TERRACOTTA_SOLDIER_LAYER)
        );
        eyesModel = new TerracottaSoldierEyesModel<>(context.bakeLayer(PModelLayers.TERRACOTTA_SOLDIER_EYES_LAYER));
        itemModelResolver = context.itemModelResolver();
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

        state.isTopBlock = blockEntity.getBlockState().getValue(TerracottaSoldierBlock.HALF) == DoubleBlockHalf.UPPER;
        state.yaw = blockEntity.getYaw();
        state.eyesActive = blockEntity.getEyesActive();
        state.weapon = blockEntity.getWeapon();

        ItemStack stack = switch (state.weapon) {
            case DIA_SWORD -> new ItemStack(Items.DIAMOND_SWORD);
            case DIA_SPEAR -> new ItemStack(Items.DIAMOND_SPEAR);
            case DIA_AXE -> new ItemStack(Items.DIAMOND_AXE);
            case CROSSBOW -> new ItemStack(Items.CROSSBOW);
            case BOW -> new ItemStack(Items.BOW);
        };

        state.weaponRenderState.clear();

        itemModelResolver.updateForTopItem(
                state.weaponRenderState,
                stack,
                ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                blockEntity.getLevel(),
                null,
                blockEntity.hashCode()
        );
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
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yaw));

        // add main model to render queue
        submitNodeCollector.submitModel(
                model,
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

        Identifier eyesTexture = OG_EYES_TEXTURE;
        RenderType eyesRenderType = RenderTypes.entityCutout(eyesTexture);
        if (state.eyesActive) {
            eyesTexture = GLOWING_EYES_TEXTURE;
            eyesRenderType = RenderTypes.eyes(eyesTexture);
        }

        // add eyes model to render queue
        submitNodeCollector.submitModel(
                eyesModel,
                state,
                poseStack,
                eyesRenderType,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF,
                null,
                0,
                null
        );

        // add item model to render queue
        state.weaponRenderState.submit(
                poseStack,
                submitNodeCollector,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();
    }

}
