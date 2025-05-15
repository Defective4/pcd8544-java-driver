package io.github.defective4.pi.pcd8544;

public class PCDConstants {
    public static final int LCDHEIGHT = 48; // < 48 pixels high
    public static final int LCDWIDTH = 84; // < LCD is 84 pixels wide

    public static final int PCD8544_DISPLAYALLON = 0x1; // < Display control, all segments on
    public static final int PCD8544_DISPLAYBLANK = 0x0; // < Display control, blank
    public static final int PCD8544_DISPLAYCONTROL = 0x08; // < Basic instruction set - Set display configuration

    public static final int PCD8544_DISPLAYINVERTED = 0x5; // < Display control, inverse mode
    public static final int PCD8544_DISPLAYNORMAL = 0x4; // < Display control, normal mode
    public static final int PCD8544_ENTRYMODE = 0x02; // < Function set, Entry mode
    public static final int PCD8544_EXTENDEDINSTRUCTION = 0x01; // < Function set, Extended instruction set control

    public static final int PCD8544_FUNCTIONSET = 0x20; // < Basic instruction set
    public static final int PCD8544_POWERDOWN = 0x04; // < Function set, Power down mode
    public static final int PCD8544_SETBIAS = 0x10; // < Extended instruction set - Set bias system
    public static final int PCD8544_SETTEMP = 0x04; // < Extended instruction set - Set temperature coefficient

    public static final int PCD8544_SETVOP = 0x80; // < Extended instruction set - Write Vop to register
    public static final int PCD8544_SETXADDR = 0x80; // < Basic instruction set - Set X address of RAM, 0 <= X <= 83
    public static final int PCD8544_SETYADDR = 0x40; // < Basic instruction set - Set Y address of RAM, 0 <= Y <= 5
}
