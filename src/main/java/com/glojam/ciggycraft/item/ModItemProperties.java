package com.glojam.ciggycraft.item;

import com.glojam.ciggycraft.Ciggycraft;
import com.glojam.ciggycraft.components.ModDataComponents;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ModItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.CIGARETTE.get(), ResourceLocation.fromNamespaceAndPath(Ciggycraft.MODID, "burn_stage"),
                ((itemStack, clientLevel, livingEntity, i) -> {
                    Integer burnStage = itemStack.get(ModDataComponents.BURN_STAGE);
                    return burnStage != null ? burnStage : -1;
                }
                ));
    }
}
