package com.shift.minestuckuniverse.client.gui;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.client.gui.modus.*;
import com.shift.minestuckuniverse.inventory.MSUModusTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;


import static com.mraof.minestuck.client.gui.MSScreenFactories.registerSylladexFactory;

@EventBusSubscriber(value = Dist.CLIENT, modid = MinestuckUniverseModus.MOD_ID)
public class MSUScreenFactories {
    @SubscribeEvent
    public static void registerScreenFactories(RegisterMenuScreensEvent event) {
        registerSylladexFactory(MSUModusTypes.ARRAY, ArraySylladexScreen::new);
        registerSylladexFactory(MSUModusTypes.SLIME, SlimeSylladexScreen::new);
        registerSylladexFactory(MSUModusTypes.WALLET, WalletSylladexScreen::new);
        registerSylladexFactory(MSUModusTypes.OPERANDI, OperandiSylladexScreen::new);
        registerSylladexFactory(MSUModusTypes.COMMUNIST, CommunistSylladexScreen::new);
        registerSylladexFactory(MSUModusTypes.CAPITALIST, CapitalistSylladexScreen::new);

    }

}
