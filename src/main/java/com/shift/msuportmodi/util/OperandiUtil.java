package com.shift.msuportmodi.util;

import com.shift.msuportmodi.item.Components.OperandiStoredItemComponent;
import com.shift.msuportmodi.item.MSUItemComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

// Use as utility to make other operandi items.
public class OperandiUtil {

    public static void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        OperandiStoredItemComponent storedItemComponent = stack.get(MSUItemComponents.STORED_ITEM);
        if(storedItemComponent != null) {
            ItemStack content = storedItemComponent.storedStack();
            if (!content.isEmpty()) {
                Component contentName = content.getHoverName();
                tooltipComponents.add(makeTooltipInfo(Component.literal(content.getCount() + "x").append(contentName)));
            }
        }
    }

    private static Component makeTooltipInfo(Component info)
    {
        return Component.literal("(").append(info).append(")").withStyle(ChatFormatting.GRAY);
    }

    public static ItemStack createItemWithItem(ItemStack item, ItemStack storedItem, MinecraftServer mcServer) {
        return createItemWithStorage(item, new OperandiStoredItemComponent(storedItem), mcServer);
    }

    public static ItemStack createItemWithStorage(ItemStack item, OperandiStoredItemComponent component, MinecraftServer server) {
        item.set(MSUItemComponents.STORED_ITEM, component);
        return item;
    }
}