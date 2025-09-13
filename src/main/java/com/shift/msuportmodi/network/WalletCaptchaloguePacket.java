package com.shift.msuportmodi.network;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.block.machine.*;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.MSPacket;
import com.shift.msuportmodi.Config;
import com.shift.msuportmodi.MSUPortModi;
import com.shift.msuportmodi.compat.FTBCompat;
import com.shift.msuportmodi.item.MSUItemComponents;
import com.shift.msuportmodi.item.MSUItems;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.shift.msuportmodi.util.RaycastUtil;
import java.util.*;

public record WalletCaptchaloguePacket() implements MSPacket.PlayToServer
{
    public static final Type<WalletCaptchaloguePacket> ID = new Type<>(MSUPortModi.id("wallet_captchalogue"));
    public static final StreamCodec<RegistryFriendlyByteBuf, WalletCaptchaloguePacket> STREAM_CODEC = StreamCodec.unit(new WalletCaptchaloguePacket());

    @Override
    public void execute(IPayloadContext context, ServerPlayer player)
    {
        if (!captchalogueEntity(player)) {
            captchalogueBlockEntity(player);
        }
    }

    private boolean captchalogueEntity(ServerPlayer player) {
        Entity lookedAt = RaycastUtil.getLookedEntity(player, 4);
        if (!(lookedAt instanceof LivingEntity living) || lookedAt instanceof Player) return false;
        if (!FTBCompat.canInteract(player, living.blockPosition(), living)) return false;
        if (Config.SERVER.shouldEntityUseModify.get())
            if (!FTBCompat.canModify(player, living.blockPosition(), living)) return false;

        //---------Entity Blacklist Logic------------
        EntityType<?> entityType = living.getType();
        String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString();
        List<? extends String> blacklisted = Config.SERVER.blacklistedEntities.get();

        boolean matchesWildcardId = blacklisted.stream()
                .anyMatch(entry -> wildCardMatch(entityId, entry));

        if (entityType.is(MSUPortModi.blacklisted) || matchesWildcardId) return false;
        //---------------------Logic-----------------
        ItemStack stack = getEntityItem(living);

        Modus modus = CaptchaDeckHandler.getModus(player);
        if (modus == null) return false;

        if (modus.putItemStack(player, stack.copy())) {
            MSCriteriaTriggers.CAPTCHALOGUE.get().trigger(player, modus, stack);
            stack.setCount(0);
            living.discard();
            modus.checkAndResend(player);
            return true;
        }
        return false;
    }

    private boolean captchalogueBlockEntity(ServerPlayer player) {
        if (!Config.SERVER.canCaptchalogueBlockEntities.get()) return false;

        BlockPos pos = RaycastUtil.getLookedBlockPos(player, 4);
        if (pos == null) return false;

        Level level = player.level();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        BlockEntity be = level.getBlockEntity(pos);
        BlockPos actualPos = pos;

        if (be == null) return false;
        //---------Entity Blacklist Logic------------
        String beId = Objects.requireNonNull(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType())).toString();
        List<? extends String> blacklistedBEs = Config.SERVER.blacklistedBlockEntities.get();

        boolean matchesPartialId = blacklistedBEs.stream().anyMatch(entry -> wildCardMatch(beId, entry));

        if (matchesPartialId) return false;
        if (!FTBCompat.canModify(player, pos)) return false;
        //-------------------------------------------
        switch (block) {
            case AlchemiterBlock destroyable -> {
                Optional<BlockPos> mainPos = destroyable.getMainPos(state, pos, level);
                if (mainPos.isPresent()) {
                    actualPos = mainPos.get();
                    be = level.getBlockEntity(actualPos);
                }
            }
            case CruxtruderBlock destroyable -> {
                actualPos = destroyable.getMainPos(state, pos);
                be = level.getBlockEntity(actualPos);
            }
            case TotemLatheBlock destroyable -> {
                actualPos = destroyable.getMainPos(state, pos);
                be = level.getBlockEntity(actualPos);
            }
            case PunchDesignixBlock destroyable -> {
                actualPos = destroyable.getMainPos(state, pos);
                be = level.getBlockEntity(actualPos);
            }
            default -> {}
        }

        if (be == null) return false;

        ItemStack stack = getBlockEntityItem(level, actualPos);
        if (stack.isEmpty()) return false;

        Modus modus = CaptchaDeckHandler.getModus(player);
        if (modus == null) return false;

        if (modus.putItemStack(player, stack.copy())) {
            MSCriteriaTriggers.CAPTCHALOGUE.get().trigger(player, modus, stack);
            stack.setCount(0);

            if (block instanceof BedBlock) {
                BedPart part = state.getValue(BedBlock.PART);
                if (part == BedPart.FOOT) {
                    Direction facing = state.getValue(BedBlock.FACING);
                    actualPos = pos.relative(facing);
                }
            }

            switch (block) {
                case AlchemiterBlock destroyable -> destroyable.destroyFull(state, level, pos);
                case CruxtruderBlock destroyable -> destroyable.destroyFull(state, level, pos);
                case TotemLatheBlock destroyable -> destroyable.destroyFull(state, level, pos);
                case PunchDesignixBlock destroyable -> destroyable.destroyFull(state, level, pos);
                default -> {
                    level.removeBlockEntity(actualPos);
                    level.destroyBlock(actualPos, false);
                }
            }

            modus.checkAndResend(player);
            return true;
        }

        return false;
    }

    private static boolean wildCardMatch(String id, String pattern) {
        String regex = pattern.replace(".", "\\.").replace("*", ".*");
        return id.matches("(?i)" + regex);
    }

    public static ItemStack getEntityItem(LivingEntity entity)
    {
        String entityId = EntityType.getKey(entity.getType()).toString();

        CompoundTag tag = new CompoundTag();
        entity.saveWithoutId(tag);
        tag.putString("id", entityId);
        ItemStack stack = new ItemStack(MSUItems.WALLET_ENTITY_ITEM.get());
        stack.set(MSUItemComponents.STORED_ENTITY, tag);

        return stack;
    }

    public static ItemStack getBlockEntityItem(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        BlockEntity be = level.getBlockEntity(pos);

        float hardness = state.getDestroySpeed(level, pos);
        boolean breakable = hardness >= 0 && hardness < 50 && !(block instanceof AirBlock);
        Item item = block.asItem();

        if (be != null && breakable && item != Items.AIR) {
            ItemStack stack = new ItemStack(item);
            be.saveToItem(stack, level.registryAccess());
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return ID;
    }
}