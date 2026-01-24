package com.glojam.ciggycraft.item.custom;

import com.glojam.ciggycraft.components.ModDataComponents;
import com.glojam.ciggycraft.effects.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Cigarette extends Item {

    private static int stackIndex = 0; // Session-unique identifiers for cigarettes to prevent them from stacking

    public Cigarette(Properties properties) {
        super(properties);
    }

    public void onCreateCigaretteStackItem(ItemStack stack) {
        stack.set(ModDataComponents.STUPID, Long.parseLong(String.valueOf(stackIndex) + System.currentTimeMillis()));
        stackIndex++;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        Integer burnStage = -1; // where -1 is unlit
        ItemStack cigStack = player.getItemInHand(usedHand);
        if (cigStack.get(ModDataComponents.BURN_STAGE) != null) {
            burnStage = cigStack.get(ModDataComponents.BURN_STAGE);
            if (burnStage > 4) return super.use(level, player, usedHand);
        }

        if (cigStack.get(ModDataComponents.LIT) == null) {
            InteractionHand oppositeHand = getOppositeHand(usedHand);
            ItemStack otherItem = player.getItemInHand(oppositeHand);
            if (otherItem.is(Items.FLINT_AND_STEEL) || otherItem.is(Items.FIRE_CHARGE)) {
                affectOffhandLighter(otherItem, level, player, oppositeHand);
                cigStack.set(ModDataComponents.LIT, true);
                cigStack.set(ModDataComponents.BURN_STAGE, 0);
            }
            return super.use(level, player, usedHand);
        }

        burnStage = Math.min(burnStage + 1, 5);
        cigStack.set(ModDataComponents.BURN_STAGE, burnStage);
        player.startUsingItem(usedHand);

        player.getCooldowns().addCooldown(this, 300);

        if (burnStage == 3) {
            return InteractionResultHolder.consume(player.getItemInHand(usedHand));
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        Player player = context.getPlayer();
        Objects.requireNonNull(player).getCooldowns().addCooldown(this, 20);
        Vec3 lookDir = player.getLookAngle();
        if ( (blockstate.is(Blocks.FIRE) || blockstate.is(Blocks.CAMPFIRE) || blockstate.is(Blocks.SOUL_CAMPFIRE)) && !Boolean.TRUE.equals(context.getItemInHand().get(ModDataComponents.LIT))) {
            context.getItemInHand().set(ModDataComponents.LIT, true);
            context.getItemInHand().set(ModDataComponents.BURN_STAGE, 0);
            level.playSound(player, BlockPos.containing(new Vec3(player.getX() + lookDir.x/2, player.getEyeY() + lookDir.y/2, player.getZ() + lookDir.z/2)),
                    SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            player.swing(context.getHand());
        }
        return super.useOn(context);
    }

    public static void affectOffhandLighter(ItemStack lighter, Level level, Player player, InteractionHand usedHand) {
        player.getCooldowns().addCooldown(Items.FLINT_AND_STEEL, 10); // Debugs accidentally setting fire
        player.getCooldowns().addCooldown(Items.FIRE_CHARGE, 10); // Debugs accidentally setting fire
        Vec3 lookDir = player.getLookAngle();

        SoundEvent soundToUse;
        if (lighter.is(Items.FLINT_AND_STEEL)) {
            soundToUse = SoundEvents.FLINTANDSTEEL_USE;
            lighter.hurtAndBreak(1, player, LivingEntity.getSlotForHand(usedHand));
        } else if (lighter.is(Items.FIRE_CHARGE)) {
            soundToUse = SoundEvents.FIRECHARGE_USE;
            lighter.consume(1, player);
        } else {
            soundToUse = SoundEvents.FLINTANDSTEEL_USE; // Fallback
        }

        level.playSound(player, BlockPos.containing(new Vec3(player.getX() + lookDir.x/2, player.getEyeY() + lookDir.y/2, player.getZ() + lookDir.z/2)),
                soundToUse, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);

        player.swing(usedHand, true);
    }

    public static InteractionHand getOppositeHand(InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return InteractionHand.OFF_HAND;
        } else {
            return InteractionHand.MAIN_HAND;
        }
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity player, int count) {
        Level level = player.level();
        RandomSource random = level.getRandom();
        Vec3 lookDir = player.getLookAngle();

        if (level instanceof ServerLevel server) {
            server.sendParticles(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    player.getX() + lookDir.x / 5,
                    player.getEyeY() + lookDir.y / 5,
                    player.getZ() + lookDir.z / 5,
                    10, // count
                    lookDir.x / 10,
                    lookDir.y / 10,
                    lookDir.z / 10,
                    0.008
            );
            level.playSound(
                    null, // null = broadcast to all players nearby
                    player.getX(),
                    player.getEyeY(),
                    player.getZ(),
                    SoundEvents.FURNACE_FIRE_CRACKLE,
                    SoundSource.PLAYERS,
                    0.5F + random.nextFloat(),
                    random.nextFloat() * 0.7F + 0.6F
            );
        }
        player.addEffect(new MobEffectInstance(ModEffects.NICOTINE_EFFECT, 900, 0), player);
        super.onStopUsing(stack, player, count);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 100;
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack stack) {
        if (stack.get(ModDataComponents.STUPID) == null) {
            onCreateCigaretteStackItem(stack);
        }
        super.verifyComponentsAfterLoad(stack);
    }
}
