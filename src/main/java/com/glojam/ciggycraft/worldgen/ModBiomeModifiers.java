package com.glojam.ciggycraft.worldgen;

import com.glojam.ciggycraft.Ciggycraft;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_TOBACCO_PATCH =
            ResourceKey.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                    ResourceLocation.fromNamespaceAndPath(Ciggycraft.MODID, "add_tobacco_patch"));

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_TOBACCO_PATCH,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.LUSH_CAVES)),
                        HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.TOBACCO_PATCH_PLACED)),
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );
    }
}
