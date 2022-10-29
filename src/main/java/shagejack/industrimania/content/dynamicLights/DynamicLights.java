package shagejack.industrimania.content.dynamicLights;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import shagejack.industrimania.foundation.utility.Pair;
import shagejack.industrimania.registries.block.AllBlocks;
import shagejack.industrimania.registries.item.AllItems;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DynamicLights {

    private static final ConcurrentHashMap<Level, ConcurrentLinkedQueue<LightReference>> levelLightsMap = new ConcurrentHashMap<>();
    private static int REMOVAL_IDLE = 6000;

    public static boolean isLit(Level level, BlockPos pos) {
        ConcurrentLinkedQueue<LightReference> levelLights = levelLightsMap.get(level);
        if (levelLights != null) {
            return levelLights.stream().anyMatch(light -> light.pos().equals(pos));
        }
        return false;
    }

    public static void addLight(Level level, BlockPos pos, int lightLevel, Entity entity) {
        ConcurrentLinkedQueue<LightReference> levelLights = levelLightsMap.get(level);

        if (levelLights == null) {
            levelLights = new ConcurrentLinkedQueue<>();
        }

        if (levelLights.stream().anyMatch(light -> light.pos().equals(pos) && light.lightLevel() >= lightLevel)) {
            return;
        }

        if ((level.getBlockState(pos).isAir() && !level.getBlockState(pos).is(AllBlocks.mechanic_fake_air_light.block().get())) || (level.getBlockState(pos).is(AllBlocks.mechanic_fake_air_light.block().get()) && level.getBlockState(pos).getValue(BlockStateProperties.LEVEL) < lightLevel)) {
            level.setBlock(pos, AllBlocks.mechanic_fake_air_light.block().get().defaultBlockState().setValue(BlockStateProperties.LEVEL, lightLevel), 3);
            levelLights.add(new LightReference(pos, lightLevel, entity));
            levelLightsMap.put(level, levelLights);
        }
    }

    public static void removeLight(Level level, BlockPos pos) {
        ConcurrentLinkedQueue<LightReference> levelLights = levelLightsMap.get(level);

        if (levelLights == null) {
            return;
        }

        levelLights.stream().filter(light -> light.pos().equals(pos)).forEach(light -> {
            if (level.getBlockState(light.pos()).is(AllBlocks.mechanic_fake_air_light.block().get()))
                level.removeBlock(light.pos(), false);
        });

        levelLights.removeIf(light -> light.pos().equals(pos));

        levelLightsMap.put(level, levelLights);
    }

    @SubscribeEvent
    public static void serverWorldTick(TickEvent.WorldTickEvent event) {

        if (event.side != LogicalSide.SERVER) {
            return;
        }

        if (REMOVAL_IDLE > 0) {
            REMOVAL_IDLE--;
            return;
        }

        ConcurrentLinkedQueue<LightReference> levelLights = levelLightsMap.get(event.world);
        if (levelLights != null) {
            Iterator<LightReference> iter = levelLights.iterator();
            while (iter.hasNext()) {
                LightReference light = iter.next();
                BlockPos pos = light.pos();
                if (light.entity().isRemoved() && !light.entity().getOnPos().above().equals(pos) && !light.entity().getOnPos().above().above().equals(pos)) {
                    iter.remove();

                    if (event.world.getBlockState(pos).is(AllBlocks.mechanic_fake_air_light.block().get()))
                        event.world.removeBlock(pos, false);

                    levelLights.removeIf(lightReference -> lightReference.pos().equals(pos));
                    levelLightsMap.put(event.world, levelLights);
                }
            }
        }

        REMOVAL_IDLE = 6000;

    }

    //Pair: if it's necessary to check the item is light up or not & light level
    public static Map<Item, Pair<Boolean, Integer>> dynamicLightsItems = new HashMap<>();

    public static boolean registerItem(Item item, Boolean needCheck, int lightLevel) {
        if (!dynamicLightsItems.containsKey(item)) {
            dynamicLightsItems.put(item, Pair.of(needCheck, lightLevel));
            return true;
        } else {
            return false;
        }
    }

    static {
        registerItem(AllItems.handOilLamp.get(), true, 7);
    }

    public static int getItemLightLevel(Item item) {
        return dynamicLightsItems.containsKey(item) ? dynamicLightsItems.get(item).getSecond() : 0;
    }

    public static int getItemLightLevel(ItemStack stack) {
        return getItemLightLevel(stack.getItem());
    }

    public static boolean isDynamicLightsItem(Item item) {
        return dynamicLightsItems.containsKey(item);
    }

    public static boolean isDynamicLightsItem(ItemStack stack) {
        return isDynamicLightsItem(stack.getItem());
    }

    public static boolean needCheckLight(Item item) {
        return dynamicLightsItems.containsKey(item) ? dynamicLightsItems.get(item).getFirst() : true;
    }

    public static boolean needCheckLight(ItemStack stack) {
        return needCheckLight(stack.getItem());
    }

}
