package com.shift.msuportmodi.inventory.modus;

import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;

import java.util.Map;
import java.util.Objects;

public class CapitalistModus extends BaseModus {
    public CapitalistModus(ModusType<? extends CapitalistModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard)
    {
        int price = 0;

        // Empty Card
        if(id == CaptchaDeckHandler.EMPTY_CARD)
        {
            if(list.size() < size)
            {
                size--;
                markDirty();
                return new ItemStack(MSItems.CAPTCHA_CARD.get());
            } else return ItemStack.EMPTY;
        }

        //Empty List
        if(list.isEmpty())
            return ItemStack.EMPTY;

        //Actual Logic
        PlayerData data = PlayerData.get(Objects.requireNonNull(IdentifierHandler.encode(player)), player.level());
        long boondollars = PlayerBoondollars.getBoondollars(data);

        if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
        {
            for(ItemStack item : list)
                price += getItemPrice(item, player.level());
            price = (int) (price * 0.8f);

            if(boondollars >= price)
            {
                PlayerBoondollars.takeBoondollars(data, price);
                player.sendSystemMessage(Component.literal("You spent $" + price + " to retrieve all items. Remaining balance $" + boondollars));
                for(ItemStack item : list)
                    CaptchaDeckHandler.launchAnyItem(player, item);
                list.clear();
                markDirty();
                return ItemStack.EMPTY;
            }
            player.sendSystemMessage(Component.literal("Not enough Boondollars to retrieve all items. You need $" + price + " to retrieve all items. Your balance: $" + boondollars));
            return ItemStack.EMPTY;
        }

        if(id < 0 || id >= list.size())
            return ItemStack.EMPTY;

        if(asCard)
        {
            ItemStack item = list.remove(id);
            size--;
            markDirty();
            return CaptchaCardItem.createCardWithItem(item, player.server);
        }

        ItemStack item = list.get(id);
        markDirty();

        price = getItemPrice(item, player.level());

        if(boondollars >= price) {
            PlayerBoondollars.takeBoondollars(data, price);
            boondollars = PlayerBoondollars.getBoondollars(data);
            list.remove(id);
            player.sendSystemMessage(
                    Component.literal("You spent $" + price + " to retrieve ")
                            .append(item.getDisplayName())
                            .append(Component.literal(". Remaining balance: $" + boondollars))
            );
            return item;
        } else {
            player.sendSystemMessage(
                    Component.literal("Not enough Boondollars to retrieve ")
                            .append(item.getDisplayName())
                            .append(Component.literal(". You need $" + price + ", your balance: $" + boondollars))
            );        }
        return ItemStack.EMPTY;
    }

    public static int getItemPrice(ItemStack item, Level level) {
        if(item.isEmpty())
            return 0;
        GristSet gristConversion = GristCostRecipe.findCostForItem(item, GristTypes.BUILD.get(), false, level);
        int gristCost = 0;
        if(gristConversion != null)
            for(Map.Entry<GristType, Long> grist : gristConversion.asMap().entrySet())
                gristCost += (int) Math.abs(grist.getValue()*grist.getKey().getValue());
        int count = item.getCount();
        int price = gristCost/count;
        int multiplier = (int) Math.ceil((double) count * 20 / 64.0);

        return price * multiplier;
    }
}
