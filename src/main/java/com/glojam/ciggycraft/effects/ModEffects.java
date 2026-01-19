package com.glojam.ciggycraft.effects;

import com.glojam.ciggycraft.Ciggycraft;
import com.glojam.ciggycraft.effects.custom.NicotineEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Ciggycraft.MODID);

    public static final Holder<MobEffect> NICOTINE_EFFECT = MOB_EFFECTS.register("nicotine",
            () -> new NicotineEffect(MobEffectCategory.NEUTRAL, 0x2b1812)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.withDefaultNamespace("effect.weakness"),
                            -4.0, AttributeModifier.Operation.ADD_VALUE));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
