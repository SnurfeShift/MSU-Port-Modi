package com.shift.msuportmodi.compat;

import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.api.Protection;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;

public class FTBCompat {
    public static boolean canModify(ServerPlayer player, BlockPos pos) {
        if(ModList.get().isLoaded("ftbchunks")) {
            return !FTBChunksAPI.api().getManager().shouldPreventInteraction(player, InteractionHand.MAIN_HAND, pos, Protection.EDIT_BLOCK, null);
        }
        return true;
    }

    public static boolean canInteract(ServerPlayer player, BlockPos pos, LivingEntity entity) {
        if (ModList.get().isLoaded("ftbchunks")) {
            return !FTBChunksAPI.api().getManager().shouldPreventInteraction(player, InteractionHand.MAIN_HAND, pos, Protection.INTERACT_ENTITY, entity);
        }
        return true;
    }
}