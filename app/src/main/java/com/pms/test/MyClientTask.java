package com.pms.test;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClientTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "MyClientTask";
    String dstAddress;
    int dstPort;
    String response = "";
    String msgToServer;

    public MyClientTask(String addr, int port, String msgTo) {
        dstAddress = addr;
        dstPort = port;
        msgToServer = msgTo;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;

        try {
            socket = new Socket(dstAddress, dstPort);
            dataOutputStream = new DataOutputStream(
                    socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());

            if (msgToServer != null) {
                dataOutputStream.writeUTF(msgToServer);
            }

            response = dataInputStream.readUTF();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.e(TAG, "onPostExecute: " + response);
        super.onPostExecute(result);
    }

}
