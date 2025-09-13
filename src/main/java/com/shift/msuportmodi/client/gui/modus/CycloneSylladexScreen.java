package com.shift.msuportmodi.client.gui.modus;

import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.shift.msuportmodi.inventory.modus.CycloneModus;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class CycloneSylladexScreen extends BaseSylladexScreen {
    public CycloneSylladexScreen(Modus modus) {
        super(modus, 26);
    }
    // Surprisingly this modus didn't need that many changes. One of the big issues however was how it handled ticking,
    // it was really redundant. It used two different tick to change cycle, which now only uses ClientTick and Screen Tick.'
    @Override
    public void updateContent() {
        NonNullList<ItemStack> stacks = this.modus.getItems();
        this.cards.clear();
        this.maxWidth = Math.max(this.mapWidth, 10+stacks.size() * 13);
        this.maxHeight = Math.max(this.mapWidth, 10+stacks.size() * 7);

        mapX = maxWidth/2 - mapWidth/2;
        mapY = maxHeight/2 - mapHeight/2;

        for(int i = 0; i<stacks.size(); i++) {
            this.cards.add(new CycloneCard(stacks.get(i), this, i, 0,0));
        }
        updatePosition();
    }

    @Override
    public void updatePosition()
    {
        this.maxWidth = Math.max(this.mapWidth, 10 + this.cards.size() * 13);
        this.maxHeight = Math.max(this.mapWidth, 10 + this.cards.size() * (this.cards.size() >= 25 ? 13 : 7));

        for(int i = 0; i < this.cards.size(); ++i)
        {
            double angle = ((((CycloneModus)modus).cyclePosition + i) %cards.size() /(double)cards.size())*(2.0*Math.PI);
            int centerOffset = 0;
            if(cards.size() > 1) centerOffset = cards.size() <= 4 ? 26 : (int) (18 / Math.sin(Math.PI/(double)cards.size()));

            int x = (int) (Math.cos(angle)*centerOffset);
            int y = (int) (Math.sin(angle)*centerOffset);

            GuiCard card = this.cards.get(i);
            card.xPos = (maxWidth - 26)/2 + x;
            card.yPos = (this.maxHeight - 26) / 2 + y;
        }
    }

    public boolean canUse(GuiCard card)
    {
        if(cards.size() == 1)
            return true;

        double angleOff = ((2.0*Math.PI)/(double)cards.size());
        double cardAngle = getCardAngle(card);

        return cardAngle > 3*Math.PI/2.0 - angleOff/2.0 && cardAngle < 3*Math.PI/2.0 + angleOff/2.0;
    }

    public double getCardAngle(GuiCard card)
    {
        int i = cards.indexOf(card);

        return (((((CycloneModus)modus).cyclePosition + i) %cards.size() /(double)cards.size())*(2.0*Math.PI));
    }

    public int getTextureIndex(GuiCard card)
    {
        return 26 + (canUse(card) ? 0 : 1);
    }

    @Override
    public int getCardTextureX(GuiCard card)
    {
        return (getTextureIndex(card) % 12)* 21;
    }

    @Override
    public int getCardTextureY(GuiCard card)
    {
        return (int) Math.floor((double)getTextureIndex(card)/12)*26;
    }

    @Override
    public void tick() {
        super.tick();
        updatePosition();
    }

    public static class CycloneCard extends GuiCard
    {
        public CycloneCard(ItemStack item, SylladexScreen gui, int index, int xPos, int yPos)
        {
            super(item, gui, index, xPos, yPos);
            this.gui = gui;
        }

        @Override
        public void onClick(int mouseButton)
        {
            if(((CycloneSylladexScreen)gui).canUse(this) || mouseButton == 1)
                super.onClick(mouseButton);
        }
    }
}


