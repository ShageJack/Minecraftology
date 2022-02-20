package shagejack.industrimania.content.primalAge.block.carcass.pig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.primalAge.block.carcass.AbstractCarcass;

public class PigCarcass extends AbstractCarcass {

    public PigCarcass(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack onSkinned(Level level, BlockPos pos) {
        return ItemStack.EMPTY;
    }

}
