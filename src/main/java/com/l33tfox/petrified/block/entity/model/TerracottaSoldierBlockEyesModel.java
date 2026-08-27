package com.l33tfox.petrified.block.entity.model;

import com.l33tfox.petrified.block.entity.state.TerracottaSoldierBlockEntityState;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

public class TerracottaSoldierBlockEyesModel<S extends TerracottaSoldierBlockEntityState> extends Model<S> {
    private final ModelPart head;
    private final ModelPart eyes;
    private final ModelPart hat;
    private final ModelPart nose;

    public TerracottaSoldierBlockEyesModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        this.head = root.getChild("head");
        this.eyes = this.head.getChild("eyes");
        this.hat = root.getChild("hat");
        this.nose = root.getChild("nose");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(9, 14).addBox(-3.0F, -4.0F, -4.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(13, 14).addBox(1.0F, -4.0F, -4.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(8.0F, 22.0F, -8.0F));

        PartDefinition nose = partdefinition.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offset(8.0F, -2.0F, -8.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(S state) {
        head.xRot = state.getHeadXRot();
        head.yRot = state.getHeadYRot();
    }
}
