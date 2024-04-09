package io.kalishak.comm.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import io.kalishak.Main;
import io.kalishak.RGBInstance;
import io.kalishak.comm.Communicator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class SerialCommunicator implements Communicator {
    private final boolean allowErrors;
    private final long maxDelay;

    private SerialPort comPorts[];
    private int port = -1;
    public SerialPort comPort;

    private boolean initialized = false;

    public SerialCommunicator(boolean allowErrors, long maxDelay) {
        this.allowErrors = allowErrors;
        this.maxDelay = maxDelay;
    }

    @Override
    public void run() {
        listSerialPorts();
        choosePort();
        open();
        // todo function
    }

    private void listSerialPorts() {
        this.comPorts = SerialPort.getCommPorts();

        if (comPorts.length > 0) {
            Main.LOGGER.info("Listing serial ports:");

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < this.comPorts.length; i++) {
                builder.append("Serialport[%s]: %s\n".formatted(i, this.comPorts[i].getDescriptivePortName()));
            }

            Main.LOGGER.info(builder.toString());

        } else {
            Main.LOGGER.warn("No available serial ports!");
        }
    }

    private void choosePort() {
        if (this.comPorts.length == 1) {
            this.port = 0;
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose interesting port and write it's index: ");
            if (scanner.hasNextInt()) {
                this.port = scanner.nextInt();
            }
        }
    }

    @Override
    public void open() {
        this.comPort = this.comPorts[port];
        Main.LOGGER.info("Attempting to open %s".formatted(this.comPort.getDescriptivePortName()));

        if(!this.comPort.openPort()) {
            Main.LOGGER.error("Failed to open COM port %s".formatted(this.comPort.getDescriptivePortName()));
            this.port = -1;
            return;
        }

        this.initialized = true;
        Main.LOGGER.info("Opened COM port %s".formatted(this.comPort.getDescriptivePortName()));

        this.comPort.setBaudRate(115200);
    }

    @Override
    public void close() {
        if (!this.comPort.closePort()) {
            Main.LOGGER.error("Failed to close COM port %s Maybe it was closed earlier?".formatted(this.comPort.getDescriptivePortName()));
        }

        this.initialized = false;
        Main.LOGGER.info("Closed COM port");
    }

    @Override
    public int send(String msg) {
        return this.comPort.writeBytes(msg.getBytes(), msg.length());
    }

    @Override
    public int send(int i) throws IOException {
        byte[] b = new byte[] { (byte) i };
        int l = this.comPort.writeBytes(b, 1);
        if (l == -1) {
            throw new IOException("Error writing " + i);
        }

        return l;
    }

    @Override
    public int send(double d) {
        byte[] b = new byte[8];
        long l = Double.doubleToLongBits(d);

        for (int i = 0; i < 8; i++) {
            b[i] = (byte) ((l >> ((7 - i) * 8)) & 0xFF);
        }

        return this.comPort.writeBytes(b, 8);
    }

    @Override
    public int send(float f) {
        byte[] b = new byte[4];
        int i = Float.floatToIntBits(f);

        for (int j = 0; j < 4; j++) {
            b[j] = (byte) (i >> (j * 8));
        }

        return this.comPort.writeBytes(b, 4);
    }

    @Override
    public int send(char c) {
        return this.comPort.writeBytes(new byte[(byte) c], 1);
    }

    @Override
    public int sendRGB(RGBInstance color) {
        float[] floats = color.asArray();
        int i = 0;

        for (int j = 0; i < 3; i++) {
            i = this.send(floats[i]);
        }

        return i;
    }

    @SuppressWarnings("unused")
    @Slf4j
    static class SerialDataListener implements SerialPortDataListener {
        private static final Logger RAW_MSG_LOGGER = LoggerFactory.getLogger("RawSerialLogger");

        private SerialPort serialPort;
        private StringBuilder rawMessage = new StringBuilder();
        private int errorCounter = 0;
        private static final int MAX_ERROR_COUNT = 3;

        public SerialDataListener(SerialPort serialPort) {
            this.serialPort = serialPort;
        }

        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                return;
            }

            /*try {
                byte[] buffer = new byte[this.serialPort.bytesAvailable()];

                this.serialPort.readBytes(buffer, buffer.length);
            }*/
        }
    }
}
