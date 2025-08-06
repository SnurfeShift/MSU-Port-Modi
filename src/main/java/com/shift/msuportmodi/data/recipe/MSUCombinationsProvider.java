package com.shift.msuportmodi.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipeBuilder;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSTags;
import com.shift.msuportmodi.item.MSUItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

public class MSUCombinationsProvider {
    public static void buildRecipes(RecipeOutput consumer) {
        //Or = Punch Designix
        //And = Totem Lathe
        CombinationRecipeBuilder.of(MSUItems.COMMUNIST_MODUS_CARD).input(MSTags.Items.MODUS_CARD).and().input(Items.CHEST).buildFor(consumer, Minestuck.MOD_ID);
        CombinationRecipeBuilder.of(MSUItems.CAPITALIST_MODUS_CARD).input(MSTags.Items.MODUS_CARD).or().input(MSItems.BOONDOLLARS).buildFor(consumer, Minestuck.MOD_ID);
        CombinationRecipeBuilder.of(MSUItems.SLIME_MODUS_CARD).input(MSTags.Items.MODUS_CARD).and().input(Items.SLIME_BALL).buildFor(consumer, Minestuck.MOD_ID);
        CombinationRecipeBuilder.of(MSUItems.OPERANDI_MODUS_CARD).input(MSTags.Items.MODUS_CARD).and().input(Items.WOODEN_PICKAXE).buildFor(consumer, Minestuck.MOD_ID);
        CombinationRecipeBuilder.of(MSUItems.ARRAY_MODUS_CARD).input(MSTags.Items.MODUS_CARD).and().input(MSItems.RAW_CRUXITE).buildFor(consumer, Minestuck.MOD_ID);
        //CombinationRecipeBuilder.of(MSUItems.WALLET_MODUS_CARD).input(MSUItems.ARRAY_MODUS_CARD).and().input(Items.LEATHER).buildFor(consumer, Minestuck.MOD_ID);
        CombinationRecipeBuilder.of(MSUItems.WALLET_MODUS_CARD).input(MSTags.Items.MODUS_CARD).and().input(Items.LEATHER).buildFor(consumer, Minestuck.MOD_ID);
        CombinationRecipeBuilder.of(MSUItems.ALCHEMY_MODUS_CARD).input(MSTags.Items.MODUS_CARD).or().input(MSItems.ALCHEMITER).buildFor(consumer, Minestuck.MOD_ID);
        CombinationRecipeBuilder.of(MSUItems.WILDMAGIC_MODUS_CARD).input(MSTags.Items.MODUS_CARD).or().input(MSItems.GRIMOIRE).buildFor(consumer, Minestuck.MOD_ID);
        CombinationRecipeBuilder.of(MSUItems.GAMBLING_MODUS_CARD).input(MSUItems.CYCLONE_MODUS_CARD.get()).or().input(MSUItems.CAPITALIST_MODUS_CARD).buildFor(consumer, Minestuck.MOD_ID);

    }
}
