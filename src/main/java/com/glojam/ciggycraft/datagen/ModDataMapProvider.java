package com.glojam.ciggycraft.datagen;

import com.glojam.ciggycraft.block.ModBlocks;
import com.glojam.ciggycraft.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(ModItems.TOBACCO_SEEDS.getId(), new Compostable(0.25f), false)
                .add(ModItems.TOBACCO.getId(), new Compostable(0.45f), false)
                .add(ModBlocks.HANGING_TOBACCO_LEAVES.getId(), new Compostable(0.6f), false)
                .add(ModItems.CIGARETTE.getId(), new Compostable(0.3f), false);
    }
}