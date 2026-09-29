package net.supernova.mightmayhem.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.supernova.mightmayhem.MightMayhem;
import net.supernova.mightmayhem.block.ModBlocks;
import net.supernova.mightmayhem.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MightMayhem.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
                .add(ModBlocks.STEEL_ORE.get()).addTag(Tags.Blocks.ORES);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.STEEL_ORE.get(),
                        ModBlocks.STEEL_BLOCK.get(),
                        ModBlocks.RAW_STEEL_BLOCK.get(),
                        ModBlocks.SOUND_BLOCK.get()






                );



        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.STEEL_ORE.get());

        this.tag(ModTags.Blocks.NEEDS_STEEL_TOOL)
                .add(ModBlocks.SOUND_BLOCK.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL);

        this.tag(BlockTags.NEEDS_STONE_TOOL);

    }
}
