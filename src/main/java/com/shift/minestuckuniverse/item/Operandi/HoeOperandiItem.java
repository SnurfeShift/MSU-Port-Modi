package com.shift.minestuckuniverse.item.Operandi;

import com.shift.minestuckuniverse.item.Components.OperandiStoredItemComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

public class HoeOperandiItem extends HoeItem {
    public HoeOperandiItem(Tier tier, Properties properties) {
        super(tier, properties);
        properties.durability(3);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        OperandiUtility.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack storedItem = OperandiStoredItemComponent.getStoredItem(context.getItemInHand());
        Player player = context.getPlayer();
        super.useOn(context);
        if(!context.getLevel().isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (context.getItemInHand().isEmpty()) {
                ItemStack otherStack = serverPlayer.getMainHandItem();
                if (otherStack.isEmpty())
                    player.setItemInHand(InteractionHand.MAIN_HAND, storedItem);
                else {
                    player.spawnAtLocation(storedItem);
                }
            }
        }
        return InteractionResult.PASS;
    }
}
