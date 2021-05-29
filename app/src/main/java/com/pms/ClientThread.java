package com.pms;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientThread implements Runnable {

    private static final String TAG = "ClientThread";
    private static final String SERVER_IP ="192.168.43.186" ;
    private static final int SERVERPORT =11000 ;
    private Socket socket;
    private BufferedReader input;

    @Override
    public void run() {

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            socket = new Socket(serverAddr, SERVERPORT);

            while (!Thread.currentThread().isInterrupted()) {

                this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = input.readLine();
                if (null == message || "Disconnect".contentEquals(message)) {
                    Thread.interrupted();
                    message = "Server Disconnected.";
//                    showMessage(message, Color.RED);
                    break;
                }

                Log.e(TAG, "run:cli "+message );

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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
