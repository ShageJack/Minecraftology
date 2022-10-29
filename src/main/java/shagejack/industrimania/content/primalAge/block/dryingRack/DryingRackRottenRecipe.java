package shagejack.industrimania.content.primalAge.block.dryingRack;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import shagejack.industrimania.content.misc.processing.ProcessingRecipe;
import shagejack.industrimania.content.misc.processing.ProcessingRecipeBuilder.ProcessingRecipeParams;
import shagejack.industrimania.registries.AllRecipeTypes;

public class DryingRackRottenRecipe extends ProcessingRecipe<RecipeWrapper> {

    public DryingRackRottenRecipe(ProcessingRecipeParams params) {
        super(AllRecipeTypes.DRYING_RACK_ROTTEN, params);
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
        return 4;
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }
}
