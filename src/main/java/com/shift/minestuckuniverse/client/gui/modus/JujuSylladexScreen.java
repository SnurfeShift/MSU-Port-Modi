/*package com.shift.minestuckuniverse.client.gui.modus;

import com.shift.minestuckuniverse.inventory.modus.JujuModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

public class JujuSylladexScreen extends MSUSylladexScreen {

    protected JujuModus modus;
    protected Button guiButton;
    protected boolean link = true;


    // Component Translatables
    public static final String s_link = "gui.jujuLink";
    public static final String s_unlink = "gui.jujuUnlink";

    public JujuSylladexScreen(Modus modus) {
        super(modus);
        this.modus = (JujuModus) modus;
        textureIndex = 16;
    }

    @Override
    public void init() {
        super.init();
        guiButton = new ExtendedButton((width - GUI_WIDTH)/2 + 15, (height - GUI_HEIGHT)/2 + 175, 120, 20, Component.empty(), button -> buttonAction());
        addRenderableWidget(guiButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float f) {
        guiButton.setX((width - GUI_WIDTH)/2 + 15);
        guiButton.setY((height - GUI_HEIGHT)/2 + 175);
        //link = ((JujuModus)modus).partnerID == -1;
        guiButton.setMessage(Component.translatable(link ? s_link : s_unlink));
        guiButton.active = true;
        super.render(guiGraphics, mouseX, mouseY, f);
    }

    public void buttonAction () {
        //MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.JUJU_UPDATE, link ? JujuModusPacket.Type.LINK : JujuModusPacket.Type.UNLINK));
    }
}*/
