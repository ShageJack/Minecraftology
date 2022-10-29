package shagejack.industrimania.content.primalAge.block.dryingRack;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import shagejack.industrimania.content.contraptions.processing.ProcessingRecipe;
import shagejack.industrimania.content.contraptions.processing.ProcessingRecipeBuilder.ProcessingRecipeParams;
import shagejack.industrimania.registries.AllRecipeTypes;

public class DryingRackRecipe extends ProcessingRecipe<RecipeWrapper> {

    public DryingRackRecipe(ProcessingRecipeParams params) {
        super(AllRecipeTypes.DRYING_RACK, params);
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }

    @Override
    protected int getMaxOutputCount() {
        return 1;
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }
}
