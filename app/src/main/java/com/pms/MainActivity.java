package com.pms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import com.pms.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private EchoServer mEchoServer;
    private TCPClient mTcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
            @Override
            public void messageReceived(String message) {

                binding.display.setText("Rec : " + message);

            }
        }, getApplication(), ip);

        mEchoServer = new EchoServer();

        binding.listenUdp.setOnClickListener(v -> mEchoServer.start());

        ClientThread clientThread = new ClientThread();

        binding.sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(clientThread).start();


//                new EchoServerAnother().start();

//                new Send().execute();
//                new ConnectTask().execute();


//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        //1. Create ServerSocket
//                        ServerSocket serverSocket = null;
//                        try {
//                            serverSocket = new ServerSocket(11003);
//
//                            //2. monitoring
//                            Socket socket = serverSocket.accept();
//                            System.out.println("server start listen");
//                            //3. input stream
//                            InputStream is = socket.getInputStream();
//                            InputStreamReader reader = new InputStreamReader(is);
//                            BufferedReader br = new BufferedReader(reader);
//                            String content = null;
//                            StringBuffer sb = new StringBuffer();
//                            while ((content = br.readLine()) != null) {
//                                sb.append(content);
//                            }
//
//                            Log.e(TAG, "run: " + sb.toString());
//                            System.out.println("server receiver: " + sb.toString());
//
//                            socket.shutdownInput();
//
//                            br.close();
//                            reader.close();
//                            is.close();
//
//                            socket.close();
//                            serverSocket.close();
//
//                        } catch (IOException exception) {
//                            exception.printStackTrace();
//                        }
//
//                    }
//                }).start();


//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        InetAddress host = null;
//                        try {
//                            host = InetAddress.getLocalHost();
//
//                            Socket socket = null;
//                            ObjectOutputStream oos = null;
//                            ObjectInputStream ois = null;
//                            for (int i = 0; i < 5; i++) {
//                                //establish socket connection to server
//                                socket = new Socket("192.168.43.186", 11000);
//                                //write to socket using ObjectOutputStream
//                                oos = new ObjectOutputStream(socket.getOutputStream());
//                                System.out.println("Sending request to Socket Server");
//                                if (i == 4) oos.writeObject("exit");
//                                else oos.writeObject("c-N");
//                                //read the server response message
//                                ois = new ObjectInputStream(socket.getInputStream());
//                                String message = (String) ois.readObject();
//                                System.out.println("Message: " + message);
//                                //close resources
//                                ois.close();
//                                oos.close();
//                                Thread.sleep(100);
//
//                            }
//
//                        } catch (UnknownHostException e) {
//                            e.printStackTrace();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (IOException exception) {
//                            exception.printStackTrace();
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }).start();


            }
        });

        binding.dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Send().execute();

//                if (mTcpClient != null) mTcpClient.sendMessage("hello");


//                new Thread(new ClientSendAndListen(ip)).start();

//                new Thread(new ClientThread()).start();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clientThread.sendMessage("Que");
                    }
                });


//                new Thread(new ClientSend()).start();

            }
        });


    }

    public class ConnectTask extends AsyncTask<String, String, TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            Log.d(TAG, "doInBackground");

//            //we create a TCPClient object and
//            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
//                @Override
//                //here the messageReceived method is implemented
//                public void messageReceived(String message) {
//
//                    //this method calls the onProgressUpdate
//                    publishProgress(message);
//
//                }
//            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate");
            binding.display.setText(values[0]);
//            View view = adapter.getChildView(0, 0, false, null, null);
//            TextView text = (TextView) view.findViewById(R.id.betChildOdd);
//            child2.get(0).get(0).put("OLD", text.getText().toString());
//            child2.get(0).get(0).put(CONVERTED_ODDS, values[0].toString());
//            child2.get(0).get(0).put("CHANGE", "TRUE");
//            adapter.notifyDataSetChanged();
        }
    }


    class UdpSend extends AsyncTask<Void, Void, Void> {


        private boolean aplicationActive = true;

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                ServerSocket ss = new ServerSocket(11000);
                Socket s = ss.accept();//establishes connection
                DataInputStream dis = new DataInputStream(s.getInputStream());
                String str = (String) dis.readUTF();
                System.out.println("message= " + str);
                ss.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            try {
//                Socket s = new Socket("localhost", 11000);
                Socket s = new Socket("127.0.0.1", 11000);
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                dout.writeUTF("Hello Server");
                dout.flush();
                dout.close();
                s.close();
            } catch (Exception e) {
                System.out.println(e);
            }


//            DatagramSocket socketUDP;
//
//            try
//            {
//                socketUDP = new DatagramSocket(11000);
//                socketUDP.setSoTimeout(5000);
//
//                // set it to true if you want to receive broadcast packets
//                socketUDP.setBroadcast(false);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//                return null;
//            }
//
//            byte[] buff = new byte[512];
//            DatagramPacket packet = new DatagramPacket(buff, buff.length);
//            boolean asyncTask_UDP_is_running;
//            try
//            {
//                asyncTask_UDP_is_running=true;
//                // Keep running until application gets inactive
//                while (aplicationActive)
//                {
//                    try
//                    {
//                        socketUDP.receive(packet);
//                        android.util.Log.w("UDP", "got a packet");
//                    }
//                    catch (java.net.SocketTimeoutException ex)
//                    {
//                        // timeout
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                        return null;
//                    }
//                }
//            }
//            finally
//            {
//                asyncTask_UDP_is_running=false;
//            }
            return null;
        }
    }

    class Send extends AsyncTask<Void, Void, String> {

        private EchoClient echoClient;

        Send() {

        }

        @Override
        protected String doInBackground(Void... voids) {
            this.echoClient = new EchoClient();

            return echoClient.sendEcho("end");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "onPostExecute: " + s);
            if (echoClient != null) echoClient.close();

        }
    }
}