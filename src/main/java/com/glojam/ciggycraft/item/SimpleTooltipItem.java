package com.glojam.ciggycraft.item;

import com.glojam.ciggycraft.item.custom.TooltipItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/*
    Sourced from Tide 2 "SimpleTooltipItem", available under MPL-2.0 license
    Source: https://github.com/Lightning-64/Tide-2/blob/main/src/main/java/com/li64/tide/registries/items/SimpleTooltipItem.java#L12
 */
public abstract class SimpleTooltipItem extends Item implements TooltipItem {
    public SimpleTooltipItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, lines, flag);
        this.addTooltip(stack, lines::add);
    }
}