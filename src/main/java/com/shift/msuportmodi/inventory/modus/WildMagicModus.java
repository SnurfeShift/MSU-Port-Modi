package com.shift.msuportmodi.inventory.modus;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

import java.util.Objects;

public class WildMagicModus extends ArrayModus {
    public WildMagicModus(ModusType<? extends ArrayModus> type, LogicalSide side) {
        super(type, side);
    }

    private static final float cost = 10;
    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard){
        if(id == CaptchaDeckHandler.EMPTY_CARD)
        {
            if(list.size() < size)
            {
                size--;
                markDirty();
                return new ItemStack(MSItems.CAPTCHA_CARD.get());
            } else return ItemStack.EMPTY;
        }

        if(list.isEmpty())
            return ItemStack.EMPTY;

        if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
        {
            for(ItemStack item : list) {
                CaptchaDeckHandler.launchAnyItem(player, item);
            }
            list.clear();
            markDirty();
            return ItemStack.EMPTY;
        }

        if(id < 0 || id >= list.size())
            return ItemStack.EMPTY;

        if(player.getFoodData().getFoodLevel() <= 0) {
            player.sendSystemMessage(Component.literal("You are too exhausted to perform this action."));
            return ItemStack.EMPTY;
        }

        ItemStack item = list.get(id);

        if(asCard)
        {
            size--;
            markDirty();
            list.remove(id);
            return CaptchaCardItem.createCardWithItem(item, player.server);
        }

        boolean giveItem = wildCardMagic(player);
        if(giveItem) {
            list.remove(id);
            markDirty();
            return item;
        }

        return ItemStack.EMPTY;
    }

    private static boolean wildCardMagic(ServerPlayer player) {
        float luck = player.getLuck();
        RandomSource random = player.level().getRandom();
        int bound = Math.max(1, 20 - (int)Math.abs(luck));
        int bonus = (int)(luck > 0 ? luck : 0);
        int roll = Math.max(0, Math.min(20, random.nextInt(bound) + 1 + bonus));
        player.causeFoodExhaustion(cost);
        player.sendSystemMessage(Component.literal("You rolled a " + roll));

        Holder<MobEffect> effect = null;
        int amp = 1;
        boolean giveItem = false;
        boolean clearEffects = false;

        switch (roll) {
            case 1, 2 -> effect = getEffectHolder("wither");
            case 3 -> {
                effect = getEffectHolder("poison");
                amp = 0;
            }
            case 4 -> effect = getEffectHolder("slowness");
            case 5, 6 -> {
                effect = getEffectHolder("slowness");
                giveItem = true;
            }
            case 9 -> {
                giveItem = true;
                clearEffects = true;
            }
            case 7, 8 -> clearEffects = true;
            case 10, 11, 12, 13, 14 -> giveItem = true;
            case 15 -> {
                effect = getEffectHolder("water_breathing");
                amp = 0;
                giveItem = true;
            }
            case 16, 17 -> {
                effect = getEffectHolder("speed");
                giveItem = true;
            }
            case 18, 19 -> {
                effect = getEffectHolder("regeneration");
                amp = 2;
                giveItem = true;
            }
            case 20 -> {
                effect = getEffectHolder("absorption");
                amp = 3;
                giveItem = true;
            }
        }

        if(clearEffects)
            player.removeAllEffects();

        if (effect != null) {
            int duration = 300;
            if (player.hasEffect(effect)) {
                duration += Objects.requireNonNull(player.getEffect(effect)).getDuration();
            }
            duration = Math.min(duration, 450);
            player.addEffect(new MobEffectInstance(effect, duration, amp));
        }

        return giveItem;
    }

    private static Holder<MobEffect> getEffectHolder(String id) {
        ResourceLocation rl = ResourceLocation.fromNamespaceAndPath("minecraft", id);
        ResourceKey<MobEffect> key = ResourceKey.create(Registries.MOB_EFFECT, rl);
        return BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(key);
    }
}
