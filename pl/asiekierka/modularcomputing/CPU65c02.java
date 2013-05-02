package pl.asiekierka.modularcomputing;

import java.util.*;

public class CPU65c02 implements ICPUEmulator {
    private int A, X, Y, SP, PC;
    private CPUThread cpu;
    private boolean[] flags;
    public static final int flagN = 7;
    public static final int flagV = 6;
    public static final int flagB = 4;
    public static final int flagD = 3;
    public static final int flagI = 2;
    public static final int flagZ = 1;
    public static final int flagC = 0;
    public static final int bflagN = 128;
    public static final int bflagV = 64;
    public static final int bflagB = 16;
    public static final int bflagD = 8;
    public static final int bflagI = 4;
    public static final int bflagZ = 2;
    public static final int bflagC = 1;
    public static final int modeImm = 2;
    public static final int modeZP = 3;
    public static final int modeZPX = 4;
    public static final int modeABS = 5;
    public static final int modeABX = 6;
    public static final int modeABY = 7;
    public static final int modeIX = 8;
    public static final int modeIY = 9;
    public static final int modeIND = 10;
    public static final int modeZPY = 11;
    public static final int modeA = 12;
    public static final int modeX = 13;
    public static final int modeY = 14;
    public static final int[] instrLength = {
      1, 1, 2, 2, 2, 3, 3, 3, 2, 2, 3, 2, 1, 1, 1
    };
    public static final int[][] opcodeInfo = {
        {0x00, 0, 7},		{0x01, modeIX, 6},	null,			null, // 0x00
        null,			{0x01, modeZP, 3},	{0x02, modeZP, 5},	null,
        {0xA0, 1, 3}, 		{0x01, modeImm, 2},	{0x02, modeA, 2},	null,
        null,			{0x01, modeABS, 4},	{0x02, modeABS, 6},	null,
        {0x80, 0, 2},		{0x01, modeIY, 5},	null,			null, // 0x10
        null,			{0x01, modeZPX, 4},	{0x02, modeZPX, 6},	null,
        {0x90, 0, 2},		{0x01, modeABY, 4},	null,			null,
        null,			{0x01, modeABX, 4},	{0x02, modeABX, 7},	null,
        {0x03, modeABS, 6},	{0x04, modeIX, 6},	null,			null, // 0x20
        {0x05, modeZP, 3},	{0x04, modeZP, 3},	{0x06, modeZP, 5},	null,
        {0xA0, 0, 4},		{0x04, modeImm, 2},	{0x06, modeA, 2},	null,
        {0x05, modeABS, 4},	{0x04, modeABS, 4},	{0x06, modeABS, 6},	null,
        {0x81, 0, 2},		{0x04, modeIY, 5},	null,			null, // 0x30
        null,			{0x04, modeZPX, 4},	{0x06, modeZPX, 6},	null,
        {0x90, 1, 2},		{0x04, modeABY, 4},	null,			null,
        null,			{0x04, modeABX, 4},	{0x06, modeABX, 7},	null,
        {0x07, 0, 6},		{0x08, modeIX, 6},	null,			null, // 0x40
        null,			{0x08, modeZP, 3},	{0x09, modeZP, 5},	null,
        {0xA1, 1, 3},		{0x08, modeImm, 2},	{0x09, modeA, 2},	null,
        {0x0A, modeABS, 3},	{0x08, modeABS, 4},	{0x09, modeABS, 6},	null,
        {0x82, 0, 2},		{0x08, modeIY, 5},	null,			null, // 0x50
        null,			{0x08, modeZPX, 4},	{0x09, modeZPX, 6},	null,
        {0x91, 0, 2},		{0x08, modeABY, 4},	null,			null,
        null,			{0x08, modeABX, 4},	{0x09, modeABX, 7},	null,
        {0x0B, 0, 6},		{0x0C, modeIX, 6},	null,			null, // 0x60
        null,			{0x0C, modeZP, 3},	{0x0D, modeZP, 5},	null,
        {0xA1, 0, 4},		{0x0C, modeImm, 2},	{0x0D, modeA, 2},	null,
        {0x0A, modeIND, 5},	{0x0C, modeABS, 4},	{0x0D, modeABS, 6},	null,
        {0x83, 0, 2},		{0x0C, modeIY, 5},	null,			null, // 0x70
        null,			{0x0C, modeZPX, 4},	{0x0D, modeZPX, 6},	null,
        {0x91, 1, 2},		{0x0C, modeABY, 4},	null,			null,
        null,			{0x0C, modeABX, 4},	{0x0D, modeABX, 7},	null,
        null,			{0x0E, modeIX, 6},	null,			null, // 0x80
        {0x10, modeZP, 3},	{0x0E, modeZP, 3},	{0x0F, modeZP, 3},	null,
        {0x70, -1, 2},		null,			{0x60, 0, 2},		null,
        {0x10, modeABS, 4},	{0x0E, modeABS, 4},	{0x0F, modeABS, 4},	null,
        {0x84, 0, 2},		{0x0E, modeIY, 6},	null,			null, // 0x90
        {0x10, modeZPX, 4},	{0x0E, modeZPX, 4},	{0x0F, modeZPY, 4},	null,
        {0x61, 0, 2},		{0x0E, modeABY, 5},	{0x68, 0, 2},		null,
        null,			{0x0E, modeABX, 5},	null,			null,
        {0x13, modeImm, 2},	{0x11, modeIX, 6},	{0x12, modeImm, 2},	null, // 0xA0
        {0x13, modeZP, 3},	{0x11, modeZP, 3},	{0x12, modeZP, 3},	null,
        {0x62, 0, 2},		{0x11, modeImm, 2},	{0x63, 0, 2},		null,
        {0x13, modeABS, 4},	{0x11, modeABS, 4},	{0x12, modeABS, 4},	null,
        {0x85, 0, 2},		{0x11, modeIY, 5},	null,			null, // 0xB0
        {0x13, modeZPX, 4},	{0x11, modeZPX, 4},	{0x12, modeZPY, 4},	null,
        {0x92, 0, 2},		{0x11, modeABY, 4},	{0x69, 0, 2},		null,
        {0x13, modeABX, 4},	{0x11, modeABX, 4},	{0x12, modeABY, 4},	null,
        {0x16, modeImm, 2},	{0x14, modeIX, 6},	null,			null, // 0xC0
        {0x16, modeZP, 3},	{0x14, modeZP, 3},	{0x17, modeZP, 5},	null,
        {0x70, 1, 2},		{0x14, modeImm, 2},	{0x71, -1, 2},		null,
        {0x16, modeABS, 4},	{0x14, modeABS, 4},	{0x17, modeABS, 6},	null,
        {0x86, 0, 2},		{0x14, modeIY, 5},	null,			null, // 0xD0
        null,			{0x14, modeZPX, 4},	{0x17, modeZPX, 6},	null,
        {0x93, 0, 2},		{0x14, modeABY, 4},	null,			null,
        null,			{0x14, modeABX, 4},	{0x17, modeABX, 7},	null,
        {0x15, modeImm, 2},	{0x19, modeIX, 6},	null,			null, // 0xE0
        {0x15, modeZP, 3},	{0x19, modeZP, 3},	{0x18, modeZP, 5},	null,
        {0x71, 1, 2},		{0x19, modeImm, 2},	{0x1A, 0, 2},		null,
        {0x15, modeABS, 4},	{0x19, modeABS, 4},	{0x18, modeABS, 6},	null,
        {0x87, 0, 2},		{0x19, modeIY, 5},	null,			null, // 0xF0
        null,			{0x19, modeZPX, 4},	{0x18, modeZPX, 6},	null,
        {0x93, 1, 2},		{0x19, modeABY, 4},	null,			null,
        null,			{0x19, modeABX, 4},	{0x18, modeABX, 7},	null
    };
    public CPU65c02() {
        A=0;X=0;Y=0; SP = 255;
        flags = new boolean[8];
    }
    public int getByte(int pos) {
        return cpu.mem.read8(pos&65535) & 0xFF;
    }
    public void setByte(int pos, int val) {
        cpu.mem.write8(pos&65535, val&0xFF);
    }
    public int getAddr(int addrMode) {
        int addrH, addr;
        switch(addrMode) {
            case modeImm:
                return PC+1;
            case modeZP:
                return getByte(PC+1);
            case modeZPX:
                return (getByte(PC+1)+X)&255;
            case modeZPY:
                return (getByte(PC+1)+Y)&255;
            case modeABS:
                return (getByte(PC+2)<<8) | getByte(PC+1);
            case modeABX:
                return (((getByte(PC+2)<<8) | getByte(PC+1))+X)&65535;
            case modeABY:
                return (((getByte(PC+2)<<8) | getByte(PC+1))+Y)&65535;
            case modeIND:
                addr = getAddr(modeABS);
                addrH = addr+1;
                if((addrH&255) == 0) addrH -= 256;
                return (getByte(addrH)<<8) | getByte(addr);
            case modeIX:
                addr = getAddr(modeZPX);
                return (((getByte((addr+1)&255)<<8) | getByte(addr))+Y)&65535;
            case modeIY:
                addr = getByte(PC+1);
                addrH = addr+1;
                if((addrH&255) == 0) addrH -= 256;
                return (((getByte(addrH)<<8) | getByte(addr))+Y)&65535;
            default:
                return 0;
	}
    }
    public int getPtByte(int addrMode) {
        switch(addrMode) {
            case modeA:
                return A;
            case modeX:
                return X;
            case modeY:
                return Y;
            default:
                return getByte(getAddr(addrMode));
        }
    }
    public void setPtByte(int addrMode, int val) {
        switch(addrMode) {
            case modeA:
                A = val&255;
            case modeX:
                X = val&255;
            case modeY:
                Y = val&255;
            default:
                setByte(getAddr(addrMode), val);
        }
    }
    public int getPC() { return PC; }
    public void setPC(int p) { PC = p; }
    public void push(int val) {
      setByte(256+SP, val);
      SP = (SP-1)&255;
    }
    public void push16(int val) {
      push(val>>8);
      push(val&255);
    }
    public int pull(boolean setFlags) {
      SP = (SP+1)&255;
      int t = getByte(256+SP);
      if(setFlags) updateFlagNZ(t);
      return t;
    }
    public int pull16(boolean setFlags) {
      int v = pull(setFlags);
      return (pull(setFlags)<<8)|v;
    }
    public int getPStatus() {
        int pstatus = 0;
        for(int i=0;i<8;i++)
            pstatus |= ((flags[i]?1:0)<<i);
        return pstatus | 0x20; // bit 5 is always set
    }
    public void setPStatus(int val) {
        for(int i=0;i<8;i++)
            flags[i] = ((val & (1<<i)) == (1<<i));
    }
    public void compare(int a, int b) {
        int val = a-b;
        flags[flagC] = (a >= b);
        flags[flagZ] = (val == 0);
        flags[flagN] = (val&0x80)>0;
    }
    public void updateFlagNZ(int val) {
        flags[flagZ] = (val == 0);
        flags[flagN] = (val&0x80)>0;
    }
    public int branch(boolean check) {
        PC++;
        if(!check) return 0;
        byte pos = (byte)getByte(PC);
        int oldPCbank = PC>>8;
        PC+=pos;
        if(oldPCbank!=(PC>>8)) return 2;
        else return 1;
    }
    // OVERFLOW CODE:
    // http://www.righto.com/2012/12/the-6502-overflow-flag-explained.html
    public String getName() { return "65c02"; }
    public int tick(CPUThread cpur, int maxCycles) {
        cpu = cpur;
        int cycles = 0;
        while(cycles<maxCycles) {
        int prevPC = PC;
        int opcode = getByte(PC);
        int[] opData = opcodeInfo[opcode];
        ModularComputing.debug("PC="+Integer.toHexString(PC)+" OPC=" + Integer.toHexString(opcode)+
                               " A="+Integer.toHexString(A)+" X="+Integer.toHexString(X)+" Y="+Integer.toHexString(Y)+
                               " SP="+Integer.toHexString(SP)+" "+(flags[flagN]?"N":" ")+(flags[flagV]?"V":" ")+
                               (flags[flagB]?"B":" ")+(flags[flagD]?"D":" ")+(flags[flagI]?"I":" ")+(flags[flagZ]?"Z":" ")+
                               (flags[flagC]?"C":" "));
        if(opData != null) {
          int cycleCount = opData[2];
          switch(opData[0]) {
              case 0x44: // BRK (65c02, also clears decimal flag)
                flags[flagD] = false; // NO BREAK! it has to continue
              case 0x00: // BRK
                flags[flagB] = true;
                ModularComputing.debug("[BRK] "+A);
                break;
              case 0x01: // ORA
                  A = A | getPtByte(opData[1]);
                  updateFlagNZ(A);
                  break;
              case 0x02: // ASL
                  int aslByte = getPtByte(opData[1]);
                  flags[flagC] = (aslByte >= 128);
                  aslByte = (aslByte<<1)&255;
                  setPtByte(opData[1],aslByte);
                  updateFlagNZ(aslByte);
                  break;
              case 0x03: // JSR
                  push16(PC+instrLength[opData[1]]-1);
              case 0x0A: // JMP
                  PC = getAddr(opData[1]) - instrLength[opData[1]];
                  break;
              case 0x04: // AND
                  A = A & getPtByte(opData[1]);
                  updateFlagNZ(A);
                  break;
              case 0x05: // BIT
                  int bb = getPtByte(opData[1]);
                  flags[flagV] = (bb & 0x40)>0;
                  updateFlagNZ(bb);
                  break;
              case 0x06: // ROL
                  int rol = (getPtByte(opData[1]) << 1) | (flags[flagC]?1:0);
                  flags[flagC] = (rol>=256);
                  setPtByte(opData[1],rol&255);
                  break;
              case 0x0D: // ROR
                  int rorO = getPtByte(opData[1]);
                  int ror = (rorO >> 1) | (flags[flagC]?128:0);
                  flags[flagC] = (rorO&1)==1;
                  setPtByte(opData[1],ror);
                  break;
              case 0x08: // EOR
                  A = A ^ getPtByte(opData[1]);
                  updateFlagNZ(A);
                  break;
              case 0x09: // LSR
                  int lsrByte = getPtByte(opData[1]);
                  setPtByte(opData[1],(lsrByte>>1)&255);
                  flags[flagC] = ((lsrByte&1) == 1);
                  flags[flagZ] = (lsrByte<2); // 0/1 == 0
                  break;
              case 0x07: // RTI
                  setPStatus(pull(false));
                  PC = pull16(false)-1;
                  break;
              case 0x0B: // RTS
                  PC = pull16(false);
                  break;
              case 0x0C: // ADC
                  int adcA = A;
                  int adcB = getPtByte(opData[1]);
                  A = A + adcB + (flags[flagC]?1:0);
                  if(A>255) { A = A & 255; flags[flagC] = true; } else flags[flagC] = false;
                  flags[flagV] = ((adcA^A)&(adcB^A)&0x80) > 0;
                  updateFlagNZ(A);
                  break;
              case 0x0E: // STA
                  setPtByte(opData[1], A);
                  break;
              case 0x0F: // STX
                  setPtByte(opData[1], X);
                  break;
              case 0x10: // STY
                  setPtByte(opData[1], Y);
                  break;
              case 0x11: // LDA
                  A = getPtByte(opData[1]);
                  updateFlagNZ(A);
                  break;
              case 0x12: // LDX
                  X = getPtByte(opData[1]);
                  updateFlagNZ(X);
                  break;
              case 0x13: // LDY
                  Y = getPtByte(opData[1]);
                  updateFlagNZ(Y);
                  break;
              case 0x14: // CMP
                  compare(A,getPtByte(opData[1]));
                  break;
              case 0x15: // CPX
                  compare(X,getPtByte(opData[1]));
                  break;
              case 0x16: // CPY
                  compare(Y,getPtByte(opData[1]));
                  break;
              case 0x17: // DEC
                  int db = (getPtByte(opData[1])-1)&255;
                  setPtByte(opData[1],db);
                  updateFlagNZ(db);
                  break;
              case 0x18: // INC
                  int ib = (getPtByte(opData[1])+1)&255;
                  setPtByte(opData[1],ib);
                  updateFlagNZ(ib);
                  break;
              case 0x19: // SBC
                  int sbcA = A;
                  int sbcB = getPtByte(opData[1]);
                  A = A - sbcB - (flags[flagC] ? 0 : 1);
                  if(A>255 || A<0) { A = A & 255; flags[flagC] = false; } else flags[flagC] = true;
                  flags[flagV] = ((sbcA^A)&((255-sbcB)^A)&0x80) > 0;
                  updateFlagNZ(A);
                  break;
              case 0x1A: // NOP
                  break;
              // 0x40-0x5F - 65c02 extensions
              case 0x40: // STZ
                  setPtByte(opData[1],0);
                  break;
              case 0x41: // BRA
                  cycleCount += branch(true);
                  break;
              case 0x42: // TRB
              case 0x43: // TSB
              case 0x45: // ADC [65c02]
                  break;
              case 0x46: // SBC [65c02]
                  break;
              case 0x50: // DEA/INA
                  A = (A+opData[1])&0xFF;
                  updateFlagNZ(A);
                  break;
              case 0x60: // TXA
                  A = X;
                  updateFlagNZ(A);
                  break;
              case 0x61: // TYA
                  A = Y;
                  updateFlagNZ(A);
                  break;
              case 0x62: // TAY
                  Y = A;
                  updateFlagNZ(Y);
                  break;
              case 0x63: // TAX
                  X = A;
                  updateFlagNZ(X);
                  break;
              case 0x68: // TXS
                  SP = X;
                  break;
              case 0x69: // TSX
                  X = SP;
                  updateFlagNZ(X);
                  break;
              case 0x70: // DEY/INY
                  Y = (Y+opData[1])&0xFF;
                  updateFlagNZ(Y);
                  break;
              case 0x71: // DEX/INX
                  X = (X+opData[1])&0xFF;
                  updateFlagNZ(X);
                  break;
              case 0x80: // BPL
                  cycleCount += branch(!flags[flagN]);
                  break;
              case 0x81: // BMI
                  cycleCount += branch(flags[flagN]);
                  break;
              case 0x82: // BVC
                  cycleCount += branch(!flags[flagV]);
                  break;
              case 0x83: // BVS
                  cycleCount += branch(flags[flagV]);
                  break;
              case 0x84: // BCC
                  cycleCount += branch(!flags[flagC]);
                  break;
              case 0x85: // BCS
                  cycleCount += branch(flags[flagC]);
                  break;
              case 0x86: // BNE
                  cycleCount += branch(!flags[flagZ]);
                  break;
              case 0x87: // BEQ
                  cycleCount += branch(flags[flagZ]);
                  break;
              case 0x90: // CLC/SEC
                  flags[flagC] = (opData[1] == 1);
                  break;
              case 0x91: // CLI/SEI
                  flags[flagI] = (opData[1] == 1);
                  break;
              case 0x92: // CLV
                  flags[flagV] = false;
                  break;
              case 0x93: // CLD/SED
                  flags[flagD] = (opData[1] == 1);
                  break;
              // PUSH is 1, PULL is 0
              case 0xA0: // PHP/PLP
                  if(opData[1] == 1)	{ flags[flagB] = true; push(getPStatus()); }
                  else			{ setPStatus(pull(true)); }
                  break;
              case 0xA1: // PHA/PLA
                  if(opData[1] == 1)	{ push(A); }
                  else			{ A = pull(true); }
                  break;
              case 0xA2: // PHX/PLX (65c02)
                  if(opData[1] == 1)	{ push(X); }
                  else			{ X = pull(true); }
                  break;
              case 0xA3: // PHY/PLY (65c02)
                  if(opData[1] == 1)	{ push(Y); }
                  else			{ Y = pull(true); }
                  break;
          }
          if(opData[1]>0)
            PC+=instrLength[opData[1]];
          else PC++;
          cycles += cycleCount;
          if(prevPC == PC) {
            ModularComputing.debug("[TRAP] TRAP!");
            return -100000;
          }
       } else { cycles += 2; PC++; }
       }
       return (cycles - maxCycles);
    }
}
