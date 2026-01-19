package com.glojam.ciggycraft.worldgen;

import com.glojam.ciggycraft.Ciggycraft;
import com.glojam.ciggycraft.worldgen.custom.TobaccoCropFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(
                    net.minecraft.core.registries.Registries.FEATURE,
                    Ciggycraft.MODID
            );

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>>
            TOBACCO_CROP = FEATURES.register(
            "tobacco_crop",
            () -> new TobaccoCropFeature(NoneFeatureConfiguration.CODEC)
    );

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
