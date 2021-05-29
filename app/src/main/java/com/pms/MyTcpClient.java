package com.pms;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MyTcpClient extends Thread {

    private static final String TAG = "MyTcpClient";
    private PrintWriter out;
    private BufferedReader in;
    private String incomingMessage;

    /**
     * Public method for sending the message via OutputStream object.
     *
     * @param message Message passed as an argument and sent via OutputStream object.
     */
    public void sendMessage(String message) {

        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
//            mHandler.sendEmptyMessageDelayed(MainActivity.SENDING, 1000);
            Log.d(TAG, "Sent Message: " + message);

        }
    }

    @Override
    public void run() {
        super.run();

        try {
            // Creating InetAddress object from ipNumber passed via constructor from IpGetter class.
            InetAddress serverAddress = InetAddress.getByName("192.168.43.186");

            Log.d(TAG, "Connecting...");

            /**
             * Sending empty message with static int value from MainActivity
             * to update UI ( 'Connecting...' ).
             *
             * @see com.example.turnmeoff.MainActivity.CONNECTING
             */
//            mHandler.sendEmptyMessageDelayed(MainActivity.CONNECTING,1000);

            /**
             * Here the socket is created with hardcoded port.
             * Also the port is given in IpGetter class.
             *
             * @see com.example.turnmeoff.IpGetter
             */
            Socket socket = new Socket(serverAddress, 11000);


            try {

                // Create PrintWriter object for sending messages to server.
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //Create BufferedReader object for receiving messages from server.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Log.d(TAG, "In/Out created");

                //Sending message with command specified by AsyncTask
                this.sendMessage("600000000011D10005002 ECR COMMS - OK03 09062404 083649EASE200A  33123433       IA  192.168.43.186  IP 11002");

                //
//                mHandler.sendEmptyMessageDelayedeDelayed(MainActivity.SENDING,2000);

                //Listen for the incoming messages while mRun = true
                while (true) {
                    incomingMessage = in.readLine();
                    if (incomingMessage != null) {

                        Log.e(TAG, "run: " + incomingMessage);

                        /**
                         * Incoming message is passed to MessageCallback object.
                         * Next it is retrieved by AsyncTask and passed to onPublishProgress method.
                         *
                         */
//                        listener.callbackMessageReceiver(incomingMessage);

                    }
                    incomingMessage = null;

                }

//                Log.d(TAG, "Received Message: " + incomingMessage);

            } catch (Exception e) {

                Log.d(TAG, "Error", e);
//                mHandler.sendEmptyMessageDelayed(MainActivity.ERROR, 2000);

            } finally {

                out.flush();
                out.close();
                in.close();
                socket.close();
//                mHandler.sendEmptyMessageDelayed(MainActivity.SENT, 3000);
                Log.d(TAG, "Socket Closed");
            }

        } catch (Exception e) {

            Log.d(TAG, "Error", e);
//            mHandler.sendEmptyMessageDelayed(MainActivity.ERROR, 2000);

        }

    }
}
