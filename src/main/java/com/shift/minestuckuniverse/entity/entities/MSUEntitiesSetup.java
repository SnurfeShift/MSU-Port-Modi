package com.shift.minestuckuniverse.entity.entities;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.entity.MSUEntities;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = MinestuckUniverseModus.MOD_ID)
public class MSUEntitiesSetup {
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(MSUEntities.CRUXITE_SLIME.get(), Monster.createMonsterAttributes().build());
    }
}
