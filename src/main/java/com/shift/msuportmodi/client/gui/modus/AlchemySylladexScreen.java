package com.shift.msuportmodi.client.gui.modus;

import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.shift.msuportmodi.client.gui.tooltip.GristSetTooltip;
import com.shift.msuportmodi.inventory.modus.AlchemyModus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlchemySylladexScreen extends BaseSylladexScreen {
    protected ArrayList<AlchemySylladexScreen.AlchemyCard> cards = new ArrayList<>();

    public AlchemySylladexScreen(Modus modus) {
        super(modus, 19);
    }

    @Override
    public void updateContent() {
        super.updateContent(AlchemyCard::new);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int xcor, int ycor, float f) {
        super.render(guiGraphics, xcor, ycor, f);
        if (this.isMouseInContainer((double)xcor, (double)ycor)) {
            int xOffset = (this.width - 256) / 2;
            int yOffset = (this.height - 202) / 2;
            int translX = (int)((float)(xcor - xOffset - 16) * this.scroll);
            int translY = (int)((float)(ycor - yOffset - 17) * this.scroll);
            // This isn't the desired way to do this. This will render two drawTooltips instead of one.
            for(AlchemyCard card : this.cards) {
                if (translX >= card.xPos + 2 - this.mapX && translX < card.xPos + 18 - this.mapX && translY >= card.yPos + 7 - this.mapY && translY < card.yPos + 23 - this.mapY) {
                    card.drawTooltip(guiGraphics, xcor, ycor);
                    break;
                }
            }
        }
    }
    public static class AlchemyCard extends GuiCard
    {
        Level level = Minecraft.getInstance().level;

        public AlchemyCard(ItemStack item, SylladexScreen gui, int index, int xPos, int yPos)
        {
            super(item, gui, index, xPos, yPos);
        }

        @Override
        protected void drawTooltip(GuiGraphics guiGraphics, int x, int y)
        {
            if (!this.item.isEmpty())
            {
                List<Component> tooltip = item.getTooltipLines(Item.TooltipContext.of(level), Minecraft.getInstance().player,
                        Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);

                GristSet cost = AlchemyModus.getGristCost(item, level);
                Optional<TooltipComponent> tooltipComponent = Optional.of(new GristSetTooltip(cost));
                guiGraphics.renderTooltip(gui.getMinecraft().font, tooltip, tooltipComponent, x, y);
            }
        }
    }
}
