package com.shift.minestuckuniverse.data.recipe;

import com.mraof.minestuck.data.recipe.MinestuckRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.concurrent.CompletableFuture;
// Extends MinestuckRecipeProvider incase I ever need to use cookingRecipesFor()
public class MSURecipeProvider extends MinestuckRecipeProvider {
    public MSURecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeBuilder) {
        MSUGristCostProvider.buildRecipes(recipeBuilder);
        MSUCombinationsProvider.buildRecipes(recipeBuilder);
    }
}
