package com.shift.msuportmodi.events;

import com.shift.msuportmodi.MSUPortModi;
import com.shift.msuportmodi.item.Captchalogue.WalletEntityItem;
import com.shift.msuportmodi.item.MSUItemComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventBusSubscriber(modid = MSUPortModi.MOD_ID)
public class ItemEntitySpawnEventHandler {

    @SubscribeEvent
    public static void onItemEntitySpawn(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof ItemEntity item))
            return;

        ItemStack stack = item.getItem();
        if (!(stack.getItem() instanceof WalletEntityItem))
            return;

        CompoundTag tag = stack.get(MSUItemComponents.STORED_ENTITY);
        if (tag == null || tag.isEmpty())
            return;

        Entity entity = WalletEntityItem.getEntity(tag, stack, event.getLevel());
        entity.moveTo(item.position());
        event.getLevel().addFreshEntity(entity);

        event.setCanceled(true);
    }
}
