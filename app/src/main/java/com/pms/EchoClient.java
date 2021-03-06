package com.pms;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoClient {
    private DatagramSocket socket;
    private InetAddress address;
    private static final String TAG = "EchoClient";

    private byte[] buf;

    public EchoClient() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            address = InetAddress.getByName("192.168.43.186");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String sendEcho(String msg) {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 11000);
        try {
            socket.send(packet);
        } catch (IOException e) {
            Log.e(TAG, "sendEcho: " + e.getLocalizedMessage());
            e.printStackTrace();

        }
        packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            Log.e(TAG, "sendEcho: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        String received = new String(
                packet.getData(), 0, packet.getLength());
        Log.e(TAG, "sendEcho: " + received);
        return received;
    }

    public void close() {
        socket.close();
    }
}