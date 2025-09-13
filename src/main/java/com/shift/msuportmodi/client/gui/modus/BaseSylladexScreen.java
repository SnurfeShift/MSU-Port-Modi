package com.shift.msuportmodi.client.gui.modus;

import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.shift.msuportmodi.MSUPortModi;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class BaseSylladexScreen extends SylladexScreen {
    protected Modus modus;

    public static final ResourceLocation CARDTEXTURES = ResourceLocation.fromNamespaceAndPath(MSUPortModi.MOD_ID, "textures/gui/captcha_cards.png");
    public static final ResourceLocation EXTRAS = ResourceLocation.fromNamespaceAndPath(MSUPortModi.MOD_ID, "textures/gui/sylladex_icons.png");

    public BaseSylladexScreen(Modus modus, int textureIndex) {
        this.modus = modus;
        this.textureIndex = textureIndex;
    }


    @Override
    public void updateContent() {
        updateContent(GuiCard::new);
    }

    public <T extends GuiCard> void updateContent(CardFactory<T> factory) {
        NonNullList<ItemStack> stacks = modus.getItems();
        this.cards.clear();
        this.maxWidth = Math.max(mapWidth, 10 + (stacks.size() * CARD_WIDTH + (stacks.size() - 1) * 5));
        this.maxHeight = mapHeight;
        super.updateContent();

        int start = Math.max(5, (mapWidth - (stacks.size() * CARD_WIDTH + (stacks.size() - 1) * 5)) / 2);
        for (int i = 0; i < stacks.size(); i++) {
            this.cards.add(factory.create(stacks.get(i), this, i, start + i * (CARD_WIDTH + 5), (mapHeight - CARD_HEIGHT) / 2));
        }
    }

    public interface CardFactory<T extends GuiCard> {
        T create(ItemStack stack, BaseSylladexScreen screen, int index, int x, int y);
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

    public ResourceLocation getCardTexture(GuiCard card)
    {
        return CARDTEXTURES;
    }

    @Override
    public int getCardTextureX(GuiCard card)
    {
        return (textureIndex % 12)* 21;
    }

    @Override
    public int getCardTextureY(GuiCard card)
    {
        return (int) Math.floor((double)textureIndex/12)*26;
    }

    // From here, change depending on how minestuck itself updates.

    // If isMouseContainer in SylladexScreen gets changed from private->protected, remove this.
    protected boolean isMouseInContainer(double xcor, double ycor) {
        int xOffset = (this.width - 256) / 2;
        int yOffset = (this.height - 202) / 2;
        return xcor >= (double)(xOffset + 16) && xcor < (double)(xOffset + 16 + 224) && ycor >= (double)(yOffset + 17) && ycor < (double)(yOffset + 17 + 153);
    }
}
