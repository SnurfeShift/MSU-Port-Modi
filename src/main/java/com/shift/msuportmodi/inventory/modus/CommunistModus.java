package com.shift.msuportmodi.inventory.modus;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.ClientPlayerData;
import com.shift.msuportmodi.inventory.modus.modusdata.CommunistModusData;
import com.shift.msuportmodi.network.CommunistRequestPacket;
import com.shift.msuportmodi.network.CommunistUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.PacketDistributor;

public class CommunistModus extends Modus {
    CommunistModusData data;
    //Size is only so when the modus is changed to a normal one, it doesn't eat away the players previous modus size.
    protected int size;
    //Client side
    private NonNullList<ItemStack> clientList = NonNullList.create();
    protected boolean changed;

    public CommunistModus(ModusType<? extends CommunistModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public void initModus(ItemStack itemStack, ServerPlayer player, NonNullList<ItemStack> prev, int size) {
        this.size = size;
        if(player.level().isClientSide) {
            changed = true;
            this.data = CommunistModusData.get(player.server);
            data.sendUpdateToPlayer(player);
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
                data.broadcastUpdate();
                return true;
            }

        data.addItem(stack);
        markDirty();
        data.broadcastUpdate();
        return true;
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard) {
        data = CommunistModusData.get(player.server);

        if(id == CaptchaDeckHandler.EMPTY_CARD)
        {
            if(data.getList().size() < data.getSize())
            {
                data.setSize(data.getSize()-1);
                markDirty();
                data.broadcastUpdate();
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
            data.broadcastUpdate();
            markDirty();

            return ItemStack.EMPTY;
        }

        if(id < 0 || id >= data.getList().size())
            return ItemStack.EMPTY;
        ItemStack item = data.removeItem(id);
        markDirty();

        if(asCard)
        {
            data.setSize(data.getSize()-1);
            markDirty();
            item = CaptchaCardItem.createCardWithItem(item, player.server);
        }
        data.broadcastUpdate();
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
            PacketDistributor.sendToServer(new CommunistRequestPacket());
            return clientList;
        }
        return NonNullList.create();
    }

    @Override
    public boolean increaseSize(ServerPlayer player)
    {
        data = CommunistModusData.get(player.server);
        if(MinestuckConfig.SERVER.modusMaxSize.get() > 0 && data.getSize() >= MinestuckConfig.SERVER.modusMaxSize.get())
            return false;

        data.setSize(data.getSize()+1);
        markDirty();
        data.broadcastUpdate();
        return true;
    }



    @Override
    public boolean canSwitchFrom(Modus modus)
    {
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    public static void updateClientList(CommunistUpdatePacket packet) {
        Minecraft mc = Minecraft.getInstance();

        if(mc.player != null) {
            Modus modus = ClientPlayerData.getModus();

            if(modus instanceof CommunistModus communistModus) {
                assert mc.level != null;
                HolderLookup.Provider provider = mc.level.registryAccess();
                communistModus.setClientListFromTag(packet.items(), provider);
            }
        }
    }

    public void setClientListFromTag(CompoundTag tag, HolderLookup.Provider provider) {
        int size = tag.getInt("size");
        NonNullList<ItemStack> newList = NonNullList.withSize(size, ItemStack.EMPTY);

        for (int i = 0; i < size; i++) {
            if (tag.contains("item" + i, Tag.TAG_COMPOUND)) {
                ItemStack stack = ItemStack.parseOptional(provider, tag.getCompound("item" + i));
                newList.set(i, stack);
            }
        }
        markDirty();
        this.clientList = newList;
    }
}
