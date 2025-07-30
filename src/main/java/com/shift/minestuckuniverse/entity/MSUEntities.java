package com.shift.minestuckuniverse.entity;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.entity.entities.EntityCruxiteSlime;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MSUEntities {
    public static final DeferredRegister<EntityType<?>> REGISTER =
        DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MinestuckUniverseModus.MOD_ID);

    public static final Supplier<EntityType<EntityCruxiteSlime>> CRUXITE_SLIME =
            REGISTER.register("cruxite_slime", () -> EntityType.Builder.of(EntityCruxiteSlime::new, MobCategory.CREATURE)
                    .sized(0.74F, 0.74F).build("cruxite_slime"));

    public static void register(IEventBus eventbus) {
        REGISTER.register(eventbus);
    }
}
