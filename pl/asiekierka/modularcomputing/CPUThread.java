package pl.asiekierka.modularcomputing;

import net.minecraft.item.*;

public class CPUThread implements Runnable {
    private int tickClockSpeed;
    private ItemCPU cpu;
    private MemoryHandler mem;
    public CPUThread(ItemCPU cpu, int clockSpeed, TileEntityChassis chassis) {
        this.cpu = cpu;
        this.tickClockSpeed = (int)Math.floor(clockSpeed/20);
        this.mem = new MemoryHandler();
        mem.registerHandler(61440, new IOHandler(chassis, this));
        int currPos = 0;
        // Register memory
        for(ItemStack is : chassis.getMemory()) {
            if(is == null || !(is.getItem() instanceof IPeripheralMemory)) continue;
            IPeripheralMemory ipm = (IPeripheralMemory)is.getItem();
            // TODO: Add code to move ROM to a different area
            if(currPos+ipm.getSize(is) >= 61440) {
                ModularComputing.logger.info("FAILWHALE: Computer memory size over memory amount!");
                break;
            }
            mem.registerHandler(currPos, is, ipm);
            currPos += ipm.getSize(is);
        }
    }
    public void run() {
        ModularComputing.logger.info("Testing write");
        for(int i=0;i<256;i++)
            mem.write8(i,i);
        ModularComputing.logger.info("Testing read");
        int ok = 0;
        for(int i=0;i<256;i++) {
            if(mem.read8(i) == i) ok++;
        }
        ModularComputing.logger.info(ok + "/256 write-reads OK!");
    }
}

