package shagejack.industrimania.registries;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registries.block.AllBlocks;
import shagejack.industrimania.registries.block.grouped.AllOres;
import shagejack.industrimania.registries.item.AllItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AllTabs {

    public static final List<CreativeModeTab> tabs = new ArrayList<>();

    public static final CreativeModeTab tabMain = tab(Industrimania.MOD_ID, () -> () -> AllItems.omniMultimeter);

    public static final CreativeModeTab tabPrimal = tab(Industrimania.MOD_ID + "_primal", () -> () -> AllItems.mud);

    public static final CreativeModeTab tabStone = tab(Industrimania.MOD_ID + "_stone", () -> () -> AllItems.flintSaw);

    public static final CreativeModeTab tabMetallurgy = tab(Industrimania.MOD_ID + "_metallurgy", () -> () -> AllItems.ironIngot);

    public static final CreativeModeTab tabSteam = tab(Industrimania.MOD_ID + "_steam", () -> AllBlocks.mechanic_boiler::item);

    public static final CreativeModeTab tabBuilding = tab(Industrimania.MOD_ID + "_building", () -> AllBlocks.building_fine_clay::item);

    public static final CreativeModeTab tabMaterial = tab(Industrimania.MOD_ID + "_material", () -> () -> AllItems.clinker);

    public static final CreativeModeTab tabTool = tab(Industrimania.MOD_ID + "_tool", () -> () -> AllItems.bronzeSaw);

    public static final CreativeModeTab tabEquipment = tab(Industrimania.MOD_ID + "_equipment", () -> () -> AllItems.hazardProtectiveChestplate);

    public static final CreativeModeTab tabNature = tab(Industrimania.MOD_ID + "_nature", () -> AllBlocks.nature_lactuca_raddeana::item);

    public static final CreativeModeTab tabRock = tab(Industrimania.MOD_ID + "_rock", () -> AllBlocks.rock_rhyolite::item);

    public static final CreativeModeTab tabOre = tab(Industrimania.MOD_ID + "_ore", () -> () -> AllOres.ORES.get("rock_rhyolite_hematite_1").item());

    public static final CreativeModeTab tabMisc = tab(Industrimania.MOD_ID + "_misc", () -> () -> AllItems.woodenBarrelCover);

    static private CreativeModeTab tab(String name, Supplier<Supplier<RegistryObject<Item>>> itemStack) {
        var tab = new CreativeModeTab(name) {
            @Override
            public @NotNull ItemStack makeIcon() {
                return new ItemStack(itemStack.get().get().get());
            }
        };
        tabs.add(tab);
        return tab;
    }
}
