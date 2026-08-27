package com.l33tfox.petrified.block.entity.renderer;

import com.l33tfox.petrified.Petrified;
import com.l33tfox.petrified.block.TerracottaSoldierBlock;
import com.l33tfox.petrified.block.entity.TerracottaSoldierBlockEntity;
import com.l33tfox.petrified.block.entity.model.PBlockModelLayers;
import com.l33tfox.petrified.block.entity.model.TerracottaSoldierBlockEyesModel;
import com.l33tfox.petrified.block.entity.model.TerracottaSoldierBlockModel;
import com.l33tfox.petrified.block.entity.state.TerracottaSoldierBlockEntityState;
import com.l33tfox.petrified.entity.model.PEntityModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class TerracottaSoldierBlockRenderer implements BlockEntityRenderer<TerracottaSoldierBlockEntity, TerracottaSoldierBlockEntityState> {

    private static final Identifier TEXTURE = Petrified.id("textures/block/terracotta_soldier.png");
    private static final Identifier OG_EYES_TEXTURE = Petrified.id("textures/block/terracotta_soldier_eyes_original.png");
    private static final Identifier GLOWING_EYES_TEXTURE = Petrified.id("textures/entity/terracotta_soldier_eyes_blue.png");
    private final TerracottaSoldierBlockModel<TerracottaSoldierBlockEntityState> model;
    private final TerracottaSoldierBlockEyesModel<TerracottaSoldierBlockEntityState> eyesModel;
    private final ItemModelResolver itemModelResolver;

    public TerracottaSoldierBlockRenderer(BlockEntityRendererProvider.Context context) {
        model = new TerracottaSoldierBlockModel<>(
                context.bakeLayer(PBlockModelLayers.TERRACOTTA_SOLDIER_BLOCK_LAYER)
        );
        eyesModel = new TerracottaSoldierBlockEyesModel<>(context.bakeLayer(PBlockModelLayers.TERRACOTTA_SOLDIER_BLOCK_EYES_LAYER));
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
        state.leftArmXRot = blockEntity.leftArmXRot;
        state.rightArmXRot = blockEntity.rightArmXRot;
        state.headXRot = blockEntity.headXRot;
        state.headYRot = blockEntity.headYRot;

        ItemStack stack = switch (state.weapon) {
            case DIA_SWORD -> new ItemStack(Items.DIAMOND_SWORD);
            case DIA_SPEAR -> new ItemStack(Items.DIAMOND_SPEAR);
            case DIA_AXE -> new ItemStack(Items.DIAMOND_AXE);
            case CROSSBOW -> new ItemStack(Items.CROSSBOW);
        };

        state.weaponRenderState.clear();

        itemModelResolver.updateForTopItem(
                state.weaponRenderState,
                stack,
                ItemDisplayContext.NONE,
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

        poseStack.pushPose();

        model.translateToRightHand(poseStack, state.rightArmXRot, state.weapon);

        // add item model to render queue
        state.weaponRenderState.submit(
                poseStack,
                submitNodeCollector,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();

        poseStack.popPose();
    }

}
