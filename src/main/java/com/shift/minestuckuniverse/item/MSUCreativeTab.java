package com.shift.minestuckuniverse.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.shift.minestuckuniverse.MinestuckUniverseModus;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Minestuck.MOD_ID, value = Dist.CLIENT)
public class MSUCreativeTab {
    public static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MinestuckUniverseModus.MOD_ID);

    public static final Supplier<CreativeModeTab> MAIN = REGISTER.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("minestuckuniversemoduses.modus_items_keys")).icon(() -> new ItemStack(MSUItems.ARRAY_MODUS_CARD.get())).displayItems(MSUCreativeTab::buildModusTab).build());

    private static void buildModusTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(MSItems.SET_MODUS_CARD.get());
        output.accept(MSItems.TREE_MODUS_CARD.get());
        output.accept(MSItems.HASHMAP_MODUS_CARD.get());
        output.accept(MSItems.QUEUE_MODUS_CARD.get());
        output.accept(MSItems.STACK_MODUS_CARD.get());
        output.accept(MSItems.QUEUESTACK_MODUS_CARD.get());
        output.accept(MSUItems.ARRAY_MODUS_CARD.get());
        output.accept(MSUItems.SLIME_MODUS_CARD.get());
        output.accept(MSUItems.WALLET_MODUS_CARD.get());
        output.accept(MSUItems.OPERANDI_MODUS_CARD.get());
        output.accept(MSUItems.COMMUNIST_MODUS_CARD.get());
        output.accept(MSUItems.CAPITALIST_MODUS_CARD.get());

    }
}
