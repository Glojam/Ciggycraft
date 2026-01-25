package com.glojam.ciggycraft.block.custom;

import com.glojam.ciggycraft.Ciggycraft;
import com.glojam.ciggycraft.item.custom.Cigarette;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HangingRootsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HangingTobaccoLeavesBlock extends HangingRootsBlock {
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    private static final VoxelShape TOBACCO_SHAPE =
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    @Override
    protected VoxelShape getShape(BlockState p_153342_, BlockGetter p_153343_, BlockPos p_153344_, CollisionContext p_153345_) {
        return TOBACCO_SHAPE;
    }

    @Override
    protected float getMaxHorizontalOffset() {
        return 0f;
    }

    @Override
    protected float getMaxVerticalOffset() {
        return 0f;
    }

    public HangingTobaccoLeavesBlock(Properties properties) {
        super(properties);
    }

    public static boolean isValidBlockPos(LevelReader reader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = reader.getBlockState(blockpos);
        return blockstate.isFaceSturdy(reader, blockpos, Direction.DOWN);
    }

    private void dry(BlockState state, ServerLevel world, BlockPos pos) {
        int i = Math.min(state.getValue(AGE) + 1, MAX_AGE);
        world.setBlock(pos, state.setValue(AGE, i), Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(AGE) >= MAX_AGE) return;
        if (level.dimension().equals(Level.OVERWORLD)) {
            if (level.isNight()) return;
        }
        int numSidesWithSkyLight = 0;
        if (level.canSeeSky(pos.north())) numSidesWithSkyLight++;
        if (level.canSeeSky(pos.south())) numSidesWithSkyLight++;
        if (level.canSeeSky(pos.east())) numSidesWithSkyLight++;
        if (level.canSeeSky(pos.west())) numSidesWithSkyLight++;
        if (level.dimension().equals(Level.NETHER)) numSidesWithSkyLight = 4;
        if (numSidesWithSkyLight < 1) return;
        float humidity = level.getBiome(pos).value().getModifiedClimateSettings().downfall();
        boolean bl = random.nextInt((int)((15f + (humidity * 40f)) / numSidesWithSkyLight)) == 0;
        if (bl) {
            this.dry(state, level, pos);
        }
    }
}
