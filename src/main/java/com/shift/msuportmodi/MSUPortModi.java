package com.shift.msuportmodi;

import com.shift.msuportmodi.entity.MSUEntities;
import com.shift.msuportmodi.inventory.MSUModusTypes;
import com.shift.msuportmodi.item.MSUCreativeTab;
import com.shift.msuportmodi.item.MSUItemComponents;
import com.shift.msuportmodi.item.MSUItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(MSUPortModi.MOD_ID)
public class MSUPortModi {
    /**
        When adding new moduses you need:
        A screen class, in client/gui/modus
        Register it in MSUScreenFactories, in client/gui
        Create an item for it, in item
        Register it in MSUModusTypes, in inventory/modus

        Self Notes: Must never extend MSUSylladexScreen UNLESS you know what you're doing.
    */

    public static final String MOD_ID = "msuportmodi";
    public static final TagKey<EntityType<?>> blacklisted = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "blacklisted"));
    public static final TagKey<BlockEntityType<?>> blacklistedblock = TagKey.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "blacklistedblock"));

    public static ResourceLocation id(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static final Logger LOGGER = LogUtils.getLogger();

    public MSUPortModi(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
        System.out.println("[MSUPortModi] FTBChunks loaded: " + ModList.get().isLoaded("ftbchunks"));
        MSUModusTypes.register();
        MSUItems.REGISTER.register(modEventBus);
        MSUEntities.register(modEventBus);
        MSUCreativeTab.REGISTER.register(modEventBus);
        MSUItemComponents.REGISTRY.register(modEventBus);
    }

    static void clientSetup(FMLClientSetupEvent event) {

    }
}