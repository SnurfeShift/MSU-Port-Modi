package com.shift.msuportmodi.inventory;

import com.shift.msuportmodi.inventory.modus.*;
import com.shift.msuportmodi.item.MSUItems;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;

import java.util.function.Supplier;

public class MSUModusTypes {
    public static Supplier<ModusType<BaseModus>> ARRAY;
    public static Supplier<ModusType<SlimeModus>> SLIME;
    public static Supplier<ModusType<WalletModus>> WALLET;
    public static Supplier<ModusType<OperandiModus>> OPERANDI;
    public static Supplier<ModusType<CommunistModus>> COMMUNIST;
    public static Supplier<ModusType<CapitalistModus>> CAPITALIST;
    public static Supplier<ModusType<AlchemyModus>> ALCHEMY;
    public static Supplier<ModusType<WildMagicModus>> WILDMAGIC;
    public static Supplier<ModusType<CycloneModus>> CYCLONE;
    public static Supplier<ModusType<BaseModus>> GAMBLING;
    public static Supplier<ModusType<BaseModus>> BOOK;

    public static void register() {
        ARRAY = ModusTypes.REGISTER.register("msuarray", () -> new ModusType<>(BaseModus::new, MSUItems.ARRAY_MODUS_CARD));
        SLIME = ModusTypes.REGISTER.register("msuslime", () -> new ModusType<>(SlimeModus::new, MSUItems.SLIME_MODUS_CARD));
        WALLET = ModusTypes.REGISTER.register("msuwallet", () -> new ModusType<>(WalletModus::new, MSUItems.WALLET_MODUS_CARD));
        OPERANDI = ModusTypes.REGISTER.register("msuoperandi", () -> new ModusType<>(OperandiModus::new, MSUItems.OPERANDI_MODUS_CARD));
        COMMUNIST = ModusTypes.REGISTER.register("msucommunist", () -> new ModusType<>(CommunistModus::new, MSUItems.COMMUNIST_MODUS_CARD));
        CAPITALIST = ModusTypes.REGISTER.register("msucapitalist", () -> new ModusType<>(CapitalistModus::new, MSUItems.CAPITALIST_MODUS_CARD));
        ALCHEMY = ModusTypes.REGISTER.register("msualchemy", () -> new ModusType<>(AlchemyModus::new, MSUItems.ALCHEMY_MODUS_CARD));
        WILDMAGIC = ModusTypes.REGISTER.register("msuwildmagic", () -> new ModusType<>(WildMagicModus::new, MSUItems.WILDMAGIC_MODUS_CARD));
        CYCLONE = ModusTypes.REGISTER.register("msucyclone", () -> new ModusType<>(CycloneModus::new, MSUItems.CYCLONE_MODUS_CARD));
        GAMBLING = ModusTypes.REGISTER.register("msugambling", () -> new ModusType<>(BaseModus::new, MSUItems.GAMBLING_MODUS_CARD));
        BOOK = ModusTypes.REGISTER.register("msubook", () -> new ModusType<>(BookModus::new, MSUItems.BOOK_MODUS_CARD));

    }
}
