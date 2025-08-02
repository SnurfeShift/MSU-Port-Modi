package com.shift.msuportmodi.client.renderer;

import com.shift.msuportmodi.entity.entities.EntityCruxiteSlime;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Axis;

public class CruxiteSlimeItemLayer extends RenderLayer<Slime, SlimeModel<Slime>> {

    public CruxiteSlimeItemLayer(RenderLayerParent<Slime, SlimeModel<Slime>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Slime entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = ((EntityCruxiteSlime) entity).getStoredItem();
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            float scale = .45f;
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(Axis.XP.rotationDegrees(180));
            poseStack.translate(0F, -2.15F, 0F);
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffer,
                    entity.level(),
                    0
            );
            poseStack.popPose();
        }
    }
}