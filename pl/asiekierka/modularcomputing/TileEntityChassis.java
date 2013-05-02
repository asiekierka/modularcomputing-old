package pl.asiekierka.modularcomputing;

import java.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;

public class TileEntityChassis extends TileEntityInventory implements IInventory {
	public TileEntityChassis() {
		super(22, "modular.chassis");
	}
	public ItemStack getCPU() { return inv[1]; }
	public ItemStack getClock() { return inv[0]; }
	public ItemStack[] getMemory() { return Arrays.copyOfRange(inv,2,6); }
	public ItemStack[] getStorage() { return Arrays.copyOfRange(inv,6,14); }
	public ItemStack[] getCards() { return Arrays.copyOfRange(inv,14,22); }
	public ItemStack[] getIoDevices() { return Arrays.copyOfRange(inv,6,22); }
	public TileEntityMonitor findMonitor() {
            for(int[] coords : MCHelper.dir3) {
                TileEntity te = worldObj.getBlockTileEntity(xCoord+coords[0],yCoord+coords[1],zCoord+coords[2]);
                if(te instanceof TileEntityMonitor) return (TileEntityMonitor)te;
            }
            return null;
        }
}
