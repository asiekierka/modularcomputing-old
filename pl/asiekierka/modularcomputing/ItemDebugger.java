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

public class ItemDebugger extends Item implements IPeripheralCard {
    private Icon icon;

    public ItemDebugger(int id) {
        super(id);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("modularcomputing:debugger");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1) {
        return icon;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir) {
        icon = ir.registerIcon("modularcomputing:debugger");
    }
    public int getVendorID() { return 42270; }
    public int getDeviceID() { return 1; }
    public void init(ItemStack is) {}
    public int read8(CPUThread cpu, ItemStack stack, int position) {
        ModularComputing.logger.info("[Debugger] Tried to read @ " + position + "!");
        return 0;
    }
    public void write8(CPUThread cpu, ItemStack stack, int position, int val) {
        ModularComputing.logger.info("[Debugger] Tried to write "+val+" @ " + position + "!");
    }
}
