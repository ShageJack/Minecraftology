/*
package shagejack.minecraftology.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMetalFurnace extends Container {
    private IItemHandler upItems;
    private IItemHandler downItems;
    //protected TileEntityMetalFurnace tile;传入一个tileentity
    protected int burnTime = 0;
    public ContainerMetalFurnace(EntityPlayer player, TileEntity tileEntity){
        super();
        this.upItems = tileEntity.getCapability(CapabilityMetalFurnace.METAL_FURNACE_UP_ITEMS, EnumFacing.UP);
        this.downItems = tileEntity.getCapability(CapabilityMetalFurnace.METAL_FURNACE_DOWN_ITEMS, EnumFacing.DOWN);
        this.addSlotToContainer(new SlotItemHandler(this.upItems,0,56,30));
        this.addSlotToContainer(new SlotItemHandler(this.downItems,0,110,30){
            @Override
            public boolean isItemValid(){
                return false;
            }
        });
        for(int i=0;i<2;++i){
            for(int j=0;j<9;++j){
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 74 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 132));
        }
        this.tileEntity = (TileEntityMetalFurnace)tileEntity;
    }
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn,int index){
        Slot slot = inventorySlots.get(index);
        if(slot == null || !slot.getHasStack()){
            return null;
        }
    ItemStack newStack = slot.getStack(), oldStack = newStack.copy();

    boolean isMerged = false;

        if (index == 0 || index == 1)
    {
        isMerged = mergeItemStack(newStack, 2, 38, true);
    }
        else if (index >= 2 && index < 29)
    {
        isMerged = mergeItemStack(newStack, 0, 1, false) || mergeItemStack(newStack, 29, 38, false);
    }
        else if (index >= 29 && index < 38)
    {
        isMerged = mergeItemStack(newStack, 0, 1, false) || mergeItemStack(newStack, 2, 29, false);
    }

        if (!isMerged)
    {
        return null;
    }

        if (newStack.getMaxStackSize() == 0)
    {
        slot.putStack(null);
    }
        else
    {
        slot.onSlotChanged();
    }

        slot.onPickupFromSlot(playerIn, newStack);

        return oldStack;
    }
    @Override
    public void detectAndSendChanges(){
        super.detectAndSendChanges();
        this.burnTime = tileEntity.getBurnTime();
        for(InventoryCrafting i : this.crafters){
            i.sendProgressBarUpdata(this,0,this.burnTime);
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void updataProgressBar(int id,int data){
        super.updateProgressBar(id,data);
        switch (id){
            case 0:
                this.burnTime=data;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return playerIn.getDistanceSq(this.tileEntity.getPos()) <= 64;
    }
}
*/
