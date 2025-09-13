package com.shift.msuportmodi.item.Captchalogue;

import com.shift.msuportmodi.item.Components.OperandiStoredItemComponent;
import com.shift.msuportmodi.util.OperandiUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ToolOperandiItem extends DiggerItem {
    public ToolOperandiItem(Tier tier, TagKey<Block> blocks, Properties properties) {
        super(tier, blocks, properties);
        properties.durability(3);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        OperandiUtil.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        ItemStack storedItem = OperandiStoredItemComponent.getStoredItem(stack);
        Tool tool = (Tool)stack.get(DataComponents.TOOL);
        if (tool == null) {
            return false;
        } else {
            if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F && tool.damagePerBlock() > 0) {
                stack.hurtAndBreak(tool.damagePerBlock(), miningEntity, EquipmentSlot.MAINHAND);
                if(tool.isCorrectForDrops(state)) {
                    //Double checks aren't necessary, however keeping them in until further testing
                    if (stack.isEmpty()) {
                        ItemStack otherStack = miningEntity.getMainHandItem();
                        if (otherStack.isEmpty())
                            miningEntity.setItemInHand(InteractionHand.MAIN_HAND, storedItem);
                        else {
                            miningEntity.spawnAtLocation(storedItem);
                        }
                    }
                }
            }
            return true;
        }
    }
}
