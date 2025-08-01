package com.shift.minestuckuniverse.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipeBuilder;
import com.mraof.minestuck.data.recipe.GeneratedGristCostBuilder;
import com.shift.minestuckuniverse.item.MSUItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import static com.mraof.minestuck.api.alchemy.GristTypes.*;
public final class MSUGristCostProvider {
    public static void buildRecipes(RecipeOutput recipeSaver) {
        GristCostRecipeBuilder.of(MSUItems.CAPITALIST_MODUS_CARD).grist(BUILD, 270).grist(DIAMOND, 5).grist(GOLD, 55).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.COMMUNIST_MODUS_CARD).grist(BUILD, 707).grist(RUBY, 128).grist(GOLD, 128).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.OPERANDI_MODUS_CARD).grist(BUILD, 340).grist(GARNET, 24).grist(SHALE, 33).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.SLIME_MODUS_CARD).grist(BUILD, 340).grist(CAULK, 28).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.WALLET_MODUS_CARD).grist(BUILD, 50000).grist(CAULK, 50000).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.ARRAY_MODUS_CARD).grist(BUILD, 3000000).buildFor(recipeSaver, Minestuck.MOD_ID);
    }
}