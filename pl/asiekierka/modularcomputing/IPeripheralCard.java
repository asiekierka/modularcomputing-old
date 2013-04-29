package pl.asiekierka.modularcomputing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IPeripheralCard {
  public int getVendorID();
  public int getDeviceID();
  public int read8(ItemStack stack, CPUThread thread, int position);
  public void write8(ItemStack stack, CPUThread thread, int position, int data);
}
