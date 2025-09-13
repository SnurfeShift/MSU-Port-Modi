package com.shift.msuportmodi.client.gui.modus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.CaptchaDeckPackets;
import com.shift.msuportmodi.MSUPortModi;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

public class RouletteSylladexScreen extends BaseSylladexScreen {

    private ExtendedButton spinButton;
    protected static final ResourceLocation sylladexFrame = ResourceLocation.fromNamespaceAndPath(MSUPortModi.MOD_ID, "textures/gui/antique_sylladex_frame.png");
    protected static final ResourceLocation greenFabricTexture = ResourceLocation.fromNamespaceAndPath(MSUPortModi.MOD_ID, "textures/gui/green_fabric.png");

    private boolean isSpinning;
    private float spinSpeed = 0f;
    private float spinAngle = 0f;

    public RouletteSylladexScreen(Modus modus) {
        super(modus, 53);
    }

    private void spinAction() {
        if (!isSpinning && !cards.isEmpty()) {
            isSpinning = true;
            spinSpeed = 0.4f;
            spinAngle = (float)(Math.random() * Math.PI * 2);
        }
    }

    @Override
    public void tick() {
        if (isSpinning) {
            spinAngle += spinSpeed;
            spinSpeed *= 0.97f;
            if (spinSpeed < 0.001f) {
                isSpinning = false;
                int selectedIndex = getTopCardIndex();
                PacketDistributor.sendToServer(new CaptchaDeckPackets.GetItem(selectedIndex, false));
            }
            updatePosition();
        }
    }

    @Override
    public void init() {
        super.init();
        spinButton = new ExtendedButton((width - GUI_WIDTH) / 2 + 15, (height - GUI_HEIGHT) / 2 + 175, 120, 20, Component.translatable("gui.modus.spin"), button -> spinAction());
        addRenderableWidget(spinButton);
    }

    @Override
    public void updateContent() {
        NonNullList<ItemStack> stacks = this.modus.getItems();
        this.cards.clear();

        this.maxWidth = Math.max(this.mapWidth, 10 + stacks.size() * 13);
        this.maxHeight = Math.max(this.mapWidth, 10 + stacks.size() * 7);

        mapX = maxWidth / 2 - mapWidth / 2;
        mapY = maxHeight / 2 - mapHeight / 2;

        for (int i = 0; i < stacks.size(); i++) {
            this.cards.add(new RouletteCard(stacks.get(i), this, i, 0, 0));
        }
        updatePosition();
    }

    @Override
    public void updatePosition() {
        this.maxWidth = Math.max(this.mapWidth, 10 + this.cards.size() * 13);
        this.maxHeight = Math.max(this.mapWidth, 10 + this.cards.size() * (this.cards.size() >= 25 ? 13 : 7));

        double radius = cards.size() > 1 ? (cards.size() <= 4 ? 26 : 18 / Math.sin(Math.PI / (double) cards.size())) : 0;

        for (int i = 0; i < cards.size(); ++i) {
            double angle = ((i / (double) cards.size()) * (2.0 * Math.PI)) + spinAngle;

            int x = (int) (Math.cos(angle) * radius);
            int y = (int) (Math.sin(angle) * radius);

            GuiCard card = this.cards.get(i);
            card.xPos = (maxWidth - 26) / 2 + x;
            card.yPos = (maxHeight - 26) / 2 + y;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int xcor, int ycor, float f) {
        if (spinButton != null) {
            spinButton.setX((width - GUI_WIDTH) / 2 + 15);
            spinButton.setY((height - GUI_HEIGHT) / 2 + 175);
            spinButton.setMessage(Component.translatable(isSpinning ? "gui.modus.spinning" : "gui.modus.spin"));
            spinButton.active = !isSpinning;
        }
        super.render(guiGraphics, xcor, ycor, f);
    }

    @Override
    public void drawGuiMap(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.blit(greenFabricTexture, 0, 0, 0, 0, this.mapWidth, this.mapHeight);
    }

    @Override
    protected void renderMenuBackground(GuiGraphics guiGraphics) {
        super.renderMenuBackground(guiGraphics);
        int xOffset = (width - GUI_WIDTH)/2;
        int yOffset = (height - GUI_HEIGHT)/2;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        guiGraphics.blit(sylladexFrame, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
    }

    public int getTopCardIndex() {
        int topCardIndex = 0;
        int minY = cards.getFirst().yPos;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).yPos < minY) {
                minY = cards.get(i).yPos;
                topCardIndex = i;
            }
        }
        return topCardIndex;
    }

    public int getTextureIndex(GuiCard card) {
        int cardIndex = cards.indexOf(card);
        boolean isTop = cardIndex == getTopCardIndex();
        boolean isEven = cardIndex % 2 == 0;
        return isEven ? (isTop ? 54 : 53) : (isTop ? 56 : 55);
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

    public static class RouletteCard extends GuiCard {
        public RouletteCard(ItemStack item, SylladexScreen gui, int index, int xPos, int yPos) {
            super(item, gui, index, xPos, yPos);
        }

        @Override
        public void onClick(int mouseButton) {
            super.onClick(mouseButton);
        }
    }
}