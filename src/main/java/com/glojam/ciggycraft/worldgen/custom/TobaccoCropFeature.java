package com.glojam.ciggycraft.worldgen.custom;

import com.glojam.ciggycraft.block.ModBlocks;
import com.glojam.ciggycraft.block.custom.TobaccoCropBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.List;

public class TobaccoCropFeature extends Feature<NoneFeatureConfiguration> {

    public TobaccoCropFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource rand = context.random();

        List<BlockPos> validPositions = new ArrayList<>();
        int radius = 3;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos candidate = findMossFloor(level, origin.offset(dx, 0, dz));
                if (candidate != null) validPositions.add(candidate);
            }
        }

        if (validPositions.isEmpty()) return false;

        int clusterSize = 1 + rand.nextInt(3); // 1-3 crops per placement
        clusterSize = Math.min(clusterSize, validPositions.size());

        for (int i = 0; i < clusterSize; i++) {
            BlockPos pos = validPositions.get(i);

            BlockState lower = ModBlocks.TOBACCO_CROP.get()
                    .defaultBlockState()
                    .setValue(TobaccoCropBlock.AGE, 4)
                    .setValue(TobaccoCropBlock.HALF, DoubleBlockHalf.LOWER)
                    .setValue(TobaccoCropBlock.NATURALLY_PLACED, true);

            BlockState upper = lower.setValue(TobaccoCropBlock.HALF, DoubleBlockHalf.UPPER);

            if (!level.isEmptyBlock(pos)) continue;
            if (!level.isEmptyBlock(pos.above())) continue;

            level.setBlock(pos, lower, Block.UPDATE_ALL);
            level.setBlock(pos.above(), upper, Block.UPDATE_ALL);
        }

        return true;
    }

    private BlockPos findMossFloor(WorldGenLevel level, BlockPos pos) {
        for (int y = level.getMaxBuildHeight(); y > level.getMinBuildHeight(); y--) {
            BlockPos check = new BlockPos(pos.getX(), y, pos.getZ());
            if (!level.isEmptyBlock(check)) continue;
            if (level.getBlockState(check.below()).is(Blocks.MOSS_BLOCK) || level.getBlockState(check.below()).is(Blocks.CLAY)) {
                return check;
            }
        }
        return null;
    }
}
