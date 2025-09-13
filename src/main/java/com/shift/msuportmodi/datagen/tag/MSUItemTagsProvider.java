package com.shift.msuportmodi.datagen.tag;

import com.shift.msuportmodi.MSUPortModi;
import com.shift.msuportmodi.item.MSUItems;
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
import static com.mraof.minestuck.util.MSTags.Items.UNREADABLE;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MSUItemTagsProvider extends ItemTagsProvider {
    public MSUItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, blockTagProvider, MSUPortModi.MOD_ID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(MODUS_CARD).add(MSUItems.ARRAY_MODUS_CARD.get());
        tag(MODUS_CARD).add(MSUItems.SLIME_MODUS_CARD.get());
        tag(MODUS_CARD).add(MSUItems.OPERANDI_MODUS_CARD.get());
        tag(MODUS_CARD).add(MSUItems.COMMUNIST_MODUS_CARD.get());
        tag(MODUS_CARD).add(MSUItems.WALLET_MODUS_CARD.get());
        tag(MODUS_CARD).add(MSUItems.CAPITALIST_MODUS_CARD.get());
        tag(MODUS_CARD).add(MSUItems.WILDMAGIC_MODUS_CARD.get());
        tag(MODUS_CARD).add(MSUItems.CYCLONE_MODUS_CARD.get());
        tag(MODUS_CARD).add(MSUItems.GAMBLING_MODUS_CARD.get());
        tag(MODUS_CARD).add(MSUItems.ALCHEMY_MODUS_CARD.get());

        tag(UNREADABLE).add(MSUItems.WALLET_ENTITY_ITEM.get());
    }
}
