package com.l33tfox.petrified.entity.model;

import com.l33tfox.petrified.entity.TerracottaSoldierEntity;
import com.l33tfox.petrified.entity.state.TerracottaSoldierEntityState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.effects.SpearAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.illager.AbstractIllager;

// Originally generated with Blockbench for Fabric 1.17+, then updated to 26.2 mappings
// Model is identical to TerracottaSoldierBlockModel, but this contains entity-specific animations
public class TerracottaSoldierModel<S extends TerracottaSoldierEntityState> extends EntityModel<S> implements ArmedModel<S> {

    private final ModelPart head;
    private final ModelPart hat;
    private final ModelPart nose;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public TerracottaSoldierModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.hat = root.getChild("hat");
        this.nose = root.getChild("nose");
        this.body = root.getChild("body");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition root = modelData.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));

        PartDefinition nose = root.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 44).mirror().addBox(-2.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).addBox(-4.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 0).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
        this.head.yRot = state.yRot * 0.017453292F;
        this.head.xRot = state.xRot * 0.017453292F;
        this.nose.xRot = state.xRot * 0.017453292F;
        this.nose.yRot = state.yRot * 0.017453292F;

        float animationSpeed = state.walkAnimationSpeed;
        float animationPos = state.walkAnimationPos;
        this.right_arm.xRot = Mth.cos((double)(animationPos * 0.6662F + 3.1415927F)) * 2.0F * animationSpeed * 0.5F;
        this.right_arm.yRot = 0.0F;
        this.right_arm.zRot = 0.0F;
        this.left_arm.xRot = Mth.cos((double)(animationPos * 0.6662F)) * 2.0F * animationSpeed * 0.5F;
        this.left_arm.yRot = 0.0F;
        this.left_arm.zRot = 0.0F;
        this.right_leg.xRot = Mth.cos((double)(animationPos * 0.6662F)) * 1.4F * animationSpeed * 0.5F;
        this.right_leg.yRot = 0.0F;
        this.right_leg.zRot = 0.0F;
        this.left_leg.xRot = Mth.cos((double)(animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed * 0.5F;
        this.left_leg.yRot = 0.0F;
        this.left_leg.zRot = 0.0F;

        TerracottaSoldierEntity.SoldierArmPose pose = state.armPose;
        if (pose == TerracottaSoldierEntity.SoldierArmPose.ATTACKING) {
            if (state.getMainHandItemState().isEmpty()) {
                AnimationUtils.animateZombieArms(this.left_arm, this.right_arm, true, state);
            } else {
                AnimationUtils.swingWeaponDown(this.right_arm, this.left_arm, state.mainArm, state.attackAnim, state.ageInTicks);
            }
        } else if (pose == TerracottaSoldierEntity.SoldierArmPose.CROSSBOW_HOLD) {
            AnimationUtils.animateCrossbowHold(this.right_arm, this.left_arm, this.head, true);
        } else if (pose == TerracottaSoldierEntity.SoldierArmPose.CROSSBOW_CHARGE) {
            AnimationUtils.animateCrossbowCharge(this.right_arm, this.left_arm, (float)state.maxCrossbowChargeDuration, state.ticksUsingItem, true);
        } else if (pose == TerracottaSoldierEntity.SoldierArmPose.SPEAR) {
            SpearAnimations.thirdPersonHandUse(right_arm, head, true, state.rightHandItemStack, state);
        }
    }

    private ModelPart getArm(final HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
    }

    @Override
    public void translateToHand(final TerracottaSoldierEntityState state, final HumanoidArm arm, final PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.getArm(arm).translateAndRotate(poseStack);
    }
}
