package com.glojam.ciggycraft.effects.custom;

import com.glojam.ciggycraft.Ciggycraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class NicotineEffect extends MobEffect {
    public NicotineEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide && livingEntity instanceof Player player) {

            if (livingEntity.level().getRandom().nextInt(25) == 0) {
                player.getFoodData().setSaturation(Math.min(20F, player.getFoodData().getSaturationLevel() + 1F));
            }

            for (MobEffectInstance effect : livingEntity.getActiveEffects()) {
                if ((effect.is(MobEffects.POISON)
                        || (effect.is(MobEffects.DIG_SLOWDOWN)))
                        || (effect.is(MobEffects.BAD_OMEN))
                        || (effect.is(MobEffects.DARKNESS))
                        || (effect.is(MobEffects.WITHER))
                        || (effect.is(MobEffects.MOVEMENT_SLOWDOWN))
                        || (effect.is(MobEffects.INFESTED))
                        || (effect.is(MobEffects.BLINDNESS))
                        || (effect.is(MobEffects.HARM))
                        || (effect.is(MobEffects.UNLUCK))
                        || (effect.is(MobEffects.CONFUSION))
                        || (effect.is(MobEffects.OOZING))
                ) {
                    livingEntity.removeEffect(effect.getEffect());
                }
            }
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
