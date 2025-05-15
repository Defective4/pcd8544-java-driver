package io.github.defective4.pi.pcd8544;

import java.util.Objects;
import java.util.concurrent.locks.LockSupport;

import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.spi.Spi;

public class SPIDevice {
    private final DigitalOutput clockPin, dataPin, chipSelectPin;

    private final Spi spi;
    private final Object writeLock = new Object();

    public SPIDevice(DigitalOutput clockPin, DigitalOutput dataPin, DigitalOutput chipSelectPin) {
        Objects.requireNonNull(clockPin);
        Objects.requireNonNull(dataPin);
        Objects.requireNonNull(chipSelectPin);
        this.clockPin = clockPin;
        this.dataPin = dataPin;
        this.chipSelectPin = chipSelectPin;
        spi = null;
    }

    public SPIDevice(Spi spi) {
        Objects.requireNonNull(spi);
        clockPin = null;
        dataPin = null;
        chipSelectPin = null;
        this.spi = spi;
    }

    protected void delay(long nanos) {
        LockSupport.parkNanos(nanos);
    }

    protected void initSPI() {
        synchronized (writeLock) {
            if (chipSelectPin != null) chipSelectPin.high();
        }
    }

    protected void writeSPI(int data) {
        synchronized (writeLock) {
            if (spi == null) {
                chipSelectPin.low();
                for (int i = 0; i < 8; i++) {
                    boolean state = (data >> 7 - i & 0x01) > 0;
                    dataPin.setState(state);
                    clockPin.high();
                    delay(1);
                    clockPin.low();
                }
                dataPin.low();
                chipSelectPin.high();
            } else {
                spi.write(data);
            }
        }
    }
}
