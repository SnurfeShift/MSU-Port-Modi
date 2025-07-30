package com.shift.minestuckuniverse;

import com.shift.minestuckuniverse.entity.MSUEntities;
import com.shift.minestuckuniverse.inventory.MSUModusTypes;
import com.shift.minestuckuniverse.item.MSUCreativeTab;
import com.shift.minestuckuniverse.item.MSUItemComponents;
import com.shift.minestuckuniverse.item.MSUItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(MinestuckUniverseModus.MOD_ID)
public class MinestuckUniverseModus {
    /**
        When adding new moduses you need:
        A screen class, in client/gui/modus
        Register it in MSUScreenFactories, in client/gui
        Create an item for it, in item
        Register it in MSUModusTypes, in inventory/modus

        Self Notes: Must never extend MSUSylladexScreen UNLESS you know what you're doing.
    */

    public static final String MOD_ID = "minestuckuniversemoduses";
    public static ResourceLocation id(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static final Logger LOGGER = LogUtils.getLogger();

    public MinestuckUniverseModus(IEventBus modEventBus, ModContainer modContainer) {
        MSUModusTypes.register();
        MSUItems.REGISTER.register(modEventBus);
        MSUEntities.register(modEventBus);
        MSUCreativeTab.REGISTER.register(modEventBus);
        MSUItemComponents.REGISTRY.register(modEventBus);
    }

    static void clientSetup(FMLClientSetupEvent event) {

    }
}