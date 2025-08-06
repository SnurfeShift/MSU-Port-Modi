package com.shift.msuportmodi.client.gui.modus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.CaptchaDeckPackets;
import com.mraof.minestuck.player.ClientPlayerData;
import com.shift.msuportmodi.MSUPortModi;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4fStack;

import java.util.ArrayList;
import java.util.Random;

public class GamblingSylladexScreen extends ArraySylladexScreen {

    private static final int SPIN_DURATION = 60;
    private static final double SPIN_SPEED = 0.4;

    private boolean isSpinning = false;
    private int spinTime = 0;
    private double spinAngle = 0;
    private double targetAngle = 0;
    private int totalSpinTime = 0;
    private int postSpinDelay = 0;

    private ExtendedButton spinButton;
    protected static final ResourceLocation sylladexFrame = ResourceLocation.fromNamespaceAndPath(MSUPortModi.MOD_ID, "textures/gui/antique_sylladex_frame.png");
    protected static final ResourceLocation greenFabricTexture = ResourceLocation.fromNamespaceAndPath(MSUPortModi.MOD_ID, "textures/gui/green_fabric.png");

    protected ArrayList<GamblingCard> cards = new ArrayList<>();

    public GamblingSylladexScreen(Modus modus) {
        super(modus);
        textureIndex = 53;
    }

    @Override
    public void init() {
        super.init();
        spinButton = new ExtendedButton((width - GUI_WIDTH) / 2 + 15, (height - GUI_HEIGHT) / 2 + 175, 120, 20, Component.translatable("gui.modus.spin"), button -> buttonAction());
        addRenderableWidget(spinButton);
    }

    @Override
    public void updateContent() {
        NonNullList<ItemStack> stacks = modus.getItems();
        this.cards.clear();
        this.maxWidth = Math.max(this.mapWidth, 10 + stacks.size() * 13);
        this.maxHeight = Math.max(this.mapWidth, 10 + stacks.size() * 7);

        for (int i = 0; i < stacks.size(); i++) {
            this.cards.add(new GamblingCard(stacks.get(i), this, i, 0, 0));
        }

        updatePosition();
    }

    @Override
    public void tick() {
        super.tick();
        if (isSpinning) {
            spinTime--;
            double t = 1 - (spinTime / (double) totalSpinTime);
            t = Math.min(1, Math.max(0, t));
            double easedT = 1 - Math.pow(1 - t, 2);
            spinAngle = targetAngle * easedT;

            if (spinTime <= 0) {
                isSpinning = false;
                postSpinDelay = 10;
            }
        } else if (postSpinDelay > 0) {
            postSpinDelay--;
            if (postSpinDelay == 0) {
                int selectedIndex = getSelectedIndex();
                PacketDistributor.sendToServer(new CaptchaDeckPackets.GetItem(selectedIndex, false));
                spinAngle = 0;
            }
        }
        updatePosition();
    }

    private int getSelectedIndex() {
        double angleAtTop = 3 * Math.PI / 2;
        int selectedIndex = -1;
        double minDiff = Double.MAX_VALUE;
        for (int i = 0; i < cards.size(); i++) {
            double baseAngle = (2 * Math.PI / cards.size()) * i;
            double angle = (baseAngle + spinAngle) % (2 * Math.PI);
            double diff = Math.abs(angle - angleAtTop);
            if (diff < minDiff) {
                minDiff = diff;
                selectedIndex = i;
            }
        }
        return selectedIndex;
    }

    @Override
    public void updatePosition() {
        int size = cards.size();
        int centerX = (maxWidth - 26) / 2;
        int centerY = (maxHeight - 26) / 2;
        int radius = 48 + size * 2;
        for (int i = 0; i < size; i++) {
            double baseAngle = (2 * Math.PI / size) * i;
            double angle = baseAngle + spinAngle;

            int x = centerX + (int) (Math.cos(angle) * radius);
            int y = centerY + (int) (Math.sin(angle) * radius);

            GuiCard card = cards.get(i);
            card.xPos = x;
            card.yPos = y;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (spinButton != null) {
            spinButton.setX((width - GUI_WIDTH) / 2 + 15);
            spinButton.setY((height - GUI_HEIGHT) / 2 + 175);
            spinButton.setMessage(Component.translatable(isSpinning ? "gui.modus.spinning" : "gui.modus.spin"));
            spinButton.active = !isSpinning && (postSpinDelay <=0);
        }

        int xOffset = (this.width - 256) / 2;
        int yOffset = (this.height - 202) / 2;
        this.emptySylladex.setX(xOffset + 140);
        this.emptySylladex.setY(yOffset + 175);
        if (this.mousePressed) {
            if (this.isMouseInContainer((double)mouseX, (double)mouseY)) {
                if (this.isMouseInContainer((double)this.mousePosX, (double)this.mousePosY)) {
                    this.mapX = Math.max(0, Math.min(this.maxWidth - this.mapWidth, this.mapX + this.mousePosX - mouseX));
                    this.mapY = Math.max(0, Math.min(this.maxHeight - this.mapHeight, this.mapY + this.mousePosY - mouseY));
                }

                this.mousePosX = mouseX;
                this.mousePosY = mouseY;
            }
        } else {
            this.mousePosX = -1;
            this.mousePosY = -1;
        }
        //Screen Renderer
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        //Screen Renderer end

        //RenderSystem.enableDepthTest();
        //RenderSystem.clearDepth(1.0F);
        Matrix4fStack modelPoseStack = RenderSystem.getModelViewStack();
        modelPoseStack.pushMatrix();
        modelPoseStack.translate((float)(xOffset + 16), (float)(yOffset + 17), 0.0F);
        modelPoseStack.scale(1.0F / this.scroll, 1.0F / this.scroll, 1.0F);
        RenderSystem.applyModelViewMatrix();
        drawGuiMap(guiGraphics, mouseX, mouseY);
        ArrayList<GamblingCard> visibleCards = new ArrayList<>();

        for(GamblingCard card : this.cards) {
            if (card.xPos + 21 > this.mapX && card.xPos < this.mapX + this.mapWidth && card.yPos + 26 > this.mapY && card.yPos < this.mapY + this.mapHeight) {
                visibleCards.add(card);
            }
        }
        for(GamblingCard card : visibleCards) {
            card.drawItemBackground(guiGraphics);
        }

        for(GamblingCard card : visibleCards) {
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
        if (this.isMouseInContainer((double)mouseX, (double)mouseY)) {
            int translX = (int)((float)(mouseX - xOffset - 16) * this.scroll);
            int translY = (int)((float)(mouseY - yOffset - 17) * this.scroll);

            for(GamblingCard card : visibleCards) {
                if (translX >= card.xPos + 2 - this.mapX && translX < card.xPos + 18 - this.mapX && translY >= card.yPos + 7 - this.mapY && translY < card.yPos + 23 - this.mapY) {
                    card.drawTooltip(guiGraphics, mouseX, mouseY);
                    break;
                }
            }
        }
        //Widget Renderer
        for(Renderable renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
        //Widget Renderer End
    }

    @Override
    public void drawGuiMap(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.blit(greenFabricTexture, 0, 0, 0, 0, this.mapWidth, this.mapHeight);
    }

    public void buttonAction() {
        if (!isSpinning && !cards.isEmpty()) {
            isSpinning = true;
            spinTime = SPIN_DURATION + new Random().nextInt(40);
            totalSpinTime = spinTime;

            int selectedIndex = new Random().nextInt(cards.size());
            double baseAngle = (2 * Math.PI / cards.size()) * selectedIndex;
            double totalRotation = SPIN_SPEED * totalSpinTime;
            targetAngle = 3 * Math.PI / 2 - baseAngle + totalRotation;
        }
    }

    public int getTextureIndex(GuiCard card) {
        if (!(card instanceof GamblingCard))
            return 53;
        int index = cards.indexOf(card);
        boolean isRed = index % 2 == 0;
        boolean isSelected = isCardAtTop(card);
        if (isRed) {
            return isSelected ? 54 : 53;
        } else {
            return isSelected ? 56 : 55;
        }
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

    private boolean isCardAtTop(GuiCard card) {
        if (!(card instanceof GamblingCard))
            return false;
        double angleAtTop = 3 * Math.PI / 2;
        int i = cards.indexOf(card);
        double baseAngle = (2 * Math.PI / cards.size()) * i;
        double angle = (baseAngle + spinAngle) % (2 * Math.PI);
        double angleDiff = Math.abs(angle - angleAtTop);
        return angleDiff < (Math.PI / cards.size());
    }

    public static class GamblingCard extends GuiCard {
        public GamblingCard(ItemStack item, SylladexScreen gui, int index, int xPos, int yPos) {
            super(item, gui, index, xPos, yPos);
        }

        @Override
        public void onClick(int mouseButton) {
            if (mouseButton == 0) return;

            super.onClick(mouseButton);
        }
        // Reason those are here is so render can work, since those are protected.
        @Override
        protected void drawTooltip(GuiGraphics guiGraphics, int x, int y)
        {
            super.drawTooltip(guiGraphics, x, y);
        }

        @Override
        protected void drawItemBackground(GuiGraphics guiGraphics) {
            super.drawItemBackground(guiGraphics);
        }

        @Override
        protected void drawItem(GuiGraphics guiGraphics) {
            super.drawItem(guiGraphics);
        }
    }

    private boolean isMouseInContainer(double xcor, double ycor) {
        int xOffset = (this.width - 256) / 2;
        int yOffset = (this.height - 202) / 2;
        return xcor >= (double)(xOffset + 16) && xcor < (double)(xOffset + 16 + 224) && ycor >= (double)(yOffset + 17) && ycor < (double)(yOffset + 17 + 153);
    }
}
