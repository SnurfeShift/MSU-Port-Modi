package com.shift.minestuckuniverse.inventory;

import com.shift.minestuckuniverse.inventory.modus.*;
import com.shift.minestuckuniverse.item.MSUItems;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;

import java.util.function.Supplier;

public class MSUModusTypes {
    public static Supplier<ModusType<ArrayModus>> ARRAY;
    public static Supplier<ModusType<SlimeModus>> SLIME;
    public static Supplier<ModusType<WalletModus>> WALLET;
    public static Supplier<ModusType<OperandiModus>> OPERANDI;
    public static Supplier<ModusType<CommunistModus>> COMMUNIST;

    public static void register() {
        ARRAY = ModusTypes.REGISTER.register("msuarray", () -> new ModusType<>(ArrayModus::new, MSUItems.ARRAY_MODUS_CARD));
        SLIME = ModusTypes.REGISTER.register("msuslime", () -> new ModusType<>(SlimeModus::new, MSUItems.SLIME_MODUS_CARD));
        WALLET = ModusTypes.REGISTER.register("msuwallet", () -> new ModusType<>(WalletModus::new, MSUItems.WALLET_MODUS_CARD));
        OPERANDI = ModusTypes.REGISTER.register("msuoperandi", () -> new ModusType<>(OperandiModus::new, MSUItems.OPERANDI_MODUS_CARD));
        COMMUNIST = ModusTypes.REGISTER.register("msucommunist", () -> new ModusType<>(CommunistModus::new, MSUItems.COMMUNIST_MODUS_CARD));

    }
}
