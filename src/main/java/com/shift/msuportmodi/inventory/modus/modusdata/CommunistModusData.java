package com.shift.msuportmodi.inventory.modus.modusdata;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.shift.msuportmodi.MSUPortModi;
import com.shift.msuportmodi.network.CommunistUpdatePacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Iterator;


public class CommunistModusData extends SavedData {
    public static final String ID = MSUPortModi.MOD_ID+"communist_modus_data";

    private int size;
    private NonNullList<ItemStack> list;

    public final MinecraftServer mcServer;

    public CommunistModusData(MinecraftServer mcServer) {
        this.size = 5;
        this.list = NonNullList.create();
        this.mcServer = mcServer;
    }

    public static CommunistModusData get(Level level)
    {
        MinecraftServer server = level.getServer();
        if(server == null)
            throw new IllegalArgumentException("Attempted to get CommunistData through client");
        return get(server);
    }

    public static CommunistModusData get(MinecraftServer mcServer)
    {
        ServerLevel level = mcServer.overworld();
        DimensionDataStorage storage = level.getDataStorage();

        return storage.computeIfAbsent(factory(mcServer), ID);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("size", size);
        Iterator<ItemStack> iter = list.iterator();

        for (int i = 0; i< list.size(); i++) {
            ItemStack item = iter.next();
            tag.put("item"+i, item.save(provider));
        }
        return tag;
    }

    public static CommunistModusData load(CompoundTag tag, HolderLookup.Provider provider, MinecraftServer mcServer) {
        CommunistModusData data = new CommunistModusData(mcServer);
        data.size = tag.getInt("size");
        data.list = NonNullList.create();
        for (int i = 0; i < data.size; i++) {
            if (tag.contains("item" + i, Tag.TAG_COMPOUND)) {
                data.list.add(ItemStack.parseOptional(provider, tag.getCompound("item"+i)));
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

    public void setSize(int size) {
        this.size = size;
        setDirty();
    }

    public void setList(NonNullList<ItemStack> list) {
        this.list = list;
        setDirty();
    }

    public void setItem(int i, ItemStack item) {
        if (i >= 0 && i < list.size()) {
            list.set(i, item);
            setDirty();
        }
    }

    public void addItem(ItemStack item) {
        list.add(item);
        setDirty();
    }

    public void clear() {
        list.clear();
        setDirty();
    }

    public ItemStack removeItem(int i) {
        if (i >= 0 && i < list.size()) {
            ItemStack item = list.remove(i);
            setDirty();
            return item;
        }
        return null;
    }

    public static void initializePlayer(ServerPlayer player) {
        CommunistModusData data = CommunistModusData.get(player.server);
        data.sendUpdateToPlayer(player);
    }

    public void sendUpdateToPlayer(ServerPlayer player) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", size);
        Iterator<ItemStack> iter = list.iterator();

        for (int i = 0; i< list.size(); i++) {
            ItemStack item = iter.next();
            tag.put("item"+i, item.save(mcServer.registryAccess()));
        }

        CommunistUpdatePacket packet = new CommunistUpdatePacket(tag);

        PacketDistributor.sendToPlayer(player, packet);
        Modus modus = CaptchaDeckHandler.getModus(player);
        modus.markDirty();
        modus.checkAndResend(player);
    }

    public void broadcastUpdate() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", size);
        Iterator<ItemStack> iter = list.iterator();

        for (int i = 0; i< list.size(); i++) {
            ItemStack item = iter.next();
            tag.put("item"+i, item.save(mcServer.registryAccess()));
        }

        CommunistUpdatePacket packet = new CommunistUpdatePacket(tag);

        for (ServerPlayer player : mcServer.getPlayerList().getPlayers()) {
            PacketDistributor.sendToPlayer(player, packet);
            Modus modus = CaptchaDeckHandler.getModus(player);
            modus.markDirty();
            modus.checkAndResend(player);
        }
    }
}
