package com.shift.msuportmodi.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipeBuilder;
import com.shift.msuportmodi.item.MSUItems;
import net.minecraft.data.recipes.RecipeOutput;

import static com.mraof.minestuck.api.alchemy.GristTypes.*;
public final class MSUGristCostProvider {
    public static void buildRecipes(RecipeOutput recipeSaver) {
        GristCostRecipeBuilder.of(MSUItems.CAPITALIST_MODUS_CARD).grist(BUILD, 270).grist(DIAMOND, 5).grist(GOLD, 55).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.COMMUNIST_MODUS_CARD).grist(BUILD, 707).grist(RUBY, 128).grist(GOLD, 128).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.OPERANDI_MODUS_CARD).grist(BUILD, 340).grist(GARNET, 24).grist(SHALE, 33).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.SLIME_MODUS_CARD).grist(BUILD, 340).grist(CAULK, 28).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.WALLET_MODUS_CARD).grist(BUILD, 500).grist(CAULK, 500).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.ARRAY_MODUS_CARD).grist(BUILD, 300).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.ALCHEMY_MODUS_CARD).grist(BUILD, 2500).grist(URANIUM, 640).grist(ARTIFACT, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.WILDMAGIC_MODUS_CARD).grist(AMETHYST, 65).grist(BUILD, 310).grist(GARNET, 32).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.CYCLONE_MODUS_CARD).grist(BUILD, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
        GristCostRecipeBuilder.of(MSUItems.GAMBLING_MODUS_CARD).grist(BUILD, 440).grist(DIAMOND, 10).grist(GOLD, 70).buildFor(recipeSaver, Minestuck.MOD_ID);

    }
}