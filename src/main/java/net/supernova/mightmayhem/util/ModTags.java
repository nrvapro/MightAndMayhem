package net.supernova.mightmayhem.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.supernova.mightmayhem.MightMayhem;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");
        public static final TagKey<Block> NEEDS_STEEL_TOOL = tag("needs_steel_tool");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(MightMayhem.MOD_ID, name));
        }

    }

    public static class Items{

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(MightMayhem.MOD_ID, name));
        }
    }
}
