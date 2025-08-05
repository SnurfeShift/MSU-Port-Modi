package com.shift.msuportmodi.client.gui.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class GristSetTooltipComponent implements ClientTooltipComponent {
    private final GristSet cost;

    public GristSetTooltipComponent(GristSet cost) {
        this.cost = cost;
    }

    @Override
    public int getHeight() {
        if (cost == null || cost.isEmpty())
            return 10;

        int count = cost.asAmounts().size();
        int index = 0;
        int row = 0;

        for (GristAmount amount : cost.asAmounts()) {
            String needStr = addSuffix(amount.amount());
            String haveStr = "(" + addSuffix(0) + ")";
            int needStrWidth = Minecraft.getInstance().font.width(needStr);
            int haveStrWidth = Minecraft.getInstance().font.width(haveStr);
            int lineWidth = needStrWidth + 8 + haveStrWidth + 2;

            if (index + lineWidth > (row + 1) * GRIST_BOARD_WIDTH) {
                row++;
                index = row * GRIST_BOARD_WIDTH;
            }

            index += lineWidth + 6;
            index = Math.min(index, (row + 1) * GRIST_BOARD_WIDTH);
        }

        return (row + 1) * 10;
    }


    @Override
    public int getWidth(@NotNull Font font) {
        if (cost == null || cost.isEmpty()) {
            String text = cost == null ? I18n.get("minestuck.not_alchemizable") : I18n.get("minestuck.free");
            return font.width(text);
        }

        ClientPlayerData.ClientCache cache = ClientPlayerData.getGristCache(ClientPlayerData.CacheSource.PLAYER);
        GristSet playerGrist = cache.set();

        int maxWidth = 0;
        int index = 0;
        int row = 0;

        for (GristAmount amount : cost.asAmounts()) {
            long need = amount.amount();
            long have = playerGrist.getGrist(amount.type());

            String needStr = addSuffix(need);
            String haveStr = "(" + addSuffix(have) + ")";
            int needStrWidth = font.width(needStr);
            int haveStrWidth = font.width(haveStr);
            int lineWidth = needStrWidth + 8 + haveStrWidth + 2;

            if (index + lineWidth > (row + 1) * GRIST_BOARD_WIDTH) {
                row++;
                index = row * GRIST_BOARD_WIDTH;
            }

            int absoluteWidth = index % GRIST_BOARD_WIDTH + lineWidth;
            maxWidth = Math.max(maxWidth, absoluteWidth);

            index += lineWidth + 6;
            index = Math.min(index, (row + 1) * GRIST_BOARD_WIDTH);
        }

        return Math.min(maxWidth, GRIST_BOARD_WIDTH);
    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
        if (cost == null || cost.isEmpty()) {
            String text = cost == null ? I18n.get("minestuck.not_alchemizable") : I18n.get("minestuck.free");
            int color = cost == null ? 0xFF0000 : 0x00FF00;
            guiGraphics.drawString(font, text, pX, pY, color, false);
            return;
        }

        ClientPlayerData.ClientCache cache = ClientPlayerData.getGristCache(ClientPlayerData.CacheSource.PLAYER);
        GristSet playerGrist = cache.set();

        int index = 0;
        for (GristAmount amount : cost.asAmounts()) {
            GristType type = amount.type();
            long need = amount.amount();
            long have = playerGrist.getGrist(type);

            int row = index / GRIST_BOARD_WIDTH;
            int color = need <= have ? 0x00FF00 : 0xFF5555;

            String needStr = addSuffix(need);
            String haveStr = "(" + addSuffix(have) + ")";
            int needOffset = 1, iconSize = 8, haveOffset = 1;

            int needStrWidth = font.width(needStr);
            if (index + needStrWidth + needOffset + iconSize + haveOffset + font.width(haveStr) > (row + 1) * GRIST_BOARD_WIDTH) {
                row++;
                index = row * GRIST_BOARD_WIDTH;
            }

            int offsetX = pX + index % GRIST_BOARD_WIDTH;
            int offsetY = pY + 8 * row;

            guiGraphics.drawString(font, needStr, offsetX + needOffset, offsetY, color, false);
            guiGraphics.drawString(font, haveStr, offsetX + needOffset + needStrWidth + iconSize + haveOffset, offsetY, color, false);

            ResourceLocation icon = type.getIcon();
            if (icon != null) {
                RenderSystem.setShaderColor(1, 1, 1, 1);
                guiGraphics.blit(icon, offsetX + needStrWidth + needOffset, offsetY, 0, 0, iconSize, iconSize, iconSize, iconSize);
            }

            index += needStrWidth + 10 + font.width(haveStr);
            index = Math.min(index + 6, (row + 1) * GRIST_BOARD_WIDTH);
        }
    }

    private static final int GRIST_BOARD_WIDTH = 158;

    public static String addSuffix(long n)
    {
        if(n < 10000)
            return String.valueOf(n);
        else if(n < 10000000)
            return (n / 1000) + "K";
        else return (n / 1000000) + "M";
    }
}
