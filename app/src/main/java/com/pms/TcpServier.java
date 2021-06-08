package com.pms;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpServier extends Thread {

    private static final String TAG = "GreetServer";

    private ServerSocket serverSocket;
    private Socket socket;
    private int port;
    private boolean running;
    private BufferedReader in;

    @Override
    public void run() {
        super.run();
        running = true;

        while (true) {
            try {
                Log.i("conex", "Conectou");

                socket = serverSocket.accept();
//                mHandler.obtainMessage(3,socket).sendToTarget();
//                out = new PrintWriter(socket.getOutputStream(), true);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = in.readLine();

                Log.e(TAG, "TCP listening  Message : " + message);


            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public TcpServier(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(port));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopThread() {


        try {
            in.close();
//            out.close();
            socket.close();
            serverSocket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
