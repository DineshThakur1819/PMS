package com.pms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

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
            serverSocket = new ServerSocket(11002);

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
startForeGround();
        new Thread(runnable).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        working.set(false);
    }

    private void startForeGround(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "packageName";
            String channelName = "Tcp Client Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(chan);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Tcp Client is running in background")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        } else {
            startForeground(1, new Notification());
        }
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
