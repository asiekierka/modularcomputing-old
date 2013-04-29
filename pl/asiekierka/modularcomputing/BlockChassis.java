package pl.asiekierka.modularcomputing;

import net.minecraft.util.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.block.material.Material;
import net.minecraft.block.*;
import net.minecraft.creativetab.*;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;

public class BlockChassis extends BlockContainer {
	Icon texFrontOff, texFrontOn, texGeneric;

	public BlockChassis(int id) {
		super(id, Material.iron);
		setHardness(3.0F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(!world.isRemote || player.isSneaking()) {
			player.openGui(ModularComputing.instance, 0, world, x, y, z);
		}
		return true;
	}
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
                super.onBlockAdded(world,x,y,z);
		MCHelper.setDefaultDirection(world,x,y,z);
	}
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving el, ItemStack is) {
		world.setBlockMetadataWithNotify(x,y,z, MCHelper.placedDirection(el), 2);
	}
        @Override
        public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
                MCHelper.dropItems(world, x, y, z);
                super.breakBlock(world, x, y, z, par5, par6);
        }
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityChassis();
	}

	public boolean isOpaqueCube() { return false; }
	public boolean renderAsNormalBlock() { return false; }

	@Override
	public Icon getIcon(int side, int metadata) {
		if (side == metadata || (metadata == 0 && side == 3)) {
			return texFrontOff;
		}

		return texGeneric;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		texFrontOn = iconRegister.registerIcon("modularcomputing:cpu_front_on");
		texFrontOff = iconRegister.registerIcon("modularcomputing:cpu_front_off");
		texGeneric = iconRegister.registerIcon("modularcomputing:generic_face");
	}
}
