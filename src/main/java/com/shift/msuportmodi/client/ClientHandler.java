package com.shift.msuportmodi.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.player.ClientPlayerData;
import com.shift.msuportmodi.MSUPortModi;
import com.shift.msuportmodi.client.renderer.CruxiteSlimeRenderer;
import com.shift.msuportmodi.entity.MSUEntities;
import com.shift.msuportmodi.inventory.modus.WalletModus;
import com.shift.msuportmodi.network.WalletCaptchaloguePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = Dist.CLIENT, modid = MSUPortModi.MOD_ID)
public class ClientHandler {
    public static final ModelLayerLocation CRUXITE_SLIME = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MSUPortModi.MOD_ID, "cruxite_slime"), "main");
    public static final ModelLayerLocation CRUXITE_SLIME_OUTER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MSUPortModi.MOD_ID, "cruxite_slime"), "outer");


    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MSUEntities.CRUXITE_SLIME.get(), CruxiteSlimeRenderer::new);
    }

    @SubscribeEvent
    public static void RegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CRUXITE_SLIME, SlimeModel::createInnerBodyLayer);
        event.registerLayerDefinition(CRUXITE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        InputConstants.Key input = InputConstants.getKey(event.getKey(), event.getScanCode());
        if(event.getAction() == GLFW.GLFW_PRESS && Minecraft.getInstance().screen == null) {
            if (MSKeyHandler.captchaKey.isActiveAndMatches(input)) {
                if (Minecraft.getInstance().player.getMainHandItem().isEmpty()) {
                    if (ClientPlayerData.getModus() instanceof WalletModus) {
                        PacketDistributor.sendToServer(new WalletCaptchaloguePacket());
                    }
                }
            }
        }
    }

    /*private static void captchalogueMob() {
        if (Minecraft.getInstance().player.getMainHandItem().isEmpty()) {
            if (ClientPlayerData.getModus() instanceof WalletModus) {
                PacketDistributor.sendToServer(new CaptchaDeckPackets.Cap());
            }
        }
    }*/
}
