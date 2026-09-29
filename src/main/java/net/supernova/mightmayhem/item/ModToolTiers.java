package net.supernova.mightmayhem.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.supernova.mightmayhem.MightMayhem;
import net.supernova.mightmayhem.util.ModTags;

import java.util.List;

public class ModToolTiers {
    public static final Tier STEEL = TierSortingRegistry.registerTier(
            new ForgeTier(5, 2500 ,5, 5f ,20,
                    ModTags.Blocks.NEEDS_STEEL_TOOL, () -> Ingredient.of(ModItems.STEEL.get())),
            new ResourceLocation(MightMayhem.MOD_ID, "steel"), List.of(Tiers.DIAMOND), List.of());






}
