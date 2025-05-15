package io.github.defective4.pi.pcd8544;

import java.util.concurrent.locks.LockSupport;

import com.pi4j.io.gpio.digital.DigitalOutput;

public class SPIDevice {
    private final DigitalOutput clockPin, dataPin, chipSelectPin;

    public SPIDevice(DigitalOutput clockPin, DigitalOutput dataPin, DigitalOutput chipSelectPin) {
        this.clockPin = clockPin;
        this.dataPin = dataPin;
        this.chipSelectPin = chipSelectPin;
    }

    protected void delay(long nanos) {
        LockSupport.parkNanos(nanos);
    }

    protected void initSPI() {
        chipSelectPin.high();

    }

    protected void writeSPI(int data) {
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
    }
}
