package com.shift.msuportmodi.inventory.modus;

import com.shift.msuportmodi.MSUPortModi;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class JujuModusData extends SavedData {
    public static final String ID = MSUPortModi.MOD_ID+"communist_modus_data";

    private final MinecraftServer mcServer;

    private final Map<String, InventoryData> inventories = new HashMap<>();

    public JujuModusData(MinecraftServer mcServer) {
        this.mcServer = mcServer;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        for(Map.Entry<String, InventoryData> entry: inventories.entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("size", entry.getValue().size);

            NonNullList<ItemStack> list = entry.getValue().getItems();
            for(int i = 0; i<list.size(); i++) {
                tag.put("item"+i, list.get(i).save(provider));
            }

            compoundTag.put(entry.getKey(), tag);
        }
        return compoundTag;
    }

    public static JujuModusData load(CompoundTag compoundTag, HolderLookup.Provider provider, MinecraftServer mcServer) {
        JujuModusData data = new JujuModusData(mcServer);

        for (String key : compoundTag.getAllKeys()) {
            CompoundTag tag = compoundTag.getCompound(key);
            int size = tag.getInt("size");
            NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);

            for (int i = 0; i < size; i++) {
                if (tag.contains("item" + i, Tag.TAG_COMPOUND)) {
                    list.set(i, ItemStack.parseOptional(provider, tag.getCompound("item" + i)));
                }
            }

            data.inventories.put(key, new InventoryData(size, list));
        }
        return data;
    }



    public static class InventoryData {
        private int size;
        private NonNullList<ItemStack> items;

        public InventoryData() {
            this.size = 0;
            this.items = NonNullList.create();
        }

        public InventoryData(int size) {
            this.size = size;
            this.items = NonNullList.create();
        }

        public InventoryData(int size, NonNullList<ItemStack> items) {
            this.size = size;
            this.items = items;
        }

        public NonNullList<ItemStack> getItems() {
            return items;
        }

        public void setItems(NonNullList<ItemStack> items) {
            this.items = items;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
