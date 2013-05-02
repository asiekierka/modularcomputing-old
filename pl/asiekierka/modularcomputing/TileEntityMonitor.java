package pl.asiekierka.modularcomputing;

import java.io.*;
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
	private boolean isOn = false;
        private int[] font;
	private ArrayList<int[]> packets;
        private short[] pal = {
            0x00, 0x00, 0x00,		0x40, 0x40, 0x40,
	    0x80, 0x80, 0x80,		0xC0, 0xC0, 0xC0,
        };
	public TileEntityMonitor(int w, int h) {
		this.rand = new Random();
		this.packets = new ArrayList<int[]>();
		width = w; height = h;
                font = MCHelper.fileToIntArray("mods/modularcomputing/rawcga.bin");
		screen = ByteBuffer.allocateDirect(256*256*3);
		clearScreen();
		updateScreen();
	}
	public void clearScreen() { screen.clear(); screen.limit(256*256*3); }
	public void updateScreen() {
                if(!isOn) {
			for(int ch=0;ch<180;ch++) {
			for(int cw=0;cw<240;cw++) {
				byte t = (byte)(rand.nextInt()&255);
				setPixel(cw,ch,t,t,t);
			}
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
	public void drawPixel(int ox, int oy, int c) {
                int x = ox%width;
                int y = oy%height;
                if(!worldObj.isRemote) {
			int[] packet = {2, x, y, c};
			addPacket(packet);
			return;
		}
		screen.position(((y*256)+x)*3);
		screen.put((byte)pal[c*3]);
		screen.put((byte)pal[c*3+1]);
		screen.put((byte)pal[c*3+2]);
		screen.position(0);
		changed = true;
	}
	public void drawChar(int ox, int oy, int chr, int col) {
                int x = ox%width;
                int y = oy%height;
                if(!worldObj.isRemote) {
			int[] packet = {3, x, y, chr, col};
			addPacket(packet);
			return;
		}
                for(int iy=0;iy<8;iy++) {
                    int cl = font[(chr<<3)+iy];
                    for(int ix=0;ix<8;ix++) {
                        if(((cl<<ix)&128)>0)
                            drawPixel(x+ix, y+iy, col%16);
                        else
                            drawPixel(x+ix, y+iy, col>>4);
                    }
                }
	}
	public void scrollUp(int am2) {
                int amount = Math.min(am2, height);
                if(!worldObj.isRemote) {
			int[] packet = {4, amount};
			addPacket(packet);
			return;
		}
                int xDiff = amount*width*3;
                int xEnd = (width*height*3)-xDiff;
                byte[] copy = Arrays.copyOfRange(screen.array(), xDiff, width*height);
                screen.put(copy,0,xEnd);
                for(int i=xEnd;i<(width*height*3);i++)
                    screen.put(i, (byte)0);
	}
	public TileEntityMonitor() {
		this(240, 180);
	}
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public ByteBuffer getBuffer() { return screen; }
	public void setPower(boolean pwr) {
                if(worldObj.isRemote) {
			int[] packet = {(pwr?1:0)};
			addPacket(packet);
			return;
		}
		isOn = pwr;
		if(isOn) clearScreen();
	}
	public void addPacket(int[] packet) {
		packets.add(packet);
	}
	public byte[] getPackets() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for(int[] data: packets) {
			out.write((byte)data.length);
			for(int integer: data) out.write((byte)integer);
		}
		packets = new ArrayList<int[]>();
		return out.toByteArray();
	}
        public void parsePacket(int[] packet) {
            if(packet.length==0) return;
            switch(packet[0]) {
                case 0: // Turn off
                    setPower(false);
                    break;
                case 1: // Turn on
                    setPower(true);
                    break;
                case 2: // Draw pixel(X,Y,col)
                    if(packet.length!=4) return;
                    drawPixel(packet[1],packet[2],packet[3]);
                case 3: // Draw char(X,Y,col,chr)
                    if(packet.length!=5) return;
                    drawChar(packet[1],packet[2],packet[4],packet[3]);
                case 4: // Scroll up
                    if(packet.length!=2) return;
                    scrollUp(packet[1]);
                default:
                    break;
            }
        }
	public void readFromNBT(NBTTagCompound cpd) {
            byte[] packets = cpd.getByteArray("monitorP");
            if(packets == null || packets.length == 0) return;
            int i = 0;
            while(i<packets.length) {
                int[] packet = new int[(int)packets[i++]&0xFF];
                for(int j=0;j<packet.length;j++) {
                    packet[j] = (int)packets[i+j]&0xFF;
                }
                parsePacket(packet);
                i+=packet.length;
            }
	}
	public void writeToNBT(NBTTagCompound cpd) {
		cpd.setByteArray("monitorP", getPackets());
	}
}
