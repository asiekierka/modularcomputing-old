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

public class ItemCPU extends Item implements IPeripheralCPU {
    private Icon icon;

    public ItemCPU(int id) {
        super(id);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setMaxStackSize(1);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1) {
        return icon;
    }
    @Override
    public boolean getShareTag() { return false; }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir) {
        icon = ir.registerIcon("modularcomputing:cpu");
    }
    public ICPUEmulator getCPUEmulator() { return new CPU65c02(); }
}
