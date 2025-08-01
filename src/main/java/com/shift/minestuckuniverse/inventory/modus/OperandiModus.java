package com.shift.minestuckuniverse.inventory.modus;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.shift.minestuckuniverse.item.MSUItems;
import com.shift.minestuckuniverse.item.Captchalogue.OperandiUtility;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OperandiModus extends ArrayModus {
    public static final List<Item> pool = List.of(
            MSUItems.OPERANDI_HOE.get(),
            MSUItems.OPERANDI_PICKAXE.get()
    );

    private static final Logger LOGGER = LoggerFactory.getLogger(OperandiModus.class);

    public OperandiModus(ModusType<? extends OperandiModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard)
    {
        if(id == CaptchaDeckHandler.EMPTY_CARD)
        {
            if(list.size() < size)
            {
                size--;
                markDirty();
                return new ItemStack(MSItems.CAPTCHA_CARD.get());
            } else return ItemStack.EMPTY;
        }

        if(list.isEmpty())
            return ItemStack.EMPTY;

        if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
        {
            for(ItemStack item : list)
                CaptchaDeckHandler.launchAnyItem(player, item);
            list.clear();
            markDirty();
            return ItemStack.EMPTY;
        }

        if(id < 0 || id >= list.size())
            return ItemStack.EMPTY;

        ItemStack realItem = list.remove(id);
        ItemStack item = OperandiUtility.createItemWithItem(new ItemStack(pool.get(player.getRandom().nextInt(pool.size()))), realItem, player.server);
        markDirty();

        if(asCard)
        {
            size--;
            markDirty();
            item = CaptchaCardItem.createCardWithItem(realItem, player.server);
        }

        return item;
    }
}
