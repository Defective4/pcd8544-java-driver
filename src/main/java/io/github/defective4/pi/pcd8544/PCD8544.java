package io.github.defective4.pi.pcd8544;

import static io.github.defective4.pi.pcd8544.PCDConstants.*;
import static java.lang.Math.min;

import java.util.Arrays;

import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.spi.Spi;

public class PCD8544 extends SPIDevice {

    public static final boolean MODE_COMMAND = false;
    public static final boolean MODE_DATA = true;

    private int bias;
    private final int[] buffer = new int[LCDWIDTH * LCDHEIGHT / 8];
    private int contrast;
    private final DigitalOutput dcPin, resetPin;
    private int xUpdateMax;
    private int xUpdateMin;
    private int yUpdateMax;
    private int yUpdateMin;

    public PCD8544(DigitalOutput clockPin, DigitalOutput dataPin, DigitalOutput chipSelectPin, DigitalOutput dcPin,
            DigitalOutput resetPin) {
        super(clockPin, dataPin, chipSelectPin);
        this.dcPin = dcPin;
        this.resetPin = resetPin;
    }

    public PCD8544(Spi spi, DigitalOutput dcPin, DigitalOutput resetPin) {
        super(spi);
        this.dcPin = dcPin;
        this.resetPin = resetPin;
    }

    public void clear() {
        Arrays.fill(buffer, 0);
        updateBoundingBox(0, 0, LCDWIDTH - 1, LCDHEIGHT - 1);
    }

    public void display() {
        int startY = yUpdateMin / 8;
        int endY = yUpdateMax / 8;
        for (int i = startY; i <= endY; i++) {
            setMode(MODE_COMMAND);
            writeSPI(PCD8544_SETYADDR | i);

            int startcol = xUpdateMin;
            int endcol = xUpdateMax;

            writeSPI(PCD8544_SETXADDR | startcol);
            setMode(MODE_DATA);
            for (int j = startcol; j <= endcol; j++) {
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

        updateBoundingBox(0, 0, LCDWIDTH - 1, LCDHEIGHT - 1);
    }

    public void set2DBuffer(byte[][] buffer) {
        set2DBuffer(buffer, false);
    }

    public void set2DBuffer(byte[][] buffer, boolean allowPartialUpdates) {
        if (buffer.length != LCDWIDTH)
            throw new IllegalArgumentException("The 2D buffer must be " + LCDWIDTH + "x" + LCDHEIGHT);
        for (byte[] sub : buffer) {
            if (sub.length != LCDHEIGHT)
                throw new IllegalArgumentException("The 2D buffer must be " + LCDWIDTH + "x" + LCDHEIGHT);
        }
        clear();
        int maxX = 0;
        int maxY = 0;
        int minX = LCDWIDTH - 1;
        int minY = LCDHEIGHT - 1;
        for (int x = 0; x < buffer.length; x++) {
            byte[] sub = buffer[x];
            for (int y = 0; y < sub.length; y++) {
                if (buffer[x][y] > 0) {
                    int page = y / 8;
                    int i = x + page * LCDWIDTH;
                    int modY = y % 8;
                    this.buffer[i] |= 1 << modY;
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                }
            }
        }
        if (allowPartialUpdates) updateBoundingBox(minX, minY, maxX, maxY);
        else updateBoundingBox(0, 0, LCDWIDTH - 1, LCDHEIGHT - 1);
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
        int len = min(this.buffer.length, buffer.length);
        for (int i = 0; i < len; i++) this.buffer[i] = buffer[i];
    }

    public void updateBoundingBox(int xmin, int ymin, int xmax, int ymax) {
//        xUpdateMin = min(xUpdateMin, xmin);
//        xUpdateMax = max(xUpdateMax, xmax);
//        yUpdateMin = min(yUpdateMin, ymin);
//        yUpdateMax = max(yUpdateMax, ymax);
        xUpdateMin = xmin;
        xUpdateMax = xmax;
        yUpdateMin = ymin;
        yUpdateMax = ymax;

        if (xUpdateMin < 0) xUpdateMin = 0;
        if (yUpdateMin < 0) yUpdateMin = 0;
        if (xUpdateMax >= LCDWIDTH) xUpdateMax = LCDWIDTH - 1;
        if (yUpdateMax >= LCDHEIGHT) yUpdateMax = LCDHEIGHT - 1;
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
