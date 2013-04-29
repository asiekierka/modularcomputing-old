package pl.asiekierka.modularcomputing;

import java.nio.*;
import java.util.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.player.*;

import org.lwjgl.opengl.GL11;

public class GuiMonitor extends GuiScreen {
	TileEntityMonitor monitor;
	private int glTexId;

	public GuiMonitor(TileEntityMonitor tileEntity) {
		monitor = tileEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		glTexId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTexId);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 256,
				256, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
				monitor.getBuffer());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}
	@Override
	protected void keyTyped(char par1, int par2) {
		ModularComputing.debug("Typed char "+par1+"!");
		super.keyTyped(par1,par2);
	}
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		GL11.glColor4f(1.0F,1.0F,1.0F,1.0F);
		this.mc.renderEngine.bindTexture("/gui/monitor_overlay.png");
		int x = (width - 246) / 2;
		int y = (height - 186) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, 246,186);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTexId);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 256,
				256, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
				monitor.getBuffer());
		x = (width - monitor.getWidth()) / 2;
		y = (height - monitor.getHeight()) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, monitor.getWidth(), monitor.getHeight());
		super.drawScreen(par1, par2, par3);
	}
	// For singleplayer.
	@Override
	public boolean doesGuiPauseGame() { return false; }
	@Override
	public void updateScreen() {
		monitor.debugRandom();
		super.updateScreen();
	}
}
