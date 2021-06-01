package com.pms;

import android.util.Log;

import com.pms.pmsmodel.PMSUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ListenUDP extends Thread {

    private static final String TAG = "ListenUDP";

    private DatagramSocket socket;
    private DatagramPacket packet;

    public ListenUDP() {

        try {
            socket = new DatagramSocket(PMSUtil.DEFAULT_UDP_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        super.run();

        try {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received
                    = new String(packet.getData(), 0, packet.getLength());
            Log.e(TAG, "run: " + received);

            if (socket != null) socket.close();

        } catch (SocketException e) {
            e.printStackTrace();
            if (socket != null) socket.close();

        } catch (IOException exception) {
            exception.printStackTrace();
            if (socket != null) socket.close();
        }

    }

    public interface UDPListener {

        void onUdp(String ip, String port);
    }
}
