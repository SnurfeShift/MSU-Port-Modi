package com.shift.minestuckuniverse.inventory.modus;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.network.CommunistUpdatePacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.Iterator;


public class CommunistModusData extends SavedData {
    public static final String ID = MinestuckUniverseModus.MOD_ID+"communist_modus_data";

    private int size;
    private NonNullList<ItemStack> list;

    public final MinecraftServer mcServer;

    public CommunistModusData(MinecraftServer server) {
        this.size = 5;
        this.list = NonNullList.create();
        this.mcServer = server;
    }

    public static CommunistModusData get(Level level)
    {
        MinecraftServer server = level.getServer();
        if(server == null)
            throw new IllegalArgumentException("");
        return get(server);
    }

    public static CommunistModusData get(MinecraftServer mcServer)
    {
        ServerLevel level = mcServer.overworld();
        DimensionDataStorage storage = level.getDataStorage();

        return storage.computeIfAbsent(factory(mcServer), ID);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putInt("size", size);
        Iterator<ItemStack> iter = list.iterator();

        for (int i = 0; i< list.size(); i++) {
            ItemStack stack = iter.next();
            compoundTag.put("item"+i, stack.save(provider));
        }
        return compoundTag;
    }

    public static CommunistModusData load(CompoundTag compoundTag, HolderLookup.Provider provider, MinecraftServer mcServer) {
        CommunistModusData data = new CommunistModusData(mcServer);
        data.size = compoundTag.getInt("size");
        data.list = NonNullList.create();
        for (int i = 0; i < data.size; i++) {
            if (compoundTag.contains("item" + i, Tag.TAG_COMPOUND)) {
                data.list.add(ItemStack.parseOptional(provider, compoundTag.getCompound("item"+i)));
            }
        }
        return data;
    }

    private static Factory<CommunistModusData> factory(MinecraftServer mcServer)
    {
        return new Factory<>(() -> new CommunistModusData(mcServer), (nbt, registries) -> load(nbt, registries, mcServer));
    }

    public NonNullList<ItemStack> getList() {
        return list;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int newSize) {
        size = newSize;
        setDirty();
    }

    public void setList(NonNullList<ItemStack> newList) {
        list = newList;
        setDirty();
    }

    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < list.size()) {
            list.set(index, stack);
            setDirty();
        }
    }

    public void addItem(ItemStack stack) {
        list.add(stack);
        setDirty();
    }

    public void clear() {
        list.clear();
        setDirty();
    }

    public ItemStack removeItem(int index) {
        if (index >= 0 && index < list.size()) {
            ItemStack removed = list.remove(index);
            setDirty();
            return removed;
        }
        return null;
    }

    public void broadcastUpdate() {
        CommunistUpdatePacket packet = new CommunistUpdatePacket(NonNullList.withSize(list.size(), ItemStack.EMPTY));
        for (int i = 0; i < list.size(); i++) {
            packet.items().set(i, list.get(i).copy());
        }

        for (ServerPlayer player : mcServer.getPlayerList().getPlayers()) {
            MSPacketHandler.sendToPlayer(player, packet);
        }
    }
}
