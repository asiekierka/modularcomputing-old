package pl.asiekierka.modularcomputing;

import java.io.*;
import java.util.*;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.*;

public class ItemROM extends Item implements IPeripheralMemory {
    private Icon icon;
    private ArrayList<int[]> roms;
    private ArrayList<String> romNames;
    private ArrayList<Integer> romSPs;
    public ItemROM(int id, int defaultSize) {
        super(id);
        roms = new ArrayList<int[]>(defaultSize);
        romNames = new ArrayList<String>(defaultSize);
        romSPs = new ArrayList<Integer>(defaultSize);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("modularcomputing:rom");
    }
    public void registerROM(String filename, String name, int pos) {
        ModularComputing.debug("Registering ROM " + name + " ("+filename+")");
        int[] rom = MCHelper.fileToIntArray(filename);
        if(rom==null) return;
        roms.add(rom);
        romNames.add(name);
        romSPs.add(pos);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1) {
        return icon;
    }
    @Override
    public String getItemDisplayName(ItemStack stack) {
        return StatCollector.translateToLocal("modularcomputing:rom") + " (" + romNames.get(stack.getItemDamage()) + ")";
    }
    @Override
    public String getLocalizedName(ItemStack stack) { return getItemDisplayName(stack); }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir) {
        icon = ir.registerIcon("modularcomputing:rom");
    }
    public int getStartPosition(ItemStack stack) {
        return romSPs.get(stack.getItemDamage());
    }
    public int[] getMemArray(ItemStack stack) {
        return roms.get(stack.getItemDamage());
    }
    public int getSize(ItemStack stack) {
       return roms.get(stack.getItemDamage()).length;
    }
    public int read8(CPUThread cpu, ItemStack stack, int position) {
       if(position < 0 || position >= getSize(stack)) return 0;
       int res = roms.get(stack.getItemDamage())[position];
       return res;
    }
    public void write8(CPUThread cpu, ItemStack stack, int position, int val) {
       if(ModularComputing.ROM_WRITES) {
         if(position < 0 || position >= getSize(stack)) return;
         roms.get(stack.getItemDamage())[position] = val&0xFF;
       }
    }
    public void init(ItemStack stack) { }
   // Creative codes
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs ct, List list) {
        for(int s = 0; s < roms.size(); s++)
            list.add(new ItemStack(id, 1, s));
    }
}
