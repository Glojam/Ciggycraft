package com.glojam.ciggycraft.item.custom;

import com.glojam.ciggycraft.components.ModDataComponents;
import com.glojam.ciggycraft.item.ModItems;
import com.glojam.ciggycraft.item.SimpleTooltipItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/*
    Remixed from Tide 2 "FishSatchelItem", available under MPL-2.0 license
    Source: https://github.com/Lightning-64/Tide-2/blob/main/src/main/java/com/li64/tide/registries/items/FishSatchelItem.java
 */
public class PackOfCigarettes extends SimpleTooltipItem {
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public PackOfCigarettes(Item.Properties properties) {
        super(properties);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean canPutInSatchel(ItemStack stack) {
        if (stack.is(ModItems.CIGARETTE)) {
            if (stack.get(ModDataComponents.BURN_STAGE) == null) return true;
        }
        return false;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) return false;
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        ItemStack other = slot.getItem();
        if (contents == null) return false;
        BundleContents.Mutable mutable = new BundleContents.Mutable(contents);

        if (other.isEmpty()) {
            this.playRemoveOneSound(player);
            ItemStack removed = mutable.removeOne();
            if (removed != null) mutable.tryInsert(slot.safeInsert(removed));
        }
        else if (other.getItem().canFitInsideContainerItems()) {
            if (!canPutInSatchel(other)) return false;
            int i = mutable.tryTransfer(slot, player);
            if (i > 0) this.playInsertSound(player);
        }

        stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (!(action == ClickAction.SECONDARY && slot.allowModification(player))) return false;
        return overrideOtherStackedOnMe(stack, other, player, access);
    }

    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Player player, SlotAccess access) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null) return false;
        BundleContents.Mutable mutable = new BundleContents.Mutable(contents);

        if (other.isEmpty()) {
            ItemStack removed = mutable.removeOne();
            if (removed != null) {
                this.playRemoveOneSound(player);
                access.set(removed);
            }
        }
        else {
            if (!canPutInSatchel(other)) return false;
            if (mutable.tryInsert(other) > 0) this.playInsertSound(player);
        }

        stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return contents.weight().compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return Math.min(1 + Mth.mulAndTruncate(contents.weight(), 12), 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(DataComponents.BUNDLE_CONTENTS)).map(BundleTooltip::new)
                : Optional.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> lines, TooltipFlag flag) {
        BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents != null) {
            int i = Mth.mulAndTruncate(bundleContents.weight(), 20);
            lines.add(Component.translatable("item.minecraft.bundle.fullness", i, 20).withStyle(ChatFormatting.GRAY));
            stack.set(ModDataComponents.CIGARETTE_FULLNESS, i);
        }
        super.appendHoverText(stack, context, lines, flag);
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        BundleContents bundleContents = itemEntity.getItem().get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents != null) {
            itemEntity.getItem().set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
            ItemUtils.onContainerDestroyed(itemEntity, bundleContents.itemsCopy());
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
    }
}
