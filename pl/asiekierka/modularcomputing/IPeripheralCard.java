package pl.asiekierka.modularcomputing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IPeripheralCard {
  public int getVendorID();
  public int getDeviceID();
  public int read8(CPUThread cpu, ItemStack stack, int position);
  public void write8(CPUThread cpu, ItemStack stack, int position, int data);
  public void init(ItemStack stack);
}
