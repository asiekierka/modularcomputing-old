package pl.asiekierka.modularcomputing;

import java.io.*;
import java.util.*;
import net.minecraft.src.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.*;
import net.minecraftforge.common.*;
import net.minecraft.network.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.*;

public class NetworkHandler implements IPacketHandler, IGuiHandler {

        @Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
            if(packet.channel.equals("MoCoSCMD")) {
                handleMCPacket(packet);
            }
	}
        private void handleMCPacket(Packet250CustomPayload packet) {
            ModularComputing.debug("Received ModuComp Server CoMmanD packet!");
            DataInputStream is = new DataInputStream(new ByteArrayInputStream(packet.data));
            int dim, x, y, z, ic, ip;
            try {
                dim = is.readInt();
                x = is.readInt();
                y = is.readInt();
                z = is.readInt();
                ic = is.readInt();
                ip = is.readInt();
            } catch(IOException e) { e.printStackTrace(); return; }
            World world = DimensionManager.getWorld(dim);
            if(world == null || world.isRemote) return;
            ModularComputing.debug("Okay to continue!");
            TileEntity te = world.getBlockTileEntity(x,y,z);
            switch(ic) {
                case 1: // Poweron
                    ModularComputing.debug("Launching test CPUThread!");
                    CPUThread test = new CPUThread(null, 10000, (TileEntityChassis)te);
                    Thread tTest = new Thread(test);
                    tTest.start();
                    break;
            }
        }

        //returns an instance of the Container you made earlier
        @Override
        public Object getServerGuiElement(int id, EntityPlayer player, World world,
                        int x, int y, int z) {
                TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
                if(tileEntity instanceof TileEntityChassis){
                        return new ContainerChassis(player.inventory, (TileEntityChassis) tileEntity);
                }
                else if(tileEntity instanceof TileEntityMonitor){
                        return new ContainerNull();
                }
                return null;
        }

        //returns an instance of the Gui you made earlier
        @Override
        public Object getClientGuiElement(int id, EntityPlayer player, World world,
                        int x, int y, int z) {
                TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
                if(tileEntity instanceof TileEntityChassis){
                        return new GuiChassis(player.inventory, (TileEntityChassis) tileEntity);
                }
                else if(tileEntity instanceof TileEntityMonitor){
                        return new GuiMonitor((TileEntityMonitor) tileEntity);
                }
                return null;

        }
}

