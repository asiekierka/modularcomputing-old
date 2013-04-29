package pl.asiekierka.modularcomputing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IPeripheralClock {
  public int getClockSpeed(ItemStack stack);
  public boolean setClockSpeed(ItemStack stack, int speed);
}
