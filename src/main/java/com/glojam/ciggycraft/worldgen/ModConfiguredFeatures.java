package com.glojam.ciggycraft.worldgen;

import com.glojam.ciggycraft.Ciggycraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> TOBACCO_PATCH =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Ciggycraft.MODID, "tobacco_patch"));

    public static final ResourceKey<ConfiguredFeature<?, ?>> TOBACCO_CROP_FEATURE =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Ciggycraft.MODID, "tobacco_crop_feature"));

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(
                TOBACCO_CROP_FEATURE,
                new ConfiguredFeature<>(
                        ModFeatures.TOBACCO_CROP.get(),
                        NoneFeatureConfiguration.INSTANCE
                )
        );

        var placed = context.lookup(Registries.PLACED_FEATURE);

        context.register(
                TOBACCO_PATCH,
                new ConfiguredFeature<>(
                        Feature.VEGETATION_PATCH,
                        new VegetationPatchConfiguration(
                                BlockTags.MOSS_REPLACEABLE,
                                BlockStateProvider.simple(Blocks.MOSS_BLOCK),
                                placed.getOrThrow(ModPlacedFeatures.TOBACCO_CROP_FEATURE_PLACED),
                                CaveSurface.FLOOR,
                                ConstantInt.of(1),
                                0.0F,
                                4,
                                0.1F,
                                UniformInt.of(1, 3),
                                0.75F
                        )
                )
        );
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Ciggycraft.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC configuration
    ) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
