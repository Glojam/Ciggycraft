package com.glojam.ciggycraft.datagen;

import com.glojam.ciggycraft.Ciggycraft;
import com.glojam.ciggycraft.block.ModBlocks;
import com.glojam.ciggycraft.block.custom.HangingTobaccoLeavesBlock;
import com.glojam.ciggycraft.block.custom.TobaccoCropBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Ciggycraft.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        makeCrop(((CropBlock) ModBlocks.TOBACCO_CROP.get()), "tobacco_crop_stage", "tobacco_crop_stage");
        hangingTobacco();
        hangingTobaccoItem();
    }

    private void hangingTobacco() {
        getVariantBuilder(ModBlocks.HANGING_TOBACCO_LEAVES.get())
                .forAllStates(state -> {
                    int age = state.getValue(HangingTobaccoLeavesBlock.AGE);

                    ModelFile model = models().cross("hanging_tobacco_leaves_age" + age,
                                    modLoc("block/hanging_tobacco_leaves" + age)).renderType("cutout");

                    return new ConfiguredModel[] {
                            new ConfiguredModel(model)
                    };
                });
    }

    private void hangingTobaccoItem() {
        itemModels()
                .withExistingParent("hanging_tobacco_leaves", "minecraft:item/generated")
                .texture("layer0", modLoc("block/hanging_tobacco_leaves0"));
    }

    private static String name(Block block) {
        return block.builtInRegistryHolder().key().location().getPath();
    }

    public void makeCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> states(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    // Limitation: Hardcoded for Tobacco only
    private ConfiguredModel[] states(BlockState state, CropBlock block, String modelName, String textureName) {
        int age = state.getValue(((TobaccoCropBlock) block).getAgeProperty());
        DoubleBlockHalf half = state.getValue(TobaccoCropBlock.HALF);

        // Clamp lower half at age 3 visually
        int visualAge = age;
        if (half == DoubleBlockHalf.LOWER && age == 4) {
            visualAge = 3;
        }

        return new ConfiguredModel[] {
                new ConfiguredModel(models().cross(modelName + visualAge,
                        ResourceLocation.fromNamespaceAndPath(Ciggycraft.MODID,"block/" + textureName + visualAge)).renderType("cutout"))
        };
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("ciggycraft:block/" + deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("ciggycraft:block/" + deferredBlock.getId().getPath() + appendix));
    }
}
