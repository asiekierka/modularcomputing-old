package pl.asiekierka.modularcomputing;

import net.minecraft.item.*;

public class IOHandler implements IPeripheralMemory {

    private ItemStack[] ioDevices;
    private CPUThread cpu;

    public IOHandler(TileEntityChassis chassis, CPUThread thread) {
        ioDevices = chassis.getIoDevices(); cpu = thread;
        for(int i=0;i<16;i++) {
            if(ioDevices[i] == null || ioDevices[i].stackSize == 0 || (ioDevices[i].getItem() instanceof IPeripheralCard))
                ioDevices[i] = null;
        }
    }
    public void init(ItemStack is) { }
    public int getSize(ItemStack is) { return 4096; }
    public int read8(ItemStack is, int pos) {
        int devPos = pos >> 8;
        int position = pos & 255;
        if(ioDevices[devPos] == null) return 0;
        ItemStack stack = ioDevices[devPos];
        IPeripheralCard dev = (IPeripheralCard)is.getItem();
        if(position == 0 || position == 1)
            return (dev.getVendorID() >> (position<<3)) & 255;
        else if(position == 2 || position == 3)
            return (dev.getDeviceID() >> ((position-2)<<3)) & 255;
        else return dev.read8(stack, cpu, position);
    }
    public void write8(ItemStack is, int pos, int val) {
        int devPos = pos >> 8;
        int position = pos & 255;
        if(ioDevices[devPos] == null || position < 4) return; // check for ID slot
        ItemStack stack = ioDevices[devPos];
        IPeripheralCard dev = (IPeripheralCard)is.getItem();
        dev.write8(stack, cpu, position, val&0xFF);
    }
}

