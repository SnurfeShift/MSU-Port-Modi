package com.shift.msuportmodi.item;

import com.shift.msuportmodi.MSUPortModi;
import com.shift.msuportmodi.item.Captchalogue.CaptchaBookItem;
import com.shift.msuportmodi.item.Captchalogue.HoeOperandiItem;
import com.shift.msuportmodi.item.Captchalogue.ToolOperandiItem;
import com.shift.msuportmodi.item.Captchalogue.WalletEntityItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MSUItems {
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(MSUPortModi.MOD_ID);

    // Modus
    public static final DeferredItem<Item> ARRAY_MODUS_CARD = REGISTER.register("array_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SLIME_MODUS_CARD = REGISTER.register("slime_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WALLET_MODUS_CARD = REGISTER.register("wallet_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> OPERANDI_MODUS_CARD = REGISTER.register("operandi_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> COMMUNIST_MODUS_CARD = REGISTER.register("communist_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAPITALIST_MODUS_CARD = REGISTER.register("capitalist_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> ALCHEMY_MODUS_CARD = REGISTER.register("alchemy_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WILDMAGIC_MODUS_CARD = REGISTER.register("wildmagic_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CYCLONE_MODUS_CARD = REGISTER.register("cyclone_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GAMBLING_MODUS_CARD = REGISTER.register("gambling_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BOOK_MODUS_CARD = REGISTER.register("book_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));

    //Operandi
    public static final DeferredItem<Item> OPERANDI_HOE = REGISTER.register("operandi_hoe", () -> new HoeOperandiItem(MSUTiers.OPERANDI, new Item.Properties().durability(3)));
    public static final DeferredItem<Item> OPERANDI_PICKAXE = REGISTER.register("operandi_pickaxe", () -> new ToolOperandiItem(MSUTiers.OPERANDI, BlockTags.MINEABLE_WITH_PICKAXE, new Item.Properties().durability(3)));
    public static final DeferredItem<Item> OPERANDI_AXE = REGISTER.register("operandi_axe", () -> new ToolOperandiItem(MSUTiers.OPERANDI, BlockTags.MINEABLE_WITH_AXE, new Item.Properties().durability(3)));
    public static final DeferredItem<Item> OPERANDI_SHOVEL = REGISTER.register("operandi_shovel", () -> new ToolOperandiItem(MSUTiers.OPERANDI, BlockTags.MINEABLE_WITH_SHOVEL, new Item.Properties().durability(3)));

    //Wallet Entity
    public static final DeferredItem<Item> WALLET_ENTITY_ITEM = REGISTER.register("wallet_entity_item", () -> new WalletEntityItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> CAPTCHA_BOOK = REGISTER.register("capcha_book", () -> new CaptchaBookItem(new Item.Properties().stacksTo(1)));

}
