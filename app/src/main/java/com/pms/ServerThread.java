package com.pms;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerThread extends Thread {

    private boolean needsToRun;
    private DatagramSocket socket;

    public ServerThread() {
        super();
        needsToRun = true;
        try {
            socket = new DatagramSocket(4446);
        }
        catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(needsToRun) {
            byte[] buf = new byte[265];
            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            try {
                socket.receive(packet);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            String data = new String(packet.getData(),0,packet.getLength());
            if(data != null)
                System.out.println(data);
        }
        socket.close();
    }
}