package pl.asiekierka.modularcomputing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface ICPUEmulator {
  public int getPC();
  public void setPC(int pc);
  public int tick(CPUThread thread, int cycles);
}
