package pl.asiekierka.modularcomputing;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.network.packet.*;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.network.*;

import org.lwjgl.opengl.GL11;

public class GuiChassis extends GuiContainer {

        TileEntityChassis tec;

	public GuiChassis(InventoryPlayer inventoryPlayer, TileEntityChassis tileEntity) {
		super(new ContainerChassis(inventoryPlayer, tileEntity));
		tec = tileEntity;
		xSize = 176;
		ySize = 230;
	}
	@Override
	public void initGui() {
		super.initGui();
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		buttonList.add(new GuiButton(1, left+133, top+66, 32, 20, "On"));
	}
	@Override
	protected void actionPerformed(GuiButton button) {
            ModularComputing.debug("[Chassis] Pressed button ID " + button.id);
            if(button.id == 1) { // Poweron
                Packet250CustomPayload packet = MCHelper.createPacket(tec.worldObj,tec.xCoord,tec.yCoord,tec.zCoord,1,0);
                PacketDispatcher.sendPacketToServer(packet);
            }
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2) {
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		fontRenderer.drawString("Offline", 86, 12, 0xFFFFFF);
		fontRenderer.drawString("ALPHA VERSION!", 86, 22, 0xFFFFFF);
		fontRenderer.drawString("TESTING ONLY", 86, 32, 0xFFFFFF);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float p1, int p2, int p3) {
		GL11.glColor4f(1.0F,1.0F,1.0F,1.0F);
		this.mc.renderEngine.bindTexture("/gui/chassis.png");
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
