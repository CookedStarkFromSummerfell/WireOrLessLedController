package io.kalishak.comm.wifi;

import io.kalishak.RGBInstance;
import io.kalishak.comm.Communicator;
import lombok.Setter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WiFiCommunicator implements Communicator {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    @Setter
    public int port;

    public WiFiCommunicator(boolean allowErrors, long maxDelay) {

    }

    @Override
    public void run() {

    }

    @Override
    public void open() throws IOException {
        this.serverSocket = new ServerSocket(this.port);
        this.clientSocket = this.serverSocket.accept();
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
    }

    @Override
    public void close() throws IOException {
        this.in.close();
        this.out.close();
        this.clientSocket.close();
        this.serverSocket.close();
    }

    @Override
    public int send(String msg) {
        this.out.write(msg);
        return msg.length();
    }

    @Override
    public int send(int i) throws IOException {
        this.out.write(i);
        return i;
    }

    @Override
    public int send(double d) {
        this.out.write(String.valueOf(d));
        return String.valueOf(d).length();
    }

    @Override
    public int send(float f) {
        this.out.write(String.valueOf(f));
        return String.valueOf(f).length();
    }

    @Override
    public int send(char c) {
        this.out.write(c);
        return c;
    }

    @Override
    public int sendRGB(RGBInstance color) {
        for (float f : color.asArray()) {
            this.send(f);
        }

        return 3;
    }
}
