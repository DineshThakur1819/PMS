package com.pms;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ClientSendAndListen implements Runnable {
    private static final String TAG = "ClientSendAndListen";

    private String ip;

    public ClientSendAndListen(String ip) {
        this.ip = "192.168.43.186";//ip;

        Log.e(TAG, "ClientSendAndListen: Ip " + this.ip);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void run() {
        boolean run = true;
        try {
            DatagramSocket udpSocket = new DatagramSocket(11000);
            InetAddress serverAddr = InetAddress.getByName(ip);
            byte[] buf = ("FILES").getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 11000);
            udpSocket.send(packet);
            while (run) {
                try {
                    byte[] message = new byte[8000];
                    packet = new DatagramPacket(message, message.length);
                    Log.i("UDP client: ", "about to wait to receive");
                    udpSocket.setSoTimeout(10000);
                    udpSocket.receive(packet);
                    String text = new String(message, 0, packet.getLength());
                    Log.d("Received text", text);
                } catch (SocketTimeoutException e) {
                    Log.e("Timeout Exception", "UDP Connection:", e);
                    run = false;
                    udpSocket.close();
                } catch (IOException e) {
                    Log.e(" UDP client has IOException", "error: ", e);
                    run = false;
                    udpSocket.close();
                }
            }
        } catch (SocketException e) {
            Log.e("Socket Open:", "Error:", e);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}