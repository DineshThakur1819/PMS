package com.pms;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GreetServer extends Thread {
    private static final String TAG = "GreetServer";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;

    @Override
    public void run() {
        super.run();

        try {
            serverSocket = new ServerSocket(port);
            Log.e(TAG, "run: server listening" );
            clientSocket = serverSocket.accept();

            out = new PrintWriter(clientSocket.getOutputStream(), true);

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String greeting = in.readLine();

            Log.e(TAG, "start: " + greeting);

//            if ("hello server".equals(greeting)) {
//                out.println("hello client");
//            } else {
//                out.println("unrecognised greeting");
//            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public GreetServer(int port) {
        this.port = port;
    }

    public void stopThread() {


        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
