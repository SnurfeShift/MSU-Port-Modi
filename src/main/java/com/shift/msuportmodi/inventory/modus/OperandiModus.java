package com.shift.msuportmodi.inventory.modus;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.shift.msuportmodi.item.MSUItems;
import com.shift.msuportmodi.util.OperandiUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

import java.util.List;

public class OperandiModus extends BaseModus {
    public static final List<Item> pool = List.of(
            MSUItems.OPERANDI_HOE.get(),
            MSUItems.OPERANDI_PICKAXE.get(),
            MSUItems.OPERANDI_SHOVEL.get(),
            MSUItems.OPERANDI_AXE.get()

    );

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
        ItemStack item = OperandiUtil.createItemWithItem(new ItemStack(pool.get(player.getRandom().nextInt(pool.size()))), realItem, player.server);
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
