package shagejack.industrimania.content.world.gen;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import shagejack.industrimania.registries.AllFeatures;

import java.util.LinkedList;
import java.util.List;

public class GenerationRegistry {

    private static final List<GenerationStep.Decoration> decorations = new LinkedList<>();
    static {
        decorations.add(GenerationStep.Decoration.UNDERGROUND_ORES);
        decorations.add(GenerationStep.Decoration.UNDERGROUND_DECORATION);
    }

    @SubscribeEvent
    public static void onBiomesLoaded(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();

        for (GenerationStep.Decoration stage : decorations) {
            List<Holder<PlacedFeature>> feats = gen.getFeatures(stage);
            List<Holder<PlacedFeature>> filtered = OreGenRemover.filterFeatures(feats);
            for (Holder<PlacedFeature> feature : filtered) {
                feats.remove(feature);
            }
        }

        if (event.getCategory() != Biome.BiomeCategory.THEEND && event.getCategory() != Biome.BiomeCategory.NETHER) {
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AllFeatures.ORE_REMOVAL_PLACED);
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AllFeatures.ROCK_LAYERS_PLACED);
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AllFeatures.SAND_STONE_REPLACEMENT_PLACED);
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AllFeatures.ORE_GEN_PLACED);
        }

        if (event.getCategory() == Biome.BiomeCategory.SAVANNA || event.getCategory() == Biome.BiomeCategory.JUNGLE) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AllFeatures.RUBBER_TREE_PLACED);
        }

        if (event.getCategory() == Biome.BiomeCategory.FOREST) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AllFeatures.MULBERRY_TREE_PLACED);
        }

        if (event.getCategory() == Biome.BiomeCategory.RIVER) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AllFeatures.COBBLE_GEN_FEATURE_PLACED);
        }

    }

}
