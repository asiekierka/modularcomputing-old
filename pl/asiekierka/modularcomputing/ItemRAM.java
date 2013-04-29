package pl.asiekierka.modularcomputing;

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

public class ItemRAM extends Item implements IPeripheralMemory {
    private static final int minSize = 2;
    private static final int maxSize = 128;
    private Icon icon;

    public ItemRAM(int id) {
        super(id);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("modularcomputing:ram");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1) {
        return icon;
    }
    @Override
    public String getItemDisplayName(ItemStack stack) {
        int size = getSize(stack);
        return StatCollector.translateToLocal("modularcomputing:ram") + " (" + (size/1024) + "KB)";
    }
    @Override
    public String getLocalizedName(ItemStack stack) { return getItemDisplayName(stack); }
    @Override
    public boolean getShareTag() { return false; }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir) {
        icon = ir.registerIcon("modularcomputing:ram");
    }
    public int[] getMemArray(ItemStack stack) {
        NBTTagCompound tc = MCHelper.getTagCompound(stack);
        if(!tc.hasKey("memory"))
            init(stack);
        return tc.getIntArray("memory");
    }
    public int getSize(ItemStack stack) {
       return stack.getItemDamage() * 1024;
    }
    public int read8(ItemStack stack, int position) {
       int size = getSize(stack);
       if(position < 0 || position >= (size*1024)) return -1;
       int[] array = getMemArray(stack);
       if(array == null) return -1;
       return array[position];
    }
    public void write8(ItemStack stack, int position, int val) {
        int size = getSize(stack);
        if(position < 0 || position >= (size*1024)) return;
        int[] array = getMemArray(stack);
        NBTTagCompound tc = MCHelper.getTagCompound(stack);
        array[position] = val;
        tc.setIntArray("memory",array);
        stack.setTagCompound(tc);
    }
    public void init(ItemStack stack) {
        NBTTagCompound tc = MCHelper.getTagCompound(stack);
        tc.setIntArray("memory", new int[getSize(stack)]);
        stack.setTagCompound(tc);
    }
   // Creative codes
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs ct, List list) {
        for(int s = minSize; s <= maxSize; s*=2)
            list.add(new ItemStack(id, 1, s));
    }
}
