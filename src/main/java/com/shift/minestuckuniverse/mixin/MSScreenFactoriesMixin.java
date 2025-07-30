/*package com.example.examplemod.mixin;

import com.example.examplemod.inventory.modus.MSUModusTypes;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.client.gui.captchalouge.SetSylladexScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mixin(MSScreenFactories.class)
public class MSScreenFactoriesMixin {

    @Inject(method = "registerScreenFactories", at = @At("TAIL"))
    private static void injectRegisterArray(RegisterMenuScreensEvent event, CallbackInfo ci) {
        // Add the extra registerSylladexFactory call at the end of registerScreenFactories
        MSScreenFactories.registerSylladexFactory(MSUModusTypes.ARRAY, SetSylladexScreen::new);
    }
}*/