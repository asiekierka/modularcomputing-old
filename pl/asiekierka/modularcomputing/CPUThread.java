package pl.asiekierka.modularcomputing;

import net.minecraft.item.*;

public class CPUThread implements Runnable {
    private int tickClockSpeed;
    private MemoryHandler mem;
    private ICPUEmulator cpu;
    public CPUThread(ItemCPU cpu, TileEntityChassis chassis) {
        this.cpu = cpu.getCPUEmulator();
        if(chassis.getClock() == null || !(chassis.getClock().getItem() instanceof IPeripheralClock)) {
            ModularComputing.logger.info("ERROR: No clock found!"); return;
        }
        // WELCOME, CITIZEN, TO THE WORLD OF PASSING ITEM STACKS!
        int clockSpeed = ((IPeripheralClock)chassis.getClock().getItem()).getClockSpeed(chassis.getClock());
        this.tickClockSpeed = (int)Math.floor(clockSpeed/20);
        if(this.tickClockSpeed < 50) { ModularComputing.logger.info("WARNING: Clock speed under 1KHz. This may end insanely badly. Also, it shouldn't even happen."); }
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

