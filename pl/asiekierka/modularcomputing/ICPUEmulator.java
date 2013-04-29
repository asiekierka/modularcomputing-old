package pl.asiekierka.modularcomputing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface ICPUEmulator {
  public String getName();
  public void tick(CPUThread thread);
}
