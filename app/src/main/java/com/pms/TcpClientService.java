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

import com.pms.pmsmodel.CommonUtils;
import com.pms.pmsmodel.PMSConstants;
import com.pms.pmsmodel.PMSField;
import com.pms.pmsmodel.PMSMessage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpClientService extends Service {
    private static final String SERVER_IP = "192.168.43.186";
    private static final int SERVERPORT = 11001;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String message = " Hello";
    private AtomicBoolean working = new AtomicBoolean(true);

    private Runnable runnable = () -> {

        Socket socket = null;

        PMSMessage pmsMessage = new PMSMessage(PMSConstants.PMSMessageType.TERMINAL_TEST, PMSConstants.RequestResponseType.RESPONSE, "00", false);

        /*e.setField("02", m_active ? "ECR COMMS - OK" : "TERMINAL BUSY");
        e.setField("03", date);
        e.setField("04", time);

        string mid = m_merchantId, tid = m_terminalId;
        e.setField("EA", StringUtility::padRight(tid, 8, ' ') + StringUtility::padRight(mid, 15, ' '));
        e.setField("VR", m_version);*/

//        pmsMessage.addField(new PMSField("02", "ECR COMMS - OK"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        String strDate = sdf.format(date);

        sdf = new SimpleDateFormat("HHMMss");
        String strTime = sdf.format(date);

        pmsMessage.addField(new PMSField("02", "ECR COMMS - OK"));
        pmsMessage.addField(new PMSField("03", strDate));
        pmsMessage.addField(new PMSField("04", strTime));

        String TID = "SE200A";
        String MID = "33123433";

        String DID = CommonUtils.rightPadding(TID, ' ', 8) + CommonUtils.rightPadding(MID, ' ', 15);

        pmsMessage.addField(new PMSField("EA", DID));

        String version = "EVO_12.0.0.3";
        pmsMessage.addField(new PMSField("VR", version));

        String ipAddress = "192.168.43.5";
        String ipPOrt = "11001";
        pmsMessage.addField(new PMSField("IA", ipAddress));
        pmsMessage.addField(new PMSField("IP", ipPOrt));

//        byte[] m = pmsMessage.getMessage();
//
//        String c = String.valueOf(CommonUtils.calculateLrc(m, m.length));

        String msg = CommonUtils.byteArrayToHexString(pmsMessage.getMessage());

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            socket = new Socket(serverAddr, SERVERPORT);

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

//            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            while (working.get()) {

                Log.e(TAG, "Msg : " + msg);
                dataOutputStream.writeUTF(msg);

//                String message = input.readLine();
//                Log.e(TAG, ": " +message);
//                Thread.sleep(2000L);


            }

        } catch (IOException e) {
            e.printStackTrace();

            try {
                socket.close();

            } catch (IOException | NullPointerException ex) {
                ex.printStackTrace();
            }
            try {
                dataInputStream.close();
                dataOutputStream.close();
            } catch (IOException ex) {
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

    private void startForeGround() {

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

//    class TcpClientHandler extends Thread {
//
//        private DataInputStream dataInputStream;
//        private DataOutputStream dataOutputStream;
//
//        TcpClientHandler(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
//            this.dataInputStream = dataInputStream;
//            this.dataOutputStream = dataOutputStream;
//        }
//
//        @Override
//        public void run() {
//            super.run();
//
//            while (true) {
//
//                try {
//                    if (dataInputStream.available() > 0) {
//                        Log.i(TAG, "Received: " + dataInputStream.readUTF());
//                        dataOutputStream.writeUTF("Hello Client");
//                        sleep(2000L);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    try {
//                        dataInputStream.close();
//                        dataOutputStream.close();
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    try {
//                        dataInputStream.close();
//                        dataOutputStream.close();
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//
//            }
//
//        }
//    }
}
