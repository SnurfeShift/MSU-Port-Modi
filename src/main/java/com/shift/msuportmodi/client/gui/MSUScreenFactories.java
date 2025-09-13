package com.shift.msuportmodi.client.gui;

import com.shift.msuportmodi.MSUPortModi;
import com.shift.msuportmodi.client.gui.modus.*;
import com.shift.msuportmodi.inventory.MSUModusTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static com.mraof.minestuck.client.gui.MSScreenFactories.registerSylladexFactory;

@EventBusSubscriber(value = Dist.CLIENT, modid = MSUPortModi.MOD_ID)
public class MSUScreenFactories {
    @SubscribeEvent
    public static void registerScreenFactories(RegisterMenuScreensEvent event) {
        registerSylladexFactory(MSUModusTypes.CAPITALIST, CapitalistSylladexScreen::new);
        registerSylladexFactory(MSUModusTypes.ALCHEMY, AlchemySylladexScreen::new);
        registerSylladexFactory(MSUModusTypes.CYCLONE, CycloneSylladexScreen::new);
        registerSylladexFactory(MSUModusTypes.GAMBLING, RouletteSylladexScreen::new);
        registerSylladexFactory(MSUModusTypes.WILDMAGIC, m -> new BaseSylladexScreen(m, 0));
        registerSylladexFactory(MSUModusTypes.WALLET, m -> new BaseSylladexScreen(m, 49));
        registerSylladexFactory(MSUModusTypes.OPERANDI, m -> new BaseSylladexScreen(m, 13));
        registerSylladexFactory(MSUModusTypes.SLIME, m -> new BaseSylladexScreen(m, 6));
        registerSylladexFactory(MSUModusTypes.ARRAY, m -> new BaseSylladexScreen(m, 42));
        registerSylladexFactory(MSUModusTypes.COMMUNIST, m -> new BaseSylladexScreen(m, 3));
        registerSylladexFactory(MSUModusTypes.BOOK, m -> new BaseSylladexScreen(m, 1));

    }
}
