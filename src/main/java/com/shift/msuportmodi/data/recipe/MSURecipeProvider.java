package com.shift.msuportmodi.data.recipe;

import com.mraof.minestuck.data.recipe.MinestuckRecipeProvider;
import com.mraof.minestuck.item.MSItems;
import com.shift.msuportmodi.item.MSUItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;

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
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MSUItems.CYCLONE_MODUS_CARD.get())
                .define('#', MSItems.RAW_CRUXITE.get())
                .define('$', MSItems.CAPTCHA_CARD.get())
                .pattern(" # ").pattern("#$#").pattern(" # ")
                .unlockedBy("has_raw_cruxite", has(MSItems.RAW_CRUXITE.get())).save(recipeBuilder);

    }
}
