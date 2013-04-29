package pl.asiekierka.modularcomputing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IPeripheralCPU {
  public ICPUEmulator getCPUEmulator();
}
