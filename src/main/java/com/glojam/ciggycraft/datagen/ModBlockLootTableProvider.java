package com.glojam.ciggycraft.datagen;

import com.glojam.ciggycraft.block.ModBlocks;
import com.glojam.ciggycraft.block.custom.HangingTobaccoLeavesBlock;
import com.glojam.ciggycraft.item.ModItems;
import com.glojam.ciggycraft.block.custom.TobaccoCropBlock;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;

import java.util.Arrays;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        LootItemCondition.Builder tobaccoCropCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.TOBACCO_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TobaccoCropBlock.AGE, 4));

        LootItemCondition.Builder tobaccoLowHalfCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.TOBACCO_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TobaccoCropBlock.HALF, DoubleBlockHalf.LOWER));

        this.add(ModBlocks.TOBACCO_CROP.get(), this.createTobaccoCropDrops(ModBlocks.TOBACCO_CROP.get(),
                ModBlocks.HANGING_TOBACCO_LEAVES.get().asItem(), ModItems.TOBACCO_SEEDS.get(), tobaccoCropCondition, tobaccoLowHalfCondition));

        this.add(ModBlocks.HANGING_TOBACCO_LEAVES.get(), hangingTobaccoLoot(ModBlocks.HANGING_TOBACCO_LEAVES.get()));
    }

    protected LootTable.Builder createTobaccoCropDrops(Block cropBlock, Item grownCropItem, Item seedsItem, LootItemCondition.Builder dropGrownCropCondition, LootItemCondition.Builder cropLowerHalfCondition) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.applyExplosionDecay(cropBlock, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add((LootItem.lootTableItem(grownCropItem)
                                .when(dropGrownCropCondition))
                                .when(cropLowerHalfCondition)
                                .otherwise(LootItem.lootTableItem(seedsItem))))
                .withPool(LootPool.lootPool().when(dropGrownCropCondition)
                        .when(cropLowerHalfCondition)
                        .add(LootItem.lootTableItem(seedsItem)
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(
                                        registrylookup.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)
                                )
                        )
                )
        );
    }

    private LootTable.Builder hangingTobaccoLoot(Block block) {
        return LootTable.lootTable()

                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(ageIs(block, 0, 1, 2))
                        .add(LootItem.lootTableItem(block))
                )

                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(ageIs(block, 3))
                        .add(LootItem.lootTableItem(ModItems.TOBACCO.get()))
                );
    }

    private static LootItemCondition.Builder ageIs(Block block, int... ages) {
        LootItemCondition.Builder[] conditions = Arrays.stream(ages)
                .mapToObj(age ->
                        LootItemBlockStatePropertyCondition
                                .hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(HangingTobaccoLeavesBlock.AGE, age)))
                .toArray(LootItemCondition.Builder[]::new);

        return AnyOfCondition.anyOf(conditions);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
