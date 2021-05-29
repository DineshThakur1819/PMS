package com.pms;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

    //    public static final String SERVER_IP = "127.0.0.1"; // your computer IP
    public static String SERVER_IP = "localhost"; // your computer IP
    // address
    public static final int SERVER_PORT = 11004;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    private String uid;

    private Application mApplication;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages
     * received from server
     */
    public TCPClient(OnMessageReceived listener, Application mApplication, String ip) {
        mMessageListener = listener;
        this.mApplication = mApplication;
        SERVER_IP = "192.168.43.186";
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null && !mBufferOut.checkError()) {
                    mBufferOut.println(message);
                    mBufferOut.flush();
                }
            }
        }).start();


    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        // send mesage that we are closing the connection
        TelephonyManager tManager = (TelephonyManager) mApplication.getSystemService(Context.TELEPHONY_SERVICE);
        uid = "12";//tManager.getDeviceId();
        sendMessage(Constants.CLOSED_CONNECTION + "id: " + uid);

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {

        mRun = true;

        try {
            // here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.e("TCP Client", "C: Connecting...");

            // create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVER_PORT);

            try {

                // sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);

                // receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                // send login name
                TelephonyManager tManager = (TelephonyManager) mApplication.getSystemService(Context.TELEPHONY_SERVICE);
                uid = "12";//tManager.getDeviceId();
                sendMessage(Constants.LOGIN_NAME + "id: " + uid);

                // in this while the client listens for the messages sent by the
                // server
                while (mRun) {

                    mServerMessage = mBufferIn.readLine();

                    if (mServerMessage != null && mMessageListener != null) {
                        // call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '"
                        + mServerMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                // the socket must be closed. It is not possible to reconnect to
                // this socket
                // after it is closed, which means a new socket instance has to
                // be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    // Declare the interface. The method messageReceived(String message) will
// must be implemented in the MyActivity
// class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}