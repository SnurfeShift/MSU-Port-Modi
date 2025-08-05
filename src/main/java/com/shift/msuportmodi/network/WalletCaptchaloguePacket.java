package com.shift.msuportmodi.network;

import com.mojang.logging.LogUtils;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

import java.util.*;

public record WalletCaptchaloguePacket() implements MSPacket.PlayToServer
{
    public static final Type<WalletCaptchaloguePacket> ID = new Type<>(MSUPortModi.id("wallet_captchalogue"));
    public static final StreamCodec<RegistryFriendlyByteBuf, WalletCaptchaloguePacket> STREAM_CODEC = StreamCodec.unit(new WalletCaptchaloguePacket());
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void execute(IPayloadContext context, ServerPlayer player)
    {
        Entity lookedAt = getLookedEntity(player, 4);
        if (lookedAt instanceof LivingEntity living
                && !(lookedAt instanceof Player)
                && FTBCompat.canInteract(player, living.blockPosition(), living)
        ) {
            // Config: Entity ID && tags
            EntityType<?> entityType = living.getType();
            String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString();
            if (entityType.is(MSUPortModi.blacklisted) || Config.SERVER.blacklistedEntities.get().contains(entityId)) {
                return;
            }

            // Logic
            ItemStack stack = createEntityItem(living);

            Modus modus = CaptchaDeckHandler.getModus(player);
            if(modus == null)
                return;

            boolean result = modus.putItemStack(player, stack.copy());
            if(result) {
                MSCriteriaTriggers.CAPTCHALOGUE.get().trigger(player, modus, stack);
                stack.setCount(0);
                living.discard();
                modus.checkAndResend(player);
            }
        } else {
            BlockPos pos = getLookedBlockPos(player, 4);
            if (pos == null)
                return;

            Level level = player.level();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            BlockEntity be = level.getBlockEntity(pos);
            BlockPos actualPos = pos;
            String beId = Objects.requireNonNull(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType())).toString();
            /*        List<? extends String> blacklistedBEs = Config.SERVER.blacklistedBlockEntities.get();
                    if (blacklistedBEs.contains(beId)) {
                        return;
                    }*/
            BlockEntityType<?> beType = be.getType();
            if (beType.builtInRegistryHolder().is(MSUPortModi.blacklistedblock) || Config.SERVER.blacklistedBlockEntities.get().contains(beId)) {
                return;
            }

            if (FTBCompat.canModify(player, pos)) {
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
                    default -> {
                    }
                }

                if (be != null) {
                    ItemStack stack = captchalogueTileEntity(level, actualPos);
                    if (stack.isEmpty())
                        return;

                    Modus modus = CaptchaDeckHandler.getModus(player);
                    if (modus == null)
                        return;

                    boolean result = modus.putItemStack(player, stack.copy());
                    if (result) {
                        MSCriteriaTriggers.CAPTCHALOGUE.get().trigger(player, modus, stack);
                        stack.setCount(0);

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
                    }
                }
            }
        }
    }

    public static ItemStack createEntityItem(LivingEntity entity)
    {
        String entityId = EntityType.getKey(entity.getType()).toString();

        CompoundTag tag = new CompoundTag();
        entity.saveWithoutId(tag);
        tag.putString("id", entityId);

        ItemStack stack = new ItemStack(MSUItems.WALLET_ENTITY_ITEM.get());
        stack.set(MSUItemComponents.STORED_ENTITY, tag);

        return stack;
    }

    public Entity getLookedEntity(ServerPlayer player, double range) {
        Level level = player.level();
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getLookAngle();
        Vec3 reachVec = eyePos.add(lookVec.scale(range));
        //There surely is a better way to do this, ping me @ discord if you see this and have a better idea.
        BlockHitResult blockHit = level.clip(new ClipContext(eyePos, reachVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        double blockDist = blockHit.getType() != HitResult.Type.MISS ? blockHit.getLocation().distanceTo(eyePos) : range;

        AABB playerbox = player.getBoundingBox().expandTowards(lookVec.scale(range));

        return level.getEntities(player, playerbox, e -> e instanceof LivingEntity && e != player)
                .stream()
                .flatMap(e -> e.getBoundingBox().clip(eyePos, reachVec).stream().map(hit -> new AbstractMap.SimpleEntry<>(e, hit)))
                .filter(pair -> eyePos.distanceTo(pair.getValue()) < blockDist)
                .min(Comparator.comparingDouble(pair -> eyePos.distanceTo(pair.getValue())))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public BlockPos getLookedBlockPos(ServerPlayer player, double range) {
        Level level = player.level();
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getLookAngle();
        Vec3 reachVec = eyePos.add(lookVec.scale(range));

        BlockHitResult blockHit = level.clip(new ClipContext(eyePos, reachVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            return blockHit.getBlockPos();
        }
        return null;
    }

    public static ItemStack captchalogueTileEntity(Level level, BlockPos pos) {
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