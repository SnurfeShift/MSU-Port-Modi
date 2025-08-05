package com.shift.msuportmodi.client.gui.modus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.CaptchaDeckPackets;
import com.mraof.minestuck.player.ClientPlayerData;
import com.shift.msuportmodi.client.gui.tooltip.GristSetTooltip;
import com.shift.msuportmodi.client.gui.tooltip.GristSetTooltipComponent;
import com.shift.msuportmodi.inventory.modus.AlchemyModus;
import com.shift.msuportmodi.inventory.modus.ArrayModus;
import com.shift.msuportmodi.inventory.modus.CapitalistModus;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4fStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlchemySylladexScreen extends MSUSylladexScreen {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlchemySylladexScreen.class);
    protected ArrayList<AlchemySylladexScreen.AlchemyCard> cards = new ArrayList();
    protected Modus modus;
    public AlchemySylladexScreen(Modus modus) {
        super(modus);
        this.modus = modus;
        textureIndex = 19;
    }

    @Override
    public void updateContent() {
        NonNullList<ItemStack> stacks = modus.getItems();
        this.emptySylladex.setMessage(Component.literal("Buy All"));
        this.cards.clear();
        this.maxWidth = Math.max(mapWidth, 10 + (stacks.size()*CARD_WIDTH + (stacks.size() - 1)*5));
        this.maxHeight = mapHeight;
        super.updateContent();

        int start = Math.max(5, (mapWidth - (stacks.size()*CARD_WIDTH + (stacks.size() - 1)*5))/2);
        for(int i = 0; i < stacks.size(); i++)
            this.cards.add(new AlchemySylladexScreen.AlchemyCard(stacks.get(i), this, i, start + i*(CARD_WIDTH + 5), (mapHeight - CARD_HEIGHT)/2));

    }

    @Override
    public void updatePosition()
    {
        this.maxWidth = Math.max(mapWidth, 10 + (cards.size()*CARD_WIDTH + (cards.size() - 1)*5));
        this.maxHeight = mapHeight;
        int start = Math.max(5, (mapWidth - (cards.size()*CARD_WIDTH + (cards.size() - 1)*5))/2);
        for(int i = 0; i < cards.size(); i++)
        {
            GuiCard card = cards.get(i);
            card.xPos = start + i*(CARD_WIDTH + 5);
            card.yPos = (mapHeight - CARD_HEIGHT)/2;
        }
    }

    /*This is SylladexScreen's render code.
    Since calling super() would render GuiCard tooltips instead of AlchemyCards, copying it was necessary.
    This simply swaps a bit of logic, like instead of calling Screen super(), it directly copies Screen render():

    this.renderBackground(guiGraphics, xcor, ycor, f);
    for(Renderable renderable : this.renderables) {
        renderable.render(guiGraphics, xcor, ycor, f);
    }

    And swaps any mention of GuiCard in render() to AlchemyCard.
    * */
    @Override
    public void render(GuiGraphics guiGraphics, int xcor, int ycor, float f) {
        int xOffset = (this.width - 256) / 2;
        int yOffset = (this.height - 202) / 2;
        this.emptySylladex.setX(xOffset + 140);
        this.emptySylladex.setY(yOffset + 175);
        if (this.mousePressed) {
            if (this.isMouseInContainer((double)xcor, (double)ycor)) {
                if (this.isMouseInContainer((double)this.mousePosX, (double)this.mousePosY)) {
                    this.mapX = Math.max(0, Math.min(this.maxWidth - this.mapWidth, this.mapX + this.mousePosX - xcor));
                    this.mapY = Math.max(0, Math.min(this.maxHeight - this.mapHeight, this.mapY + this.mousePosY - ycor));
                }

                this.mousePosX = xcor;
                this.mousePosY = ycor;
            }
        } else {
            this.mousePosX = -1;
            this.mousePosY = -1;
        }

        this.renderBackground(guiGraphics, xcor, ycor, f);

        for(Renderable renderable : this.renderables) {
            renderable.render(guiGraphics, xcor, ycor, f);
        }

        Matrix4fStack modelPoseStack = RenderSystem.getModelViewStack();
        modelPoseStack.pushMatrix();
        modelPoseStack.translate((float)(xOffset + 16), (float)(yOffset + 17), 0.0F);
        modelPoseStack.scale(1.0F / this.scroll, 1.0F / this.scroll, 1.0F);
        RenderSystem.applyModelViewMatrix();
        this.drawGuiMap(guiGraphics, xcor, ycor);
        ArrayList<AlchemySylladexScreen.AlchemyCard> visibleCards = new ArrayList();

        for(AlchemySylladexScreen.AlchemyCard card : this.cards) {
            if (card.xPos + 21 > this.mapX && card.xPos < this.mapX + this.mapWidth && card.yPos + 26 > this.mapY && card.yPos < this.mapY + this.mapHeight) {
                visibleCards.add(card);
            }
        }

        for(AlchemySylladexScreen.AlchemyCard card : visibleCards) {
            card.drawItemBackground(guiGraphics);
        }

        for(AlchemySylladexScreen.AlchemyCard card : visibleCards) {
            card.drawItem(guiGraphics);
        }

        modelPoseStack.popMatrix();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(sylladexFrame, xOffset, yOffset, 0, 0, 256, 202);
        guiGraphics.drawString(this.font, this.getTitle().getString(), xOffset + 15, yOffset + 5, 4210752, false);
        String str = ClientPlayerData.getModus().getName().getString();
        guiGraphics.drawString(this.font, str, xOffset + 256 - this.font.width(str) - 16, yOffset + 5, 4210752, false);
        if (this.isMouseInContainer((double)xcor, (double)ycor)) {
            int translX = (int)((float)(xcor - xOffset - 16) * this.scroll);
            int translY = (int)((float)(ycor - yOffset - 17) * this.scroll);

            for(AlchemySylladexScreen.AlchemyCard card : visibleCards) {
                if (translX >= card.xPos + 2 - this.mapX && translX < card.xPos + 18 - this.mapX && translY >= card.yPos + 7 - this.mapY && translY < card.yPos + 23 - this.mapY) {
                    card.drawTooltip(guiGraphics, xcor, ycor);
                    break;
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.isMouseInContainer(mouseX, mouseY)) {
            int xOffset = (this.width - 256) / 2;
            int yOffset = (this.height - 202) / 2;
            int translX = (int)((mouseX - (double)xOffset - (double)16.0F) * (double)this.scroll);
            int translY = (int)((mouseY - (double)yOffset - (double)17.0F) * (double)this.scroll);

            for(AlchemySylladexScreen.AlchemyCard card : this.cards) {
                if (translX >= card.xPos + 2 - this.mapX && translX < card.xPos + 18 - this.mapX && translY >= card.yPos + 7 - this.mapY && translY < card.yPos + 23 - this.mapY) {
                    card.onClick(mouseButton);
                    return true;
                }
            }

            this.mousePressed = true;
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    public static class AlchemyCard extends GuiCard
    {
        public AlchemySylladexScreen gui;
        Level level = Minecraft.getInstance().level;

        public AlchemyCard(ItemStack item, AlchemySylladexScreen gui, int index, int xPos, int yPos)
        {
            super(item, gui, index, xPos, yPos);
            this.gui = gui;
        }

        @Override
        protected void drawTooltip(GuiGraphics guiGraphics, int x, int y)
        {
            if (!this.item.isEmpty())
            {
                List<Component> tooltip = item.getTooltipLines(Item.TooltipContext.of(level), Minecraft.getInstance().player,
                        Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);

                // Add gristboard-specific tooltips directly here
                GristSet cost = AlchemyModus.getGristCost(item, level);
                Optional<TooltipComponent> tooltipComponent = Optional.of(new GristSetTooltip(cost));
                guiGraphics.renderTooltip(this.gui.font, tooltip, tooltipComponent, x, y);
            }
        }

        @Override
        protected void drawItemBackground(GuiGraphics guiGraphics) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int minX = 0;
            int maxX = 21;
            int minY = 0;
            int maxY = 26;
            if (this.xPos + minX < this.gui.mapX) {
                minX += this.gui.mapX - (this.xPos + minX);
            } else if (this.xPos + maxX > this.gui.mapX + this.gui.mapWidth) {
                maxX -= this.xPos + maxX - (this.gui.mapX + this.gui.mapWidth);
            }

            if (this.yPos + minY < this.gui.mapY) {
                minY += this.gui.mapY - (this.yPos + minY);
            } else if (this.yPos + maxY > this.gui.mapY + this.gui.mapHeight) {
                maxY -= this.yPos + maxY - (this.gui.mapY + this.gui.mapHeight);
            }

            guiGraphics.blit(this.gui.getCardTexture(this), this.xPos + minX - this.gui.mapX, this.yPos + minY - this.gui.mapY, this.gui.getCardTextureX(this) + minX, this.gui.getCardTextureY(this) + minY, maxX - minX, maxY - minY);
        }

        @Override
        protected void drawItem(GuiGraphics guiGraphics) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (!this.item.isEmpty()) {
                int x = this.xPos + 2 - this.gui.mapX;
                int y = this.yPos + 7 - this.gui.mapY;
                if (x >= this.gui.mapWidth || y >= this.gui.mapHeight || x + 16 < 0 || y + 16 < 0) {
                    return;
                }

                guiGraphics.renderItem(this.item, x, y);
                guiGraphics.renderItemDecorations(this.gui.font, this.item, x, y);
            }
        }

        @Override
        public void onClick(int mouseButton) {
            int toSend = -1;
            if (this.item.isEmpty() && mouseButton == 1) {
                toSend = -2;
            } else if (this.index != -1 && (mouseButton == 0 || mouseButton == 1)) {
                toSend = this.index;
            }

            if (toSend != -1) {
                PacketDistributor.sendToServer(new CaptchaDeckPackets.GetItem(toSend, mouseButton != 0), new CustomPacketPayload[0]);
            }

        }
    }

    private boolean isMouseInContainer(double xcor, double ycor) {
        int xOffset = (this.width - 256) / 2;
        int yOffset = (this.height - 202) / 2;
        return xcor >= (double)(xOffset + 16) && xcor < (double)(xOffset + 16 + 224) && ycor >= (double)(yOffset + 17) && ycor < (double)(yOffset + 17 + 153);
    }
}
