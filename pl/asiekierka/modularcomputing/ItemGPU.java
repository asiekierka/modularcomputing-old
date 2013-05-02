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

public class ItemGPU extends Item implements IPeripheralCard {
    private Icon icon;

    public ItemGPU(int id) {
        super(id);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("modularcomputing:gpu");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1) {
        return icon;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir) {
        icon = ir.registerIcon("modularcomputing:gpu");
    }
    public int getVendorID() { return 42270; }
    public int getDeviceID() { return 2; }
    @Override
    public boolean getShareTag() { return false; }
    public int[] getMemArray(ItemStack stack) {
        NBTTagCompound tc = MCHelper.getTagCompound(stack);
        if(!tc.hasKey("memory"))
            init(stack);
        return tc.getIntArray("memory");
    }
    public int read8(CPUThread cpu, ItemStack stack, int position) {
       if(position < 0 || position >= 128) return -1;
       int[] array = getMemArray(stack);
       if(array == null) return -1;
       return array[position];
    }
    public void write8(CPUThread cpu, ItemStack stack, int position, int val) {
        if(position < 0 || position >= 128) return;
        int[] array = getMemArray(stack);
        NBTTagCompound tc = MCHelper.getTagCompound(stack);
        array[position] = val;
        tc.setIntArray("memory",updateRender(cpu,array));
        stack.setTagCompound(tc);
    }
    private int[] updateRender(CPUThread cpu, int[] mem) {
        TileEntityMonitor monitor = cpu.getMonitor();
        if(monitor == null) { mem[4] = 0; return mem; }
        if(mem[4] != 0) { // Command area
            switch(mem[4]) {
                case 1: // Set pixel (X,Y,col)
                    monitor.drawPixel(mem[5],mem[6],mem[7]);
                    break;
                case 2: // Draw char (cX,cY,chr,col)
                    monitor.drawChar(mem[5]*8,mem[6]*8,mem[8],mem[7]);
                    break;
                case 3: // Draw char (X,Y,chr,col)
                    monitor.drawChar(mem[5],mem[6],mem[8],mem[7]);
                    break;
                case 4: // Scroll up (amount)
                    monitor.scrollUp(mem[9]);
                    break;
                default:
                    break;
            }
            mem[4] = 0;
        }
        return mem;
    }
    public void init(ItemStack stack) {
        NBTTagCompound tc = MCHelper.getTagCompound(stack);
        tc.setIntArray("memory", new int[128]);
        stack.setTagCompound(tc);
    }
}
