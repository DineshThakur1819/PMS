package com.pms;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;

public class TCPConnectClientThread implements Runnable {

    private static final String TAG = "ClientThread";
    private static final String SERVER_IP = "192.168.43.186";
    private static final int SERVERPORT = 11000;
    private Socket socket;
    private BufferedReader input;

    private byte[] msg;

    public TCPConnectClientThread(byte[] msg) {

        this.msg = msg;

    }

    @Override
    public void run() {

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            socket = new Socket(serverAddr, SERVERPORT);
            Log.e(TAG, "TCP: connected " + SERVER_IP + ":" + SERVERPORT);
            Log.e(TAG, "TCP: " + socket.isConnected());

            try {
                if (null != socket) {
                    OutputStream socketWriter = socket.getOutputStream();
                    System.out.println("Start sending");

                    socketWriter.write(msg);
                    socketWriter.flush();
                    System.out.println("Send completed, start receiving information");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (!Thread.currentThread().isInterrupted()) {

//                this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String message = input.readLine();
//                Log.e(TAG, "TCP: message : " + message);

//                showMessage("Server: " + message, clientTextColor);
            }

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    void sendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != socket) {
                        PrintWriter out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),
                                true);
                        out.println(message);
//                        out.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void sendMessage(final byte[] message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != socket) {
                        OutputStream socketWriter = socket.getOutputStream();
                        System.out.println("Start sending");

                        socketWriter.write(message);
                        socketWriter.flush();
                        System.out.println("Send completed, start receiving information");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
