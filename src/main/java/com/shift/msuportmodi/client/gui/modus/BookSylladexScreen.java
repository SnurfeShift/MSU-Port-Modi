package com.shift.msuportmodi.client.gui.modus;

import com.mraof.minestuck.inventory.captchalogue.Modus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

public class BookSylladexScreen extends BaseSylladexScreen{
    private ExtendedButton publishButton;

    public BookSylladexScreen(Modus modus, int textureIndex) {
        super(modus, textureIndex);
    }

    @Override
    public void init() {
        super.init();
        publishButton = new ExtendedButton((width - GUI_WIDTH) / 2 + 15, (height - GUI_HEIGHT) / 2 + 175, 120, 20, Component.translatable("gui.modus.publish"), button -> publishAction());
        addRenderableWidget(publishButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int xcor, int ycor, float f) {
        if (publishButton != null) {
            publishButton.setX((width - GUI_WIDTH) / 2 + 15);
            publishButton.setY((height - GUI_HEIGHT) / 2 + 175);
        }
        super.render(guiGraphics, xcor, ycor, f);
    }

    public void publishAction() {

    }
}


