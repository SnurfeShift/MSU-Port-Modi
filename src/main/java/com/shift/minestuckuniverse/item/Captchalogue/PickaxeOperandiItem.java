package com.shift.minestuckuniverse.item.Captchalogue;

import com.shift.minestuckuniverse.item.Components.OperandiStoredItemComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PickaxeOperandiItem extends PickaxeItem {
    private static final Logger LOGGER = LoggerFactory.getLogger(PickaxeOperandiItem.class);

    public PickaxeOperandiItem(Tier tier, Properties properties) {
        super(tier, properties);
        properties.durability(3);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        OperandiUtility.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
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
