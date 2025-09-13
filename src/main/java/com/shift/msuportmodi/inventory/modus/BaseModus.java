package com.shift.msuportmodi.inventory.modus;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseModus extends Modus {
    protected int size;
    protected NonNullList<ItemStack> list;

    //Client side
    protected boolean changed;
    protected NonNullList<ItemStack> items;

    public BaseModus(ModusType<? extends BaseModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public void initModus(ItemStack modusItem, ServerPlayer player, NonNullList<ItemStack> prev, int size) {
        this.size = size;
        list = NonNullList.create();
        if(prev != null)
		{
			for(ItemStack stack : prev)
				if(!stack.isEmpty())
					list.add(stack);
		}
        if(player.level().isClientSide) {
            items = NonNullList.create();
            changed = true;
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider provider)
    {
        size = nbt.getInt("size");
        list = NonNullList.create();

        for(int i = 0; i < size; i++)
            if(nbt.contains("item"+i, Tag.TAG_COMPOUND))
                list.add(ItemStack.parse(provider, nbt.getCompound("item"+i)).orElseThrow());
            else break;
        if(side == LogicalSide.CLIENT)
        {
            items = NonNullList.create();
            changed = true;
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        nbt.putInt("size", size);
        Iterator<ItemStack> iter = list.iterator();

        for (int i = 0; i< list.size(); i++) {
            ItemStack stack = iter.next();
            nbt.put("item"+i, stack.save(provider));
        }
        return nbt;
    }

    @Override
    public boolean putItemStack(ServerPlayer player, ItemStack items) {
        if (size <= list.size() || items.isEmpty())
            return false;

        for (int i = 0; i < list.size(); i++)
            if (list.get(i).isEmpty()) {
                list.set(i, items);
                markDirty();

                return true;
            }

        list.add(items);
        markDirty();

        return true;
    }

    @Override
    public NonNullList<ItemStack> getItems()
    {
        if(side == LogicalSide.SERVER)	//Used only when replacing the modus
        {
            NonNullList<ItemStack> items = NonNullList.create();
            fillList(items);
            return items;
        }

        if(changed)
        {
            fillList(items);
        }
        return items;
    }

    protected void fillList(NonNullList<ItemStack> items)
    {
        items.clear();
        Iterator<ItemStack> iter = list.iterator();
        for(int i = 0; i < size; i++)
            if(iter.hasNext())
                items.add(iter.next());
            else items.add(ItemStack.EMPTY);
    }

    @Override
    public boolean increaseSize(ServerPlayer player)
    {
        if(MinestuckConfig.SERVER.modusMaxSize.get() > 0 && size >= MinestuckConfig.SERVER.modusMaxSize.get())
            return false;

        size++;
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
            for(ItemStack item : list)
                CaptchaDeckHandler.launchAnyItem(player, item);
            list.clear();
            markDirty();
            return ItemStack.EMPTY;
        }

        if(id < 0 || id >= list.size())
            return ItemStack.EMPTY;

        ItemStack item = list.remove(id);
        markDirty();

        if(asCard)
        {
            size--;
            markDirty();
            item = CaptchaCardItem.createCardWithItem(item, player.server);
        }
        return item;
    }

    @Override
    public boolean canSwitchFrom(Modus modus)
    {
        return false;
    }

    @Override
    public int getSize()
    {
        return size;
    }
}

