package com.shift.minestuckuniverse.data;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.data.recipe.MSURecipeProvider;
import com.shift.minestuckuniverse.data.tag.MSUBlockTagsProvider;
import com.shift.minestuckuniverse.data.tag.MSUItemTagsProvider;
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

@EventBusSubscriber(modid = MinestuckUniverseModus.MOD_ID)
public class MSUData {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        var builtinEntries = gen.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), registrySetBuilder(), Set.of(MinestuckUniverseModus.MOD_ID)));
        CompletableFuture<HolderLookup.Provider> lookupProvider = builtinEntries.getRegistryProvider();

        var blockTags = gen.addProvider(event.includeServer(), new MSUBlockTagsProvider(output, lookupProvider, fileHelper));

        gen.addProvider(event.includeServer(), new MSUItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), fileHelper));
        gen.addProvider(event.includeServer(), new MSURecipeProvider(output, event.getLookupProvider()));

    }

    private static RegistrySetBuilder registrySetBuilder() {
        return new RegistrySetBuilder();
    }
}
