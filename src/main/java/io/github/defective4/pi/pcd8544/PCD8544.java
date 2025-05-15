package io.github.defective4.pi.pcd8544;

import static io.github.defective4.pi.pcd8544.PCDConstants.*;

import java.util.Arrays;

import com.pi4j.io.gpio.digital.DigitalOutput;

public class PCD8544 extends SPIDevice {

    public static final boolean MODE_COMMAND = false;
    public static final boolean MODE_DATA = true;

    private int bias;
    private final int[] buffer = new int[LCDWIDTH * LCDHEIGHT / 8];
    private int contrast;
    private final DigitalOutput dcPin, resetPin;

    public PCD8544(DigitalOutput clockPin, DigitalOutput dataPin, DigitalOutput chipSelectPin, DigitalOutput dcPin,
            DigitalOutput resetPin) {
        super(clockPin, dataPin, chipSelectPin);
        this.dcPin = dcPin;
        this.resetPin = resetPin;
    }

    public void clear() {
        Arrays.fill(buffer, 0);
    }

    public void display() {
        int rows = LCDHEIGHT / 8;
        for (int i = 0; i < rows; i++) {
            setMode(MODE_COMMAND);
            writeSPI(PCD8544_SETYADDR | i);
            writeSPI(PCD8544_SETXADDR | 0);
            setMode(MODE_DATA);
            for (int j = 0; j < LCDWIDTH; j++) {
                writeSPI(buffer[LCDWIDTH * i + j]);
            }
        }
    }

    public void init() {
        init(75, 4);
    }

    public void init(int contrast, int bias) {
        initSPI();
        resetPin.low();
        delay(1);
        resetPin.high();

        setBias(bias);
        setContrast(contrast);

        setMode(MODE_COMMAND);
        writeSPI(PCD8544_FUNCTIONSET);
        writeSPI(PCD8544_DISPLAYCONTROL | PCD8544_DISPLAYNORMAL);
    }

    public void set2DBuffer(byte[][] buffer) {
        clear();
        if (buffer.length != LCDWIDTH)
            throw new IllegalArgumentException("The 2D buffer must be " + LCDWIDTH + "x" + LCDHEIGHT);
        for (byte[] sub : buffer) {
            if (sub.length != LCDHEIGHT)
                throw new IllegalArgumentException("The 2D buffer must be " + LCDWIDTH + "x" + LCDHEIGHT);
        }
        for (int x = 0; x < buffer.length; x++) {
            byte[] sub = buffer[x];
            for (int y = 0; y < sub.length; y++) {
                if (buffer[x][y] > 0) {
                    int page = y / 8;
                    int i = x + page * LCDWIDTH;
                    int modY = y % 8;
                    this.buffer[i] |= 1 << modY;
                }
            }
        }
    }

    public void setBias(int val) {
        if (val > 0x07) {
            val = 0x07;
        }
        bias = val;
        setMode(MODE_COMMAND);
        writeSPI(PCD8544_FUNCTIONSET | PCD8544_EXTENDEDINSTRUCTION);
        writeSPI(PCD8544_SETBIAS | val);
        writeSPI(PCD8544_FUNCTIONSET);
    }

    public void setContrast(int val) {
        if (val > 0x7f) {
            val = 0x7f;
        }
        contrast = val;
        setMode(MODE_COMMAND);
        writeSPI(PCD8544_FUNCTIONSET | PCD8544_EXTENDEDINSTRUCTION);
        writeSPI(PCD8544_SETVOP | val);
        writeSPI(PCD8544_FUNCTIONSET);
    }

    public void setDirectBuffer(int[] buffer) {
        int len = Math.min(this.buffer.length, buffer.length);
        for (int i = 0; i < len; i++) this.buffer[i] = buffer[i];
    }

    public void writeCommand(int val) {
        setMode(MODE_COMMAND);
        writeSPI(val);
    }

    public void writeData(int val) {
        setMode(MODE_DATA);
        writeSPI(val);
    }

    private void setMode(boolean mode) {
        dcPin.setState(mode);
    }
}
