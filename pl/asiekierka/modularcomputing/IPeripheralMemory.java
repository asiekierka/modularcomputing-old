package pl.asiekierka.modularcomputing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IPeripheralMemory {
  // I use damage values and stuff, therefore passing ItemStacks like crazy is a bit of a necessity ATM.
  public void init(ItemStack stack);
  public int getSize(ItemStack stack);
  public int read8(CPUThread cpu, ItemStack stack, int position);
  public void write8(CPUThread cpu, ItemStack stack, int position, int data);
}
