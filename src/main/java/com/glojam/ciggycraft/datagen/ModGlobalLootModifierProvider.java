package com.glojam.ciggycraft.datagen;

import com.glojam.ciggycraft.Ciggycraft;
import com.glojam.ciggycraft.item.ModItems;
import com.glojam.ciggycraft.loot.AddItemModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;


public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Ciggycraft.MODID);
    }

    @Override
    protected void start() {
        this.add("ciggy_from_zombie",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/zombie")).build(),
                        LootItemRandomChanceCondition.randomChance(0.0083f).build()
                }, ModItems.CIGARETTE.get())
        );
        this.add("tobacco_seeds_from_minecart_chest",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/abandoned_mineshaft")).build(),
                        LootItemRandomChanceCondition.randomChance(0.7f).build()
                }, ModItems.TOBACCO_SEEDS.get())
        );
    }
}
