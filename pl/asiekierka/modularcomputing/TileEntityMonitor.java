package pl.asiekierka.modularcomputing;

import java.nio.*;
import java.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMonitor extends TileEntity {
	private int width, height;
	private ByteBuffer screen;
	private Random rand;
	private boolean changed = false;

	public TileEntityMonitor(int w, int h) {
		rand = new Random();
		width = w; height = h;
		screen = ByteBuffer.allocateDirect(256*256*3);
		screen.clear();
		screen.limit(256*256*3);
		debugRandom();
	}

	public void debugRandom() {
		for(int ch=0;ch<180;ch++) {
		for(int cw=0;cw<240;cw++) {
			byte t = (byte)(rand.nextInt()&255);
			setPixel(cw,ch,t);
		}
		}
	}

	public byte[] getPixel(int x, int y) {
		byte[] pixel = new byte[3];
		screen.put(pixel,((y*256)+x)*3,3);
		screen.position(0);
		return pixel;
	}
	public void setPixel(int x, int y, byte[] data) {
		screen.position(((y*256)+x)*3);
		screen.put(data);
		screen.position(0);
		changed = true;
	}
	public void setPixel(int x, int y, byte r, byte g, byte b) {
		screen.position(((y*256)+x)*3);
		screen.put(r);
		screen.put(g);
		screen.put(b);
		screen.position(0);
		changed = true;
	}
	public void setPixel(int x, int y, byte c) {
		screen.position(((y*256)+x)*3);
		screen.put(c);
		screen.put(c);
		screen.put(c);
		screen.position(0);
		changed = true;
	}
	public TileEntityMonitor() {
		this(240, 180);
	}
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public ByteBuffer getBuffer() { return screen; }
}
