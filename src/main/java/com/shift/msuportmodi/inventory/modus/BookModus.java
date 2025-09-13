package com.shift.msuportmodi.inventory.modus;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.MSItems;
import com.shift.msuportmodi.item.MSUItems;
import com.shift.msuportmodi.util.StoredItemsUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

public class BookModus extends BaseModus {
    public int page = 0;
    public String bookName = "";

    public BookModus(ModusType<? extends BaseModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard) {
        ItemStack item = StoredItemsUtil.createItem(new ItemStack(MSUItems.CAPTCHA_BOOK.get()), StoredItemsUtil.createTagFromList(list, player.registryAccess()));
        CaptchaDeckHandler.launchAnyItem(player, item);

        return super.getItem(player, id, asCard);
    }

    public ItemStack createBook() {
        ItemStack cards = new ItemStack(MSItems.CAPTCHA_CARD.get());
        if(list.isEmpty() && size <= cards.getMaxStackSize()) {
            cards.setCount(size);
            return cards;
        }

        return null;
    }
}
