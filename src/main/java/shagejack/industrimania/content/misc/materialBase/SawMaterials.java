package shagejack.industrimania.content.misc.materialBase;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum SawMaterials implements SawMaterial {
    BONE("iron", 15, 9, 0.5F, () -> Ingredient.EMPTY),
    FLINT("iron", 15, 9, 1.0F, () -> Ingredient.EMPTY),
    IRON("iron", 15, 9, 2.0F, () -> Ingredient.of(Items.IRON_INGOT)),
    GOLD("gold", 7, 25, 1.0F, () -> Ingredient.of(Items.GOLD_INGOT)),
    DIAMOND("diamond", 33, 10, 4.0F, () -> Ingredient.of(Items.DIAMOND)),
    NETHERITE("netherite", 37, 15, 5.0F, () -> Ingredient.of(Items.NETHERITE_INGOT));

    private final String name;
    private final int durability;
    private final int enchantmentValue;
    private final float sharpness;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    SawMaterials(String name, int durability, int enchantmentValue, float sharpness, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durability = durability;
        this.enchantmentValue = enchantmentValue;
        this.sharpness = sharpness;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getSharpness() {
        return this.sharpness;
    }

}
