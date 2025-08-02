/*package com.example.examplemod.mixin;

import com.example.examplemod.inventory.modus.MSUModusTypes;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModusTypes.class)
public class ModusTypesMixin {

    @Inject(method = "getTypeFromItem", at = @At("RETURN"), cancellable = true)
    private static void extendToMSUItems(Item item, CallbackInfoReturnable<ModusType<?>> cir) {
        if (cir.getReturnValue() == null) {
            ModusType<?> msuType = MSUModusTypes.getTypeFromItem(item);
            if (msuType != null) {
                cir.setReturnValue(msuType);
            }
        }
    }
}*/