package shagejack.industrimania.foundation.utility;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.IEventBus;
import shagejack.industrimania.foundation.block.renderer.CustomBlockModels;
import shagejack.industrimania.foundation.item.renderer.CustomItemModels;
import shagejack.industrimania.foundation.item.renderer.CustomRenderedItemModel;
import shagejack.industrimania.foundation.item.renderer.CustomRenderedItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ModelSwapper {

    protected CustomBlockModels customBlockModels = new CustomBlockModels();
    protected CustomItemModels customItemModels = new CustomItemModels();
    protected CustomRenderedItems customRenderedItems = new CustomRenderedItems();

    public CustomBlockModels getCustomBlockModels() {
        return customBlockModels;
    }

    public CustomItemModels getCustomItemModels() {
        return customItemModels;
    }

    public CustomRenderedItems getCustomRenderedItems() {
        return customRenderedItems;
    }

    public void onModelRegistry(ModelRegistryEvent event) {
        customRenderedItems.forEach((item, modelFunc) -> modelFunc.apply(null)
                .getModelLocations()
                .forEach(ForgeModelBakery::addSpecialModel));
    }

    public void onModelBake(ModelBakeEvent event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModelRegistry();

        customBlockModels.forEach((block, modelFunc) -> swapModels(modelRegistry, getAllBlockStateModelLocations(block), modelFunc));
        customItemModels.forEach((item, modelFunc) -> swapModels(modelRegistry, getItemModelLocation(item), modelFunc));
        customRenderedItems.forEach((item, modelFunc) -> {
            swapModels(modelRegistry, getItemModelLocation(item), m -> {
                CustomRenderedItemModel swapped = modelFunc.apply(m);
                swapped.loadPartials(event);
                return swapped;
            });
        });
    }

    public void registerListeners(IEventBus modEventBus) {
        modEventBus.addListener(this::onModelRegistry);
        modEventBus.addListener(this::onModelBake);
    }

    public static <T extends BakedModel> void swapModels(Map<ResourceLocation, BakedModel> modelRegistry,
                                                         List<ModelResourceLocation> locations, Function<BakedModel, T> factory) {
        locations.forEach(location -> {
            swapModels(modelRegistry, location, factory);
        });
    }

    public static <T extends BakedModel> void swapModels(Map<ResourceLocation, BakedModel> modelRegistry,
                                                         ModelResourceLocation location, Function<BakedModel, T> factory) {
        modelRegistry.put(location, factory.apply(modelRegistry.get(location)));
    }

    public static List<ModelResourceLocation> getAllBlockStateModelLocations(Block block) {
        List<ModelResourceLocation> models = new ArrayList<>();
        ResourceLocation blockRl = block.getRegistryName();
        block.getStateDefinition()
                .getPossibleStates()
                .forEach(state -> {
                    models.add(BlockModelShaper.stateToModelLocation(blockRl, state));
                });
        return models;
    }

    public static ModelResourceLocation getItemModelLocation(Item item) {
        return new ModelResourceLocation(item.getRegistryName(), "inventory");
    }

}