package com.shift.msuportmodi.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;

public class RaycastUtil {
    public static Entity getLookedEntity(ServerPlayer player, double range) {
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

    public static BlockPos getLookedBlockPos(ServerPlayer player, double range) {
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
}
