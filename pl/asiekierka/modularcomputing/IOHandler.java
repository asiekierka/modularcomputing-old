package pl.asiekierka.modularcomputing;

import net.minecraft.item.*;

public class IOHandler implements IPeripheralMemory {

    private ItemStack[] ioDevices;

    public IOHandler(TileEntityChassis chassis) {
        ioDevices = chassis.getIoDevices();
        for(int i=0;i<16;i++) {
            if(ioDevices[i] == null) continue;
            else if(ioDevices[i].stackSize == 0 || ioDevices[i].getItem() == null || !(ioDevices[i].getItem() instanceof IPeripheralCard))
                ioDevices[i] = null;
        }
    }
    public void init(ItemStack is) {
        for(int i=0;i<16;i++)
            if(ioDevices[i] != null) ((IPeripheralCard)ioDevices[i].getItem()).init(ioDevices[i]);
    }
    public int getSize(ItemStack is) { return 4096; }
    public int read8(CPUThread cpu, ItemStack is, int pos) {
        int devPos = pos >> 8;
        int position = pos & 255;
        ModularComputing.debug("[IO] Received read, dev "+devPos+"@"+Integer.toHexString(position));
        ItemStack stack = ioDevices[devPos];
        if(stack == null) return 0;
        Item item = stack.getItem();
        if(!(item instanceof IPeripheralCard)) return 0;
        IPeripheralCard dev = (IPeripheralCard)item;
        if(position == 0 || position == 1)
            return (dev.getVendorID() >> (position<<3)) & 255;
        else if(position == 2 || position == 3)
            return (dev.getDeviceID() >> ((position-2)<<3)) & 255;
        else return dev.read8(cpu, stack, position);
    }
    public void write8(CPUThread cpu, ItemStack is, int pos, int val) {
        int devPos = pos >> 8;
        int position = pos & 255;
        if(position < 4) return; // check for ID position write attempts
        ModularComputing.debug("[IO] Received write, dev "+devPos+"@"+Integer.toHexString(position)+", value "+val);
        ItemStack stack = ioDevices[devPos];
        if(stack == null) return;
        Item item = stack.getItem();
        if(!(item instanceof IPeripheralCard)) return;
        IPeripheralCard dev = (IPeripheralCard)item;
        dev.write8(cpu, stack, position, val&0xFF);
    }
}

