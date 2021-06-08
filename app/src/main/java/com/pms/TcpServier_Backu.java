package com.pms;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServier_Backu extends Thread {

    private static final String TAG = "GreetServer";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;
    private boolean running;

    @Override
    public void run() {
        super.run();
        running = true;
        try {
            serverSocket = new ServerSocket(port);
            Log.e(TAG, "run: TCP is listening");
            clientSocket = serverSocket.accept();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        while (running) {
            try {


                out = new PrintWriter(clientSocket.getOutputStream(), true);

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = in.readLine();

                Log.e(TAG, "TCP Message : " + message);

//                if (message.equals("end")) {
//                    running = false;
//                    continue;
//                }
//                if ("hello server".equals(message)) {
//                    out.println("hello client");
//                } else {
//                    out.println("unrecognised greeting");
//                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public TcpServier_Backu(int port) {
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
