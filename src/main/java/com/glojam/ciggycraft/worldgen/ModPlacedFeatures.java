package com.glojam.ciggycraft.worldgen;

import com.glojam.ciggycraft.Ciggycraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> TOBACCO_CROP_FEATURE_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Ciggycraft.MODID, "tobacco_crop_feature_placed"));

    public static final ResourceKey<PlacedFeature> TOBACCO_PATCH_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Ciggycraft.MODID, "tobacco_patch_placed"));

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configured = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(
                ModPlacedFeatures.TOBACCO_CROP_FEATURE_PLACED,
                new PlacedFeature(
                        configured.getOrThrow(ModConfiguredFeatures.TOBACCO_CROP_FEATURE),
                        List.of(
                                InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                                CountPlacement.of(1)
                        )
                )
        );

        context.register(
                ModPlacedFeatures.TOBACCO_PATCH_PLACED,
                new PlacedFeature(
                        configured.getOrThrow(ModConfiguredFeatures.TOBACCO_PATCH),
                        List.of(
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(-60),
                                        VerticalAnchor.absolute(40)
                                ),
                                CountPlacement.of(1)
                        )
                )
        );
    }
}
