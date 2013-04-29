package pl.asiekierka.modularcomputing;

import java.util.*;
import java.util.logging.Logger;

import net.minecraftforge.common.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.registry.*;

@Mod(modid = ModularComputing.ID, version = ModularComputing.VERSION)
@NetworkMod(channels = { ModularComputing.ID }, packetHandler = NetworkHandler.class, clientSideRequired = true)
public class ModularComputing {
        public static final boolean DEBUG = true;
	public static final String ID = "ModularComputing";
	public static final String VERSION = "0.1";
	public static BlockChassis blockChassis;
	public static BlockMonitor blockMonitor;
	public static ItemClock itemRedstoneClock, itemQuartzClock;
	public static ItemRAM itemRAM;

	@Instance(ID)
	public static ModularComputing instance;

	@SidedProxy(clientSide = "pl.asiekierka.modularcomputing.ClientProxy", serverSide = "pl.asiekierka.modularcomputing.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger;
        public static void debug(String string) {
            if(DEBUG) logger.info(string);
        }
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		logger = Logger.getLogger(ID);
		logger.setParent(FMLLog.getLogger());
	}

	@Init
	public void init(FMLInitializationEvent event) {
		blockChassis = new BlockChassis(501);
		blockMonitor = new BlockMonitor(502);
		itemRedstoneClock = new ItemClock(25000, 10, 100, "modularcomputing:redstoneclock");
		itemRedstoneClock = new ItemClock(25001, 100, 1000, "modularcomputing:quartzclock");
		itemRAM = new ItemRAM(25002);
		GameRegistry.registerBlock(blockChassis, ItemBlock.class, "blockChassis");
		GameRegistry.registerTileEntity(TileEntityChassis.class, "containerChassis");

		GameRegistry.registerBlock(blockMonitor, ItemBlock.class, "blockMonitor");
		GameRegistry.registerTileEntity(TileEntityMonitor.class, "entityMonitor");
		LanguageRegistry.addName(blockChassis, "Computer");
		LanguageRegistry.addName(blockMonitor, "Monitor");

		// Create clocks
		LanguageRegistry.instance().addStringLocalization("modularcomputing:redstoneclock", "Redstone Clock");
		LanguageRegistry.instance().addStringLocalization("modularcomputing:quartzclock", "Quartz Clock");
		LanguageRegistry.instance().addStringLocalization("modularcomputing:ram", "Random Access Memory");

		// item = new Item(Configs.itemId);

		// block = new Block(blockId);
		// GameRegistry.registerBlock(block);

		// GameRegistry.registerTileEntity(TileEntity.class, "myTile");

		// GameRegistry.addRecipe(new ItemStack(itemId), new Object[] {});

		// EntityRegistry.registerModEntity(entity.class, "myEntity", 0, this, 32, 10, true)
	        NetworkRegistry.instance().registerGuiHandler(this, new NetworkHandler());
                NetworkRegistry.instance().registerChannel(new NetworkHandler(), "MoCoSCMD", Side.SERVER);
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {

	}

}
