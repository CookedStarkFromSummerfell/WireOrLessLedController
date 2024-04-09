package io.kalishak.comm;

import io.kalishak.RGBInstance;
import org.w3c.dom.css.RGBColor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Communicator {
    byte START_MESSAGE = 0x1;
    byte END_MESSAGE = 0x3;

    byte ERROR_READING = 0x2;
    byte ERROR_WRITING = 0x4;

    void run();
    void open() throws IOException;
    void close() throws IOException;

    int send(String msg);
    int send(int i) throws IOException;
    int send(double d);
    int send(float f);
    int send(char c);
    int sendRGB(RGBInstance color);

    enum Type {
        SERIAL,
        WIFI,
        NONE
    }

    @FunctionalInterface
    interface Factory<C extends Communicator> {
        C create(boolean allowErrors, long maxDelay);
    }
}
