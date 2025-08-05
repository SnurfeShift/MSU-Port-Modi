package com.shift.msuportmodi.inventory.modus;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;

import java.util.Objects;

public class AlchemyModus extends ArrayModus{
    public AlchemyModus(ModusType<? extends ArrayModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public boolean putItemStack(ServerPlayer player, ItemStack items) {
        if (size <= list.size() || items.isEmpty())
            return false;

        for (int i = 0; i < list.size(); i++)
            if (list.get(i).isEmpty() && getGristCost(items, player.level()).isEmpty()) {
                list.set(i, items);
                markDirty();
                return true;
            }

        if (getGristCost(items, player.level()).isEmpty()) {
            return false;
        }
        list.add(items);
        markDirty();
        return true;
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
            //for(ItemStack item : list)
            //    CaptchaDeckHandler.launchAnyItem(player, item);  Check if players want this 1:1
            list.clear();
            markDirty();
            return ItemStack.EMPTY;
        }

        if(id < 0 || id >= list.size())
            return ItemStack.EMPTY;

        ItemStack item = list.get(id);
        markDirty();

        if (getGristCost(item, player.level()).isEmpty()) {
            return ItemStack.EMPTY;
        }

        if(asCard)
        {
            size--;
            markDirty();
            return CaptchaCardItem.createGhostCard(list.remove(id), player.server);
        }

        return alchemize(item, player);
    }


    public ItemStack alchemize(ItemStack stack, ServerPlayer player) {
        // Cibernet originally made it save the NBT data of the item, add it later
        ItemStack item = stack.copy();
        GristSet cost = getGristCost(stack, player.level());
        PlayerData data = PlayerData.get(Objects.requireNonNull(IdentifierHandler.encode(player)), player.level());
        GristCache cache = GristCache.get(player);
        //No need for Safety Net, putItemStack will also check if there's a cost.
        if(cache.tryTake(cost, GristHelper.EnumSource.SERVER)) {
            return item;
        }
        return ItemStack.EMPTY;
    }

    public static GristSet getGristCost(ItemStack stack, Level level)
    {
        GristSet grist = GristCostRecipe.findCostForItem(stack, GristTypes.BUILD.get(), false, level);
        // Set custom logic for custom grist cost here. For example, when eventually adding Wildcard for the card item, change it here.
        return grist;
    }
}
