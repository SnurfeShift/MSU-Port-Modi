package com.shift.minestuckuniverse.client;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.client.renderer.CruxiteSlimeRenderer;
import com.shift.minestuckuniverse.entity.MSUEntities;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = MinestuckUniverseModus.MOD_ID)
public class ClientHandler {
    public static final ModelLayerLocation CRUXITE_SLIME = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MinestuckUniverseModus.MOD_ID, "cruxite_slime"), "main");
    public static final ModelLayerLocation CRUXITE_SLIME_OUTER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MinestuckUniverseModus.MOD_ID, "cruxite_slime"), "outer");


    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MSUEntities.CRUXITE_SLIME.get(), CruxiteSlimeRenderer::new);
    }

    @SubscribeEvent
    public static void RegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CRUXITE_SLIME, SlimeModel::createInnerBodyLayer);
        event.registerLayerDefinition(CRUXITE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);

    }
}
