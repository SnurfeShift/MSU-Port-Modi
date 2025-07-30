package com.shift.minestuckuniverse.client.gui.modus;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MSUSylladexScreen extends SylladexScreen {
    public static final ResourceLocation MS_ICONS = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/icons.png");
    //public static final ResourceLocation EXTRAS = ResourceLocation.fromNamespaceAndPath(ExampleMod.MOD_ID, "textures/gui/sylladex_icons.png");
    public static final ResourceLocation CARDTEXTURES = ResourceLocation.fromNamespaceAndPath(MinestuckUniverseModus.MOD_ID, "textures/gui/captcha_cards.png");


    public MSUSylladexScreen (Modus modus) {
        super();
    }

    @Override
    public void updatePosition() {

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
}
