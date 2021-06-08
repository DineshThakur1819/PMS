package com.pms;

import android.util.Log;

import com.pms.pmsmodel.PMSUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoServer extends Thread {

    private static final String TAG = "EchoServer";
    private static final int PORT = 11001;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public EchoServer() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received
                    = new String(packet.getData(), 0, packet.getLength());

            Log.e(TAG, "run: " + received);
            SplitStringAndSave(received);


            if (received.equals("end")) {
                running = false;
                continue;
            }
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    private void SplitStringAndSave(String received) {

        String HICAPSConnect, laptop, ipPort;
        String[] separated = received.split("\\+");

        HICAPSConnect = separated[1];
        laptop = separated[2];
        ipPort = separated[3];


        PMSUtil.getInstance().saveValue(PMSUtil.HICAPS_CONNECT, HICAPSConnect);
        PMSUtil.getInstance().saveValue(PMSUtil.LAPTOP, laptop);
        PMSUtil.getInstance().saveValue(PMSUtil.IP_PORT, ipPort);




//        Log.e(TAG, "Separated String " + separated.toString());
//        for (String w : separated) {
//            System.out.println("Separated String " + w);
//
//
//        }

    }
}