package com.glojam.ciggycraft.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

/*
    Sourced from Tide 2 "TooltipItem", available under MPL-2.0 license
    Source: https://github.com/Lightning-64/Tide-2/blob/main/src/main/java/com/li64/tide/registries/items/TooltipItem.java#L9
 */
public interface TooltipItem {
    void addTooltip(ItemStack stack, Consumer<Component> tooltip);
}