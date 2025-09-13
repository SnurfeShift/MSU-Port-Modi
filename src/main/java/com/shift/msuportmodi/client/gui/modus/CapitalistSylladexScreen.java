package com.shift.msuportmodi.client.gui.modus;

import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.shift.msuportmodi.inventory.modus.CapitalistModus;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CapitalistSylladexScreen extends BaseSylladexScreen {
    protected ArrayList<GuiPricedCard> cards = new ArrayList<>();
    protected Modus modus;
    public CapitalistSylladexScreen(Modus modus) {
        super(modus, 2);
    }

    @Override
    public void updateContent() {
        super.updateContent(GuiPricedCard::new);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int xcor, int ycor, float f) {
        super.render(guiGraphics, xcor, ycor, f);
        if (this.isMouseInContainer((double)xcor, (double)ycor)) {
            int xOffset = (this.width - 256) / 2;
            int yOffset = (this.height - 202) / 2;
            int translX = (int)((float)(xcor - xOffset - 16) * this.scroll);
            int translY = (int)((float)(ycor - yOffset - 17) * this.scroll);

            for(GuiPricedCard card : this.cards) {
                if (translX >= card.xPos + 2 - this.mapX && translX < card.xPos + 18 - this.mapX && translY >= card.yPos + 7 - this.mapY && translY < card.yPos + 23 - this.mapY) {
                    card.drawTooltip(guiGraphics, xcor, ycor);
                    break;
                }
            }
        }
    }

    public static class GuiPricedCard extends GuiCard
    {
        public int price;
        Level level = Minecraft.getInstance().level;

        public GuiPricedCard(ItemStack item, SylladexScreen gui, int index, int xPos, int yPos)
        {
            super(item, gui, index, xPos, yPos);
            this.price = CapitalistModus.getItemPrice(item, level);
        }
        @Override
        protected void drawTooltip(GuiGraphics guiGraphics, int x, int y)
        {
            if (!this.item.isEmpty()) {
                List<Component> tooltip = item.getTooltipLines(Item.TooltipContext.of(level), Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
                tooltip.add(1, Component.literal("Costs " + price + " Boondollars to retrieve").withStyle(ChatFormatting.AQUA));
                guiGraphics.renderTooltip(this.gui.getMinecraft().font, tooltip, Optional.empty(), x, y);
            }
        }
    }
}
