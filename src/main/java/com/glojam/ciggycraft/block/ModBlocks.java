package com.glojam.ciggycraft.block;

import com.glojam.ciggycraft.Ciggycraft;
import com.glojam.ciggycraft.block.custom.HangingTobaccoLeavesBlock;
import com.glojam.ciggycraft.block.custom.TobaccoCropBlock;
import com.glojam.ciggycraft.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.swing.*;
import java.util.function.Supplier;

import static net.minecraft.world.item.Items.registerBlock;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Ciggycraft.MODID);

    public static final DeferredBlock<Block> TOBACCO_CROP = BLOCKS.register("tobacco_crop",
            () -> new TobaccoCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEETROOTS)));

    public static final DeferredBlock<Block> HANGING_TOBACCO_LEAVES = registerBlock("hanging_tobacco_leaves",
            () -> new HangingTobaccoLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HANGING_ROOTS).randomTicks().sound(SoundType.AZALEA_LEAVES)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
