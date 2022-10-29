package shagejack.industrimania.registries.dataGen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.foundation.utility.Wrapper;
import shagejack.industrimania.registries.block.AllBlocks;
import shagejack.industrimania.registries.AllTabs;
import shagejack.industrimania.registries.RegisterHandle;
import shagejack.industrimania.registries.block.grouped.AllOres;
import shagejack.industrimania.registries.item.AllItems;
import shagejack.industrimania.registries.item.grouped.AllOreChunks;
import shagejack.industrimania.registries.recipe.ProcessingRecipeGen;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static shagejack.industrimania.Industrimania.MOD_ID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenHandle {

    public static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    public static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

    private static final ArrayList<Consumer<ItemModelProvider>> itemModelTasks = new ArrayList<>();
    private static final ArrayList<Consumer<BlockModelProvider>> blockModelTasks = new ArrayList<>();
    private static final ArrayList<Consumer<BlockStateProvider>> blockStateTasks = new ArrayList<>();
    private static final ArrayList<Consumer<LanguageProvider>> languageTasks = new ArrayList<>();
    private static final ArrayList<TagTask<Block>> blockTagsTasks = new ArrayList<>();
    private static final ArrayList<TagTask<Item>> itemTagsTasks = new ArrayList<>();
    private static final Wrapper<ItemModelProvider> itemModelPro = new Wrapper<>();
    private static final Wrapper<BlockModelProvider> blockModelPro = new Wrapper<>();
    private static final Wrapper<BlockStateProvider> blockStatePro = new Wrapper<>();
    private static final Wrapper<LanguageProvider> languagePro = new Wrapper<>();
    private static final ArrayList<TagTask<? extends Fluid>> fluidTagsTasks = new ArrayList<>();
    private static final Wrapper<BlockTagsProvider> blockTagsPro = new Wrapper<>();
    private static final Wrapper<ItemTagsProvider> itemTagsPro = new Wrapper<>();
    private static final Wrapper<FluidTagsProvider> fluidTagsPro = new Wrapper<>();
    public static Lazy<ExistingModelFile> itemGeneratedModel = () -> existingModel(itemModelPro.get(), "item/generated");
    public static Lazy<ExistingModelFile> itemHeldModel = () -> existingModel(itemModelPro.get(), "item/handheld");
    public static Lazy<UncheckedModelFile> blockBuiltinEntity = () -> uncheckedModel("builtin/entity");
    public static Lazy<ExistingModelFile> blockCubeAll = () -> existingModel(itemModelPro.get(), "block/cube_all");
    public static Lazy<ExistingModelFile> blockCube = () -> existingModel(itemModelPro.get(), "block/cube");
    public static Lazy<ExistingModelFile> blockCubeRotatable = () -> existingModel(itemModelPro.get(), "");
    public static Lazy<ExistingModelFile> blockCrossTexture = () -> existingModel(itemModelPro.get(), "block/cross");
    public static Lazy<ExistingModelFile> blockOre = () -> modExistingModel(itemModelPro.get(), "block/ore");
    public static Lazy<ExistingModelFile> blockCubeRGBOverlay = () -> modExistingModel(itemModelPro.get(), "block/cube_rgb_overlay");
    public static Lazy<ExistingModelFile> blockSnowLikeModel0 = () -> modExistingModel(itemModelPro.get(), "block/snow_like_height2");
    public static Lazy<ExistingModelFile> blockSnowLikeModel1 = () -> modExistingModel(itemModelPro.get(), "block/snow_like_height4");
    public static Lazy<ExistingModelFile> blockSnowLikeModel2 = () -> modExistingModel(itemModelPro.get(), "block/snow_like_height6");
    public static Lazy<ExistingModelFile> blockSnowLikeModel3 = () -> modExistingModel(itemModelPro.get(), "block/snow_like_height8");
    public static Lazy<ExistingModelFile> blockSnowLikeModel4 = () -> modExistingModel(itemModelPro.get(), "block/snow_like_height10");
    public static Lazy<ExistingModelFile> blockSnowLikeModel5 = () -> modExistingModel(itemModelPro.get(), "block/snow_like_height12");
    public static Lazy<ExistingModelFile> blockSnowLikeModel6 = () -> modExistingModel(itemModelPro.get(), "block/snow_like_height14");

    public static void addBlockTagTask(TagTask<Block> task) {
        runOnDataGen(() -> () -> blockTagsTasks.add(task));
    }
    public static void addItemTagTask(TagTask<Item> task) {
        runOnDataGen(() -> () -> itemTagsTasks.add(task));
    }
    public static void addFluidTagTask(TagTask<? extends Fluid> task) {
        runOnDataGen(() -> () -> fluidTagsTasks.add(task));
    }

    public static boolean checkFileExist(ItemModelProvider provider, ResourceLocation resourceLocation, ExistingFileHelper.IResourceType packType) {
        final var exists = provider.existingFileHelper.exists(resourceLocation, packType);
        if (!exists) {
            Industrimania.LOGGER.info("{} not exist for block/item in path:{}}", packType.getPrefix(), resourceLocation.toString());
        }
        return exists;
    }

    public static boolean checkFileExist(ItemModelProvider provider, String path, ExistingFileHelper.IResourceType packType) {
        return provider.existingFileHelper.exists(new ResourceLocation(MOD_ID, path), packType);
    }

    public static boolean checkItemTextureExist(ItemModelProvider provider, String name, String texture) {
        return checkFileExist(provider,
                "item/" + name + "/" + texture,
                DataGenHandle.TEXTURE);
    }

    public static boolean checkItemModelExist(ItemModelProvider provider, String name, String texture) {
        return checkFileExist(provider,
                "item/" + name + "/" + texture,
                DataGenHandle.MODEL);
    }

    public static boolean checkBlockTextureExist(ItemModelProvider provider, String name, String texture) {
        return checkFileExist(provider,
                "block/" + name + "/" + texture,
                DataGenHandle.TEXTURE);
    }

    public static boolean checkBlockModelExist(ItemModelProvider provider, String name, String texture) {
        return checkFileExist(provider,
                "block/" + name + "/" + texture,
                DataGenHandle.MODEL);
    }

    public static void addItemModelTask(Consumer<ItemModelProvider> task) {
        runOnDataGen(() -> () -> {
            itemModelTasks.add(task);
        });
    }

    public static void addBlockModelTask(Consumer<BlockModelProvider> task) {
        runOnDataGen(() -> () -> {
            blockModelTasks.add(task);
        });
    }

    public static void addBlockStateTask(Consumer<BlockStateProvider> task) {
        runOnDataGen(() -> () -> {
            blockStateTasks.add(task);
        });
    }

    public static void runOnDataGen(Supplier<DistExecutor.SafeRunnable> toRun) {
        if (FMLEnvironment.dist == Dist.CLIENT && Industrimania.isDataGen) {
            toRun.get().run();
        }
    }

    public static boolean checkTextureFileExist(ModelProvider<?> provider, String texturePath) {
        return provider.existingFileHelper.exists(new ResourceLocation(MOD_ID, texturePath), DataGenHandle.TEXTURE);
    }

    public static boolean checkItemTextureFileExist(ModelProvider<?> provider, String texturePath) {
        return checkTextureFileExist(provider, String.format("item/%s", texturePath));
    }

    public static boolean checkBlockTextureFileExist(ModelProvider<?> provider, String texturePath) {
        return checkTextureFileExist(provider, String.format("block/%s", texturePath));
    }

    public static ExistingModelFile existingModel(ModelProvider<?> provider, String path) {
        return provider.getExistingFile(provider.mcLoc(path));
    }

    public static ExistingModelFile modExistingModel(ModelProvider<?> provider, String path) {
        return provider.getExistingFile(provider.modLoc(path));
    }

    static UncheckedModelFile uncheckedModel(String path) {
        return new UncheckedModelFile(path);
    }


//    static ExistingModelFile ItemGeneratedModel(ModelProvider<?> provider) {
//        return existingModel(provider, "item/generated");
//    }
//
//    public static ExistingModelFile ItemHeldModel(ModelProvider<?> provider) {
//        return existingModel(provider, "item/handheld");
//    }
//
//    static UncheckedModelFile BlockBuiltinEntity(ModelProvider<?> provider) {
//        return new UncheckedModelFile("builtin/entity");
//    }
//
//    static ExistingModelFile BlockCubeAll(ModelProvider<?> provider) {
//        return existingModel(provider, "block/cube_all");
//    }
//
//    static ExistingModelFile BlockCube(ModelProvider<?> provider) {
//        return existingModel(provider, "block/cube");
//    }


    @SubscribeEvent
    public static void processDataGen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ItemModelProvider itemModelProvider = new ItemModelProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void registerModels() {
                itemModelTasks.forEach((task) -> task.accept(this));
            }
        };

        BlockModelProvider blockModelProvider = new BlockModelProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void registerModels() {
                blockModelTasks.forEach((task) -> task.accept(this));
            }
        };

        BlockStateProvider blockStateProvider = new BlockStateProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void registerStatesAndModels() {
                blockStateTasks.forEach((task) -> task.accept(this));
            }
        };

        BlockTagsProvider blockTagsProvider = new BlockTagsProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void addTags() {
                blockTagsTasks.forEach(task -> task.run((block, tag) -> tag(BlockTags.create(new ResourceLocation(tag))).add(block)));
            }
        };

        ItemTagsProvider itemTagsProvider = new ItemTagsProvider(generator, blockTagsProvider, MOD_ID, existingFileHelper) {
            @Override
            protected void addTags() {
                itemTagsTasks.forEach(task -> task.run((item, tag) -> tag(ItemTags.create(new ResourceLocation(tag))).add(item)));
            }
        };

        FluidTagsProvider fluidTagsProvider = new FluidTagsProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void addTags() {
                fluidTagsTasks.forEach(task -> task.run((fluid, tag) -> tag(FluidTags.create(new ResourceLocation(tag))).add(fluid)));
            }
        };

        // only use for generate template lang file, keys are lang and values are registryName's path or display name
        LanguageProvider languageProvider = new LanguageProvider(generator, MOD_ID, "template") {
            @Override
            protected void addTranslations() {

                List<Block> ores = AllOres.ORES.values().stream().map(it -> it.block().get()).toList();
                List<Item> oreChunks = AllOreChunks.ORE_CHUNKS.values().stream().map(RegistryObject::get).toList();

                RegisterHandle.ITEM_REGISTER.getEntries()
                        .stream().sequential()
                        .filter(item -> !(item.get() instanceof BlockItem) && !(oreChunks.contains(item.get())))
                        .forEach((item ->
                                this.addItem(item, Objects.requireNonNull(item.get().getRegistryName()).getPath())));
                RegisterHandle.BLOCK_REGISTER.getEntries()
                        .stream().sequential()
                        .filter(block -> !ores.contains(block.get()))
                        .forEach((block ->
                        this.addBlock(block, Objects.requireNonNull(Objects.requireNonNull(block.get().getRegistryName()).getPath()))));
                RegisterHandle.MOB_EFFECT_REGISTER.getEntries().forEach((effect) ->
                        this.addEffect(effect, effect.get().getDisplayName().getString()));
                RegisterHandle.ENCHANTMENT_REGISTER.getEntries().forEach((enchantment ->
                        this.addEnchantment(enchantment, Objects.requireNonNull(enchantment.get().getRegistryName()).getPath())));
                RegisterHandle.ENTITY_TYPE_REGISTER.getEntries().forEach((entityType) ->
                        this.addEntityType(entityType, Objects.requireNonNull(entityType.get().getRegistryName()).getPath()));
                AllTabs.tabs.forEach((tab)->this.add(((TranslatableComponent)(tab.getDisplayName())).getKey(), ""));
            }

        };

        itemModelPro.set(() -> itemModelProvider);
        blockModelPro.set(() -> blockModelProvider);
        blockStatePro.set(() -> blockStateProvider);
        blockTagsPro.set(() -> blockTagsProvider);
        itemTagsPro.set(() -> itemTagsProvider);
        fluidTagsPro.set(() -> fluidTagsProvider);


        generator.addProvider(itemModelProvider);
        generator.addProvider(blockModelProvider);
        generator.addProvider(blockStateProvider);
        generator.addProvider(blockTagsProvider);
        generator.addProvider(itemTagsProvider);
        generator.addProvider(fluidTagsProvider);
        generator.addProvider(languageProvider);
        ProcessingRecipeGen.registerAll(generator);
    }
}