package pl.asiekierka.modularcomputing;

import java.io.*;
import java.nio.*;
import java.util.*;
import net.minecraft.item.*;

public class CPUThread implements Runnable {
    private int tickClockSpeed;
    public MemoryHandler mem;
    private ICPUEmulator cpu;
    private TileEntityMonitor monitor;
    private ArrayList<int[]> packets;

    public CPUThread(TileEntityChassis chassis) {
        this.cpu = null;
        this.packets = new ArrayList<int[]>();
        ItemStack cpuStack = chassis.getCPU();
        if(cpuStack != null && cpuStack.getItem() instanceof ItemCPU) {
            this.cpu = ((ItemCPU)cpuStack.getItem()).getCPUEmulator();
            this.cpu.setPC(0x8000);
        }
        this.monitor = chassis.findMonitor();
        if(chassis.getClock() == null || !(chassis.getClock().getItem() instanceof IPeripheralClock)) {
            ModularComputing.logger.info("ERROR: No clock found!"); return;
        }
        // WELCOME, CITIZEN, TO THE WORLD OF PASSING ITEM STACKS!
        int clockSpeed = ((IPeripheralClock)chassis.getClock().getItem()).getClockSpeed(chassis.getClock());
        this.tickClockSpeed = (int)Math.floor(clockSpeed/20);
        if(this.tickClockSpeed < 50) { ModularComputing.logger.info("WARNING: Clock speed under 1KHz. This may end insanely badly. Also, it shouldn't even happen."); }
        this.mem = new MemoryHandler(this);
        mem.registerHandler(0xF000, new IOHandler(chassis));
        int currPos = 0;
        // Register memory
        for(ItemStack is : chassis.getMemory()) { // NON-ROM STUFF
            if(is == null || !(is.getItem() instanceof IPeripheralMemory) || (is.getItem() instanceof ItemROM)) continue;
            IPeripheralMemory ipm = (IPeripheralMemory)is.getItem();
            // TODO: Add code to move ROM to a different area
            if(currPos+ipm.getSize(is) >= 61440) {
                ModularComputing.logger.info("FAILWHALE: Computer memory size over memory amount!");
                break;
            }
            mem.registerHandler(currPos, is, ipm);
            currPos += ipm.getSize(is);
        }
        for(ItemStack is : chassis.getMemory()) { // SPECIAL LOOP FOR ROMS
            if(is == null || !(is.getItem() instanceof ItemROM)) continue;
            ItemROM irom = (ItemROM)is.getItem();
            mem.registerHandler(irom.getStartPosition(is), is, (IPeripheralMemory)irom);
        }
    }
    public TileEntityMonitor getMonitor() { return monitor; }
    public void run() {
        if(this.monitor != null) this.monitor.setPower(true);
        try {
          int i = 0;
          int err = 0;
          while(i<1000 && err>(-65536)) {
            if(cpu != null)
                err = cpu.tick(this, tickClockSpeed);
            Thread.sleep(50);
            i++;
          }
       } catch(Exception e){ e.printStackTrace(); }
    }
}

