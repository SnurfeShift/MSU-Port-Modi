package com.shift.msuportmodi.util;

import com.shift.msuportmodi.item.MSUItemComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;

public class StoredItemsUtil {
    public static CompoundTag createTagFromList(NonNullList<ItemStack> list, HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("size", list.size());

        Iterator<ItemStack> iter = list.iterator();
        for (int i = 0; i< list.size(); i++) {
            ItemStack stack = iter.next();
            tag.put("item"+i, stack.save(provider));
        }
        return tag;
    }

    public static NonNullList<ItemStack> getListFromTag(CompoundTag tag, HolderLookup.Provider provider) {
        int size = tag.getInt("size");

        NonNullList<ItemStack> list = NonNullList.create();

        for(int i = 0; i < size; i++)
            if(tag.contains("item"+i, Tag.TAG_COMPOUND))
                list.add(ItemStack.parse(provider, tag.getCompound("item"+i)).orElseThrow());
            else break;

        return list;
    }

    public static ItemStack createItem(ItemStack item, CompoundTag component) {
        item.set(MSUItemComponents.STORED_ITEMS, component);
        return item;
    }
}
