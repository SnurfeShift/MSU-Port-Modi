package com.shift.minestuckuniverse.client.renderer;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CruxiteSlimeRenderer extends SlimeRenderer {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(MinestuckUniverseModus.MOD_ID, "textures/entity/cruxite_slime.png");

    public CruxiteSlimeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.layers.removeLast();
        this.addLayer(new CruxiteSlimeLayer(this, context.getModelSet()));
        this.addLayer(new CruxiteSlimeItemLayer(this));

    }

    /*public void render(Slime entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        ItemStack stack = ((EntityCruxiteSlime) entity).getStoredItem();
        poseStack.pushPose();
        float scale = 0.5f;
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0F, 0.95F, 0F);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), 0);
        poseStack.popPose();
    }*/

    @Override
    public ResourceLocation getTextureLocation(Slime entityCruxiteSlime) {
        return LOCATION;
    }
}
