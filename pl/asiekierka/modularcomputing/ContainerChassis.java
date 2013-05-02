package pl.asiekierka.modularcomputing;

import net.minecraft.util.*;
import net.minecraft.block.*;
import net.minecraft.world.World;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;

public class ContainerChassis extends ContainerInventory {
	protected TileEntityChassis tileEntity;

	public ContainerChassis(InventoryPlayer inventoryPlayer, TileEntityChassis te) {
		super(22);
		tileEntity = te;
		addSlotToContainer(new SlotModular(IPeripheralClock.class, tileEntity, 0, 16, 16)); // Clock
		addSlotToContainer(new SlotModular(IPeripheralCPU.class, tileEntity, 1, 54, 16)); // CPU
		int MAX_LENGTH = 5;
		for(int j = 0; j < 4; j++) {
			addSlotToContainer(new SlotModular(IPeripheralMemory.class, tileEntity, 2+j, 8, 68+(j*18))); // Memory
			addSlotToContainer(new SlotModular(IPeripheralCard.class, tileEntity, 6+j, 40, 68+(j*18))); // Storage 1
			addSlotToContainer(new SlotModular(IPeripheralCard.class, tileEntity, 10+j, 58, 68+(j*18))); // Storage 2
			addSlotToContainer(new SlotModular(IPeripheralCard.class, tileEntity, 14+j, 90, 68+(j*18))); // Cards 1
			addSlotToContainer(new SlotModular(IPeripheralCard.class, tileEntity, 18+j, 108, 68+(j*18))); // Cards 2
		}
		bindPlayerInventory(inventoryPlayer, 8, 148);
	}
        @Override
        public boolean canInteractWith(EntityPlayer player) {
                return tileEntity.isUseableByPlayer(player);
        }
}
