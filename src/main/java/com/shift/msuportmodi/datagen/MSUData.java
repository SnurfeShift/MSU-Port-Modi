package com.shift.msuportmodi.datagen;

import com.shift.msuportmodi.MSUPortModi;
import com.shift.msuportmodi.datagen.recipe.MSURecipeProvider;
import com.shift.msuportmodi.datagen.tag.MSUBlockTagsProvider;
import com.shift.msuportmodi.datagen.tag.MSUItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MSUPortModi.MOD_ID)
public class MSUData {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        var builtinEntries = gen.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), registrySetBuilder(), Set.of(MSUPortModi.MOD_ID)));
        CompletableFuture<HolderLookup.Provider> lookupProvider = builtinEntries.getRegistryProvider();

        var blockTags = gen.addProvider(event.includeServer(), new MSUBlockTagsProvider(output, lookupProvider, fileHelper));

        gen.addProvider(event.includeServer(), new MSUItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), fileHelper));
        gen.addProvider(event.includeServer(), new MSURecipeProvider(output, event.getLookupProvider()));

    }

    private static RegistrySetBuilder registrySetBuilder() {
        return new RegistrySetBuilder();
    }
}
