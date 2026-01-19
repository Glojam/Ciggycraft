package com.glojam.ciggycraft.misc;

import com.glojam.ciggycraft.components.ModDataComponents;
import com.glojam.ciggycraft.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class ModEventHandler {
    @SubscribeEvent
    public void onCigaretteDropped(EntityTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        Entity entity = event.getEntity();
        if (!(entity instanceof ItemEntity)) return;
        ItemStack stack = ((ItemEntity) entity).getItem();
        if (!(stack.is(ModItems.CIGARETTE))) return;
        Integer burnStage = stack.get(ModDataComponents.BURN_STAGE);
        if (burnStage == null || burnStage < 0 || burnStage > 4) return;
        Level level = entity.level();
        if (level.getRandom().nextInt(100)>0) return;
        BlockPos offset = entity.blockPosition().offset(level.getRandom().nextInt(2) * (level.getRandom().nextBoolean() ? -1 : 1),
                level.getRandom().nextInt(1) * (level.getRandom().nextBoolean() ? -1 : 1),
                level.getRandom().nextInt(2) * (level.getRandom().nextBoolean() ? -1 : 1)
        );
        if (!(level.getBlockState(offset).is(Blocks.AIR))) return;
        if (!BaseFireBlock.canBePlacedAt(level, offset, Direction.UP)) return;
        level.playSound(null, offset, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
        level.setBlock(offset, BaseFireBlock.getState(level, offset), 11);
    }
}
