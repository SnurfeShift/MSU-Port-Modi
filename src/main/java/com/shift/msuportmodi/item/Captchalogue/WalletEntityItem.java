package com.shift.msuportmodi.item.Captchalogue;

import com.shift.msuportmodi.client.gui.tooltip.WalletEntityTooltip;
import com.shift.msuportmodi.item.MSUItemComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class WalletEntityItem extends Item {
    public WalletEntityItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide)
            return;

        if (entity instanceof Player player)
        {
            WalletEntityItem.spawnEntity(stack, player, level);
        }
    }

    public static Entity getEntity(CompoundTag tag, ItemStack stack, Level level) {
        Entity entity = ((EntityType<?>) BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(tag.getString("id")))).create(level);
        entity.load(tag);
        return entity;
    }

    public static void spawnEntity(ItemStack stack, Player player, Level level) {
        CompoundTag tag = stack.get(MSUItemComponents.STORED_ENTITY);

        if (tag == null || tag.isEmpty())
            return;

        Entity entity = getEntity(tag, stack, level);
        entity.moveTo(player.getX(), player.getY(), player.getZ());
        level.addFreshEntity(entity);

        player.getInventory().removeItem(stack);
    }

    //Incase the item somehow ends up in inventory without being removed via inventoryTick, those show data it normally should.
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();

        if (player == null)
            return InteractionResult.FAIL;

        ItemStack stack = context.getItemInHand();
        CompoundTag tag = stack.get(MSUItemComponents.STORED_ENTITY);

        if (level.isClientSide || !(tag != null && !tag.isEmpty()))
            return InteractionResult.FAIL;

        Entity entity = getEntity(tag, stack, level);
        BlockPos blockPos = context.getClickedPos();
        entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, 0, 0);
        player.getInventory().removeItem(stack);
        return InteractionResult.SUCCESS;
    }

    /*@Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        CompoundTag storedTag = stack.get(MSUItemComponents.STORED_ENTITY);
        if(storedTag != null) {
            String id = storedTag.getString("id");
            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(id));
            Component entityName = type.getDescription();
            tooltipComponents.add(makeTooltipInfo(entityName));
        }
    }

    private static Component makeTooltipInfo(Component info) {
        return Component.literal("(").append(info).append(")").withStyle(ChatFormatting.GRAY);
    }*/

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag tag = stack.get(MSUItemComponents.STORED_ENTITY);

        if (tag != null && tag.contains("id")) {
            String id = tag.getString("id");
            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(id));
            return type.getDescription();
        }

        return super.getName(stack);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        CompoundTag tag = stack.get(MSUItemComponents.STORED_ENTITY);
        if(tag != null && !tag.isEmpty()) {
            return Optional.of(new WalletEntityTooltip(tag));
        }
        return Optional.empty();
    }
}
