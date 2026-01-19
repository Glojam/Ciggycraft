package com.glojam.ciggycraft.item;

import com.glojam.ciggycraft.Ciggycraft;
import com.glojam.ciggycraft.block.ModBlocks;
import com.glojam.ciggycraft.components.ModDataComponents;
import com.glojam.ciggycraft.item.custom.Cigarette;
import com.glojam.ciggycraft.item.custom.PackOfCigarettes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.time.Instant;
import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Ciggycraft.MODID);

    public static final DeferredItem<Item> TOBACCO_SEEDS = ITEMS.register("tobacco_seeds",
            () -> new ItemNameBlockItem(ModBlocks.TOBACCO_CROP.get(), new Item.Properties()));

    public static final DeferredItem<Item> TOBACCO = ITEMS.register("tobacco",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CIGARETTE = ITEMS.register("cigarette",
            () -> new Cigarette(new Item.Properties().stacksTo(20)));

    public static final DeferredItem<Item> PACK_OF_CIGARETTES = ITEMS.register("pack_of_cigarettes",
            () -> new PackOfCigarettes(new Item.Properties().component(DataComponents.BUNDLE_CONTENTS, new BundleContents(List.of())).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
