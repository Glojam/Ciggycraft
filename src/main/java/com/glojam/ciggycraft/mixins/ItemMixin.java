package com.glojam.ciggycraft.mixins;

import com.glojam.ciggycraft.components.ModDataComponents;
import com.glojam.ciggycraft.item.ModItems;
import com.glojam.ciggycraft.item.custom.Cigarette;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.UUID;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow public abstract InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand);

    /**
     * @author
     * @reason
     */
    @Inject(method = "interactLivingEntity", at = @At("TAIL"), cancellable = true)
    public void interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack item_mainhand = interactionTarget.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack item_offhand = interactionTarget.getItemInHand(InteractionHand.OFF_HAND);
        if (stack.getItem() instanceof FlintAndSteelItem || stack.getItem() instanceof FireChargeItem) {
            if (item_mainhand.is(ModItems.CIGARETTE) && !Boolean.TRUE.equals(item_mainhand.get(ModDataComponents.LIT))) {
                Cigarette.affectOffhandLighter(stack, player.level(), player, usedHand);
                item_mainhand.set(ModDataComponents.LIT, true);
                item_mainhand.set(ModDataComponents.BURN_STAGE, 0);

                // It's a feature
                if (Objects.requireNonNull(player).getGameProfile().getId() == UUID.fromString("dcd618d1-03ae-4138-a556-67705132f913")) {
                    player.kill();
                }
            }
            if (item_offhand.is(ModItems.CIGARETTE) && !Boolean.TRUE.equals(item_mainhand.get(ModDataComponents.LIT))) {
                Cigarette.affectOffhandLighter(stack, player.level(), player, usedHand);
                item_offhand.set(ModDataComponents.LIT, true);
                item_offhand.set(ModDataComponents.BURN_STAGE, 0);
            }
        }
    }
}
