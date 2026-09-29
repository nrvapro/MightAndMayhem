package net.supernova.mightmayhem.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.supernova.mightmayhem.MightMayhem;
import net.supernova.mightmayhem.item.ModItems;
import net.supernova.mightmayhem.loot.AddItemModifier;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, MightMayhem.MOD_ID);
    }

    @Override
    protected void start() {
        add("steel_ore_from_iron", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.IRON_ORE).build(),
                LootItemRandomChanceCondition.randomChance(0.05f).build()}, ModItems.RAW_STEEL.get()));

    }
}
