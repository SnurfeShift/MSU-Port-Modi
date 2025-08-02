package com.shift.msuportmodi.inventory.modus;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//This should be able to catalogue entities soon
public class WalletModus extends ArrayModus {
    //This should be Configurable
    public static final int CARD_LIMIT = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(WalletModus.class);

    public WalletModus(ModusType<? extends WalletModus> type, LogicalSide side) {
        super(type, side);
    }

    // This is so when the modus is initialized, for example when a player switches moduses, it throws the extra cards out.
    @Override
    public void initModus(ItemStack modusItem, ServerPlayer player, NonNullList<ItemStack> prev, int size) {
        super.initModus(modusItem, player, prev, size);
        if(this.size > CARD_LIMIT)
        {
            CaptchaDeckHandler.launchAnyItem(player, new ItemStack(MSItems.CAPTCHA_CARD.get(), this.size-CARD_LIMIT));
            this.size = CARD_LIMIT;
        }
    }

    // This is so players can't allocate more than the set amount of cards. If this isn't added, they can add but it won't be rendered in Sylladex Screen.
    @Override
    public boolean increaseSize(ServerPlayer player) {
        if (size >= CARD_LIMIT)
            return false;
        return super.increaseSize(player);
    }
}
