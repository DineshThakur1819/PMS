package com.pms;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerAnother extends Thread {

    private static final String TAG = "TCpEchoServer";
    private static final int PORT = 11002;
    private ServerSocket socket;
    private Socket s;
    private boolean running;
    private byte[] buf = new byte[256];

    public EchoServerAnother() {
        try {
            socket = new ServerSocket(PORT);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void run() {
        running = true;

        while (running) {

            try {
                s = socket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                DataInputStream din = new DataInputStream(s.getInputStream());
                String received
                        = new String(din.readUTF());

                Log.e(TAG, "run: TCP " + received);

            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
    }
}