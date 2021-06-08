package com.pms.test;

import android.util.Log;

import com.pms.pmsmodel.CommonUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class UdpListen extends Thread {

    private static final String TAG = "UdpListen";

    @Override
    public void run() {
        super.run();

        try {
            // Step 1 : Create a socket to listen at port 1234
            DatagramSocket ds = null;

            ds = new DatagramSocket(11001);

            byte[] receive = new byte[8192];

            DatagramPacket DpReceive = null;
            while (true) {

                // Step 2 : create a DatgramPacket to receive the data.
                DpReceive = new DatagramPacket(receive, receive.length);

                // Step 3 : revieve the data in byte buffer.
                ds.receive(DpReceive);

                String msg = data(receive).toString();
//                System.out.println("Client:-" + msg);

                new MyClientTask("192.168.43.186", 11000, null);

                // Exit the server if the client sends "bye"
                if (data(receive).toString().equals("bye")) {
                    System.out.println("Client sent bye.....EXITING");
                    break;
                }

                // Clear the buffer after every message.
                receive = new byte[8192];
            }
        } catch (
                SocketException e) {
            e.printStackTrace();
        } catch (
                IOException exception) {
            exception.printStackTrace();
        }

    }

    // A utility method to convert the byte array
    // data into a string representation.
    public static StringBuilder data(byte[] a) {

        Log.e(TAG, "data: " + Arrays.toString(a));

        Log.e(TAG, "data: Hex " + CommonUtils.byteArrayToHexString(a));

        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}
