package com.l33tfox.petrified.entity.renderer;

import com.l33tfox.petrified.Petrified;
import com.l33tfox.petrified.block.entity.model.TerracottaSoldierBlockEyesModel;
import com.l33tfox.petrified.block.entity.model.TerracottaSoldierBlockModel;
import com.l33tfox.petrified.block.entity.state.TerracottaSoldierBlockEntityState;
import com.l33tfox.petrified.entity.TerracottaSoldierEntity;
import com.l33tfox.petrified.entity.model.PEntityModelLayers;
import com.l33tfox.petrified.entity.model.TerracottaSoldierEyesModel;
import com.l33tfox.petrified.entity.model.TerracottaSoldierModel;
import com.l33tfox.petrified.entity.renderer.layers.TerracottaSoldierEyesLayer;
import com.l33tfox.petrified.entity.state.TerracottaSoldierEntityState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.item.CrossbowItem;

public class TerracottaSoldierEntityRenderer extends MobRenderer<TerracottaSoldierEntity, TerracottaSoldierEntityState, TerracottaSoldierModel<TerracottaSoldierEntityState>> {

    private static final Identifier TEXTURE = Petrified.id("textures/entity/terracotta_soldier.png");
    private static final Identifier EYES_TEXTURE = Petrified.id("textures/entity/terracotta_soldier_eyes_blue.png");
    private final TerracottaSoldierEyesModel<TerracottaSoldierEntityState> eyesModel;

    public TerracottaSoldierEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new TerracottaSoldierModel<>(context.bakeLayer(PEntityModelLayers.TERRACOTTA_SOLDIER_LAYER)), 0.375f);
        eyesModel = new TerracottaSoldierEyesModel<>(context.bakeLayer(PEntityModelLayers.TERRACOTTA_SOLDIER_EYES_LAYER));
        this.addLayer(new ItemInHandLayer<>(this));
        this.addLayer(new TerracottaSoldierEyesLayer<>(this));
    }

    @Override
    public Identifier getTextureLocation(TerracottaSoldierEntityState state) {
        return TEXTURE;
    }

    @Override
    public TerracottaSoldierEntityState createRenderState() {
        return new TerracottaSoldierEntityState();
    }

    @Override
    public void extractRenderState(TerracottaSoldierEntity entity, TerracottaSoldierEntityState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, itemModelResolver, partialTicks);
        state.isRiding = entity.isPassenger();
        state.mainArm = entity.getMainArm();
        state.armPose = entity.getSoldierArmPose();
        state.maxCrossbowChargeDuration = state.armPose == TerracottaSoldierEntity.SoldierArmPose.CROSSBOW_CHARGE ? CrossbowItem.getChargeDuration(entity.getUseItem(), entity) : 0;
        state.ticksUsingItem = entity.getTicksUsingItem(partialTicks);
        state.attackAnim = entity.getAttackAnim(partialTicks);
        state.isAggressive = entity.isAggressive();
    }

    @Override
    public void submit(TerracottaSoldierEntityState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

}
