package com.pms;

import android.util.Log;

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

public class SendAndReceiveClientThread implements Runnable {

    private static final String TAG = "ClientThread";
    private static final String SERVER_IP = "192.168.43.35";
    private static final int SERVERPORT = 11003;
    private Socket socket;


    @Override
    public void run() {

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            socket = new Socket(serverAddr, SERVERPORT);

//            while (!Thread.currentThread().isInterrupted()) {
//
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = input.readLine();
////                if (null == message || "Disconnect".contentEquals(message)) {
////                    Thread.interrupted();
////                    message = "Server Disconnected.";
//////                    showMessage(message, Color.RED);
////                    break;
////                }
//
//                StringBuilder stringbuilder = new StringBuilder();
//                String line;
//                while ((line = input.readLine()) != null) {
//                    stringbuilder.append(line);
//                    Log.i("line", "line.line" + line);
//                }
//
//
//                String message = stringbuilder.toString();
            Log.e(TAG, "run:TCP Message : " + message);
//

////                showMessage("Server: " + message, clientTextColor);
//            }

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
