package com.glojam.ciggycraft.datagen;

import com.glojam.ciggycraft.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PACK_OF_CIGARETTES.get())
                .pattern("PPP")
                .pattern("PBP")
                .pattern("PPP")
                .define('P', Items.PAPER)
                .define('B', Items.BUNDLE)
                .unlockedBy("has_cigarette", has(ModItems.PACK_OF_CIGARETTES)).save(recipeOutput);
        shapeless(RecipeCategory.FOOD, ModItems.CIGARETTE, 1)
                .requires(ModItems.TOBACCO)
                .requires(Items.PAPER)
                .unlockedBy("has_tobacco", has(ModItems.TOBACCO)).save(recipeOutput);
        shapeless(RecipeCategory.DECORATIONS, Items.BROWN_DYE, 1).requires(ModItems.TOBACCO).unlockedBy("has_tobacco", has(ModItems.TOBACCO)).save(recipeOutput);
    }
}
