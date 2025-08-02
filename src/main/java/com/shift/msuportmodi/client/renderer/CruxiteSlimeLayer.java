package com.shift.msuportmodi.client.renderer;

import com.shift.msuportmodi.entity.entities.EntityCruxiteSlime;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.monster.Slime;

public class CruxiteSlimeLayer extends SlimeOuterLayer<Slime> {
    private final SlimeModel<Slime> slimeModel;

    public CruxiteSlimeLayer(RenderLayerParent<Slime, SlimeModel<Slime>> renderer, EntityModelSet modelSet) {
        super(renderer, modelSet);
        this.slimeModel = new SlimeModel<>(modelSet.bakeLayer(ModelLayers.SLIME_OUTER));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Slime entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentCull(this.getTextureLocation(entity)));
            int color = ((EntityCruxiteSlime) entity).getColor();
            this.getParentModel().copyPropertiesTo(this.slimeModel);
            this.slimeModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
            this.slimeModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.slimeModel.renderToBuffer(poseStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), FastColor.ARGB32.opaque(color));
        }
    }
}

