package com.pms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpServerService extends Service {

    private ServerSocket serverSocket;
    private AtomicBoolean working = new AtomicBoolean(true);

    private Runnable runnable = () -> {

        Socket socket = null;

        try {
            serverSocket = new ServerSocket(11000);

            while (working.get()) {

                if (serverSocket != null) {

                    socket = serverSocket.accept();

                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                    new TcpClientHandler(inputStream, outputStream).start();

                } else {
                    Log.e(TAG, "Couldn't create ServerSocket!");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException | NullPointerException ex) {
                ex.printStackTrace();
            }
        }


    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(runnable).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        working.set(false);
    }

    private void startForeGround(){


    }

    private static final String TAG = "TcpServerService";

    class TcpClientHandler extends Thread {

        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        TcpClientHandler(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
            this.dataInputStream = dataInputStream;
            this.dataOutputStream = dataOutputStream;
        }

        @Override
        public void run() {
            super.run();

            while (true) {

                try {
                    if (dataInputStream.available() > 0) {
                        Log.i(TAG, "Received: " + dataInputStream.readUTF());
                        dataOutputStream.writeUTF("Hello Client");
                        sleep(2000L);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        dataInputStream.close();
                        dataOutputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    try {
                        dataInputStream.close();
                        dataOutputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        }
    }
}
