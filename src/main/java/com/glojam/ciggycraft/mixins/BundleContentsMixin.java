package com.glojam.ciggycraft.mixins;

import com.glojam.ciggycraft.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleContents.class)
public abstract class BundleContentsMixin {
    @Inject(method = "getWeight", at = @At("HEAD"), cancellable = true)
    private static void getWeight(ItemStack stack, CallbackInfoReturnable<Fraction> cir) {
        BundleContents bundlecontents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundlecontents == null && stack.is(ModItems.CIGARETTE)) {
            List<BeehiveBlockEntity.Occupant> list = (List)stack.getOrDefault(DataComponents.BEES, List.of());
            cir.setReturnValue(!list.isEmpty() ? Fraction.ONE : Fraction.getFraction(1, 20));
        }
    }
}
