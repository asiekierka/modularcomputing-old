package pl.asiekierka.modularcomputing;

import java.util.*;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.*;

public class ItemClock extends Item implements IPeripheralClock {
    private int minClock, maxClock;
    private String iconName, clockName;
    private Icon icon;
    private int[] creativeSpeeds = { 10, 25, 50, 100, 250, 500, 1000, 2500, 5000, 10000, 25000 };

    public ItemClock(int id, int min, int max, String icon, String name) {
        super(id);
        this.setHasSubtypes(true);
	minClock = min; maxClock = max; iconName = icon; clockName = name;
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setMaxStackSize(1);
    }
    public ItemClock(int id, int max, String icon, String name) {
        this(id,10,max,icon,name);
    }
    public ItemClock(int id, int min, int max, String name) {
        this(id,min,max,name,name);
    }
    public ItemClock(int id, int max, String name) {
        this(id,10,max,name,name);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1) {
        return icon;
    }
    @Override
    public String getItemDisplayName(ItemStack stack) {
        int speed = minClock + stack.getItemDamage();
        return StatCollector.translateToLocal(clockName) + " (" + speed + "kHz)";
    }
    @Override
    public String getLocalizedName(ItemStack stack) { return getItemDisplayName(stack); }
    @Override
    public String getUnlocalizedName() { return clockName; }
    @Override
    public String getUnlocalizedName(ItemStack stack) { return clockName; }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir) {
        icon = ir.registerIcon(iconName);
    }
    public int getClockSpeed(ItemStack stack) {
        return (minClock + stack.getItemDamage()) * 1000;
    }
    public boolean setClockSpeed(ItemStack stack, int speed) {
        int oldSpeed = getClockSpeed(stack);
        int speedDiv = (int)Math.floor(speed/1000);
        if(speedDiv < minClock || speedDiv > maxClock) return false; // Wrong clock speed!
        if(speed > oldSpeed) speedDiv++;
        int speedDamage = speedDiv - minClock;
        stack.setItemDamage(speedDamage);
        return true;
    }
   // Creative codes
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs ct, List list) {
        for(int pos = 0; pos < creativeSpeeds.length; pos++) {
            int speed = creativeSpeeds[pos];
            if(speed < minClock || speed > maxClock) continue;
            list.add(new ItemStack(id, 1, (speed - minClock)));
        }
    }
}
