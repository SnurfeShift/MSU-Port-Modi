package com.shift.minestuckuniverse.data.tag;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.item.MSUItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.util.MSTags.Items.MODUS_CARD;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MSUItemTagsProvider extends ItemTagsProvider {
    public MSUItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, blockTagProvider, MinestuckUniverseModus.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(MODUS_CARD).add(MSUItems.ARRAY_MODUS_CARD.get());
    }

    @Override
    public String getName()
    {
        return "MSU Item Tags";
    }
}
