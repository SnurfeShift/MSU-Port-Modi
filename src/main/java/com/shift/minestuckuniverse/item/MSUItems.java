package com.shift.minestuckuniverse.item;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.item.Operandi.HoeOperandiItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MSUItems {
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(MinestuckUniverseModus.MOD_ID);

    // Modus
    public static final DeferredItem<Item> ARRAY_MODUS_CARD = REGISTER.register("array_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SLIME_MODUS_CARD = REGISTER.register("slime_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WALLET_MODUS_CARD = REGISTER.register("wallet_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> OPERANDI_MODUS_CARD = REGISTER.register("operandi_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> COMMUNIST_MODUS_CARD = REGISTER.register("communist_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAPITALIST_MODUS_CARD = REGISTER.register("capitalist_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));

    //Operandi
    public static final DeferredItem<Item> OPERANDI_HOE = REGISTER.register("operandi_hoe", () -> new HoeOperandiItem(MSUTiers.OPERANDI, new Item.Properties().durability(3)));

}
