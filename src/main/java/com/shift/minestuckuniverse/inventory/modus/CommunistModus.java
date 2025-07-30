package com.shift.minestuckuniverse.inventory.modus;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class CommunistModus extends Modus {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunistModus.class);
    private MinecraftServer server;
    CommunistModusData data;
    //Client side
    private NonNullList<ItemStack> clientList = NonNullList.create();
    protected boolean changed;

    public CommunistModus(ModusType<? extends CommunistModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public void initModus(ItemStack itemStack, ServerPlayer player, NonNullList<ItemStack> prev, int size) {
        if(player.level().isClientSide) {
            changed = true;
        }
        if (side.isServer()) {
            this.server = player.getServer();
        }
    }

    @Override
    public boolean putItemStack(ServerPlayer player, ItemStack stack) {
        data = CommunistModusData.get(player.server);
        if (data.getSize() <= data.getList().size() || stack.isEmpty())
            return false;

        for (int i = 0; i < data.getList().size(); i++)
            if (data.getList().get(i).isEmpty()) {
                data.setItem(i, stack);
                markDirty();

                return true;
            }

        data.addItem(stack);
        markDirty();

        return true;
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard) {
        CommunistModusData data = CommunistModusData.get(player.server);

        if(id == CaptchaDeckHandler.EMPTY_CARD)
        {
            if(data.getList().size() < data.getSize())
            {
                data.setSize(data.getSize()-1);
                markDirty();
                return new ItemStack(MSItems.CAPTCHA_CARD.get());
            } else return ItemStack.EMPTY;
        }

        if(data.getList().isEmpty())
            return ItemStack.EMPTY;

        if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
        {
            for(ItemStack item : data.getList())
                CaptchaDeckHandler.launchAnyItem(player, item);
            data.clear();
            markDirty();

            return ItemStack.EMPTY;
        }

        if(id < 0 || id >= data.getList().size())
            return ItemStack.EMPTY;
        //from here, undone.
        ItemStack item = data.removeItem(id);
        markDirty();

        if(asCard)
        {
            data.setSize(data.getSize()-1);
            markDirty();
            item = CaptchaCardItem.createCardWithItem(item, player.server);
        }
        return item;
    }

    @Override
    public void readFromNBT(CompoundTag compoundTag, HolderLookup.Provider provider) {
        if(side == LogicalSide.SERVER) {
            this.changed = true;
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return compoundTag;
    }


    @Override
    public NonNullList<ItemStack> getItems() {
        if (side == LogicalSide.CLIENT) {
            return clientList;
        }
        return NonNullList.create(); // Not used on server
    }

    @Override
    public boolean increaseSize(ServerPlayer player)
    {
        CommunistModusData data = CommunistModusData.get(player.server);
        if(MinestuckConfig.SERVER.modusMaxSize.get() > 0 && data.getSize() >= MinestuckConfig.SERVER.modusMaxSize.get())
            return false;

        data.setSize(data.getSize()+1);
        markDirty();

        return true;
    }



    @Override
    public boolean canSwitchFrom(Modus modus)
    {
        return false;
    }

    @Override
    public int getSize()
    {
        return 0;
    }

    public static void updateClientList(NonNullList<ItemStack> items) {
        this.clientList = items;
        this.changed = true;
    }

}
