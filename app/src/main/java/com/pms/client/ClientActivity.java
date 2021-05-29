package com.pms.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pms.EchoServer;
import com.pms.MyTcpClient;
import com.pms.R;
import com.pms.databinding.ActivityClientBinding;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ClientActivity extends AppCompatActivity {

    private static final String TAG = "ClientActivity";
    private ActivityClientBinding binding;

    Thread Thread1 = null;

    String SERVER_IP;
    int SERVER_PORT;
    private EchoServer mEchoServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_client);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client);
        mEchoServer = new EchoServer();

        mEchoServer.start();

        binding.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvMessages.setText("");
                SERVER_IP = binding.etIP.getText().toString().trim();
                SERVER_PORT = Integer.parseInt(binding.etPort.getText().toString().trim());
                Thread1 = new Thread(new Thread1());
                Thread1.start();
            }
        });
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String message = "Hijsdjsjdfsnnznx zjxcjznxncz zjxcnznxcnxz zxcnzxncnzx  zxcznnxuwewe zxczxncnz zx zxcmzmkaks kzxkcz";//binding.etMessage.getText().toString().trim();
//                if (!message.isEmpty()) {
//                    new Thread(new Thread3(message)).start();
//                }


                new MyTcpClient().start();


//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Connect("192.168.43.186", 11000, 50000, false);
//                    }
//                }).start();

            }
        });

    }


    Socket socket;

    private void Connect(String host, int port, int timeout, boolean shouldSleep) {
        try {


            while (true) {
                socket = new Socket();
                // Connect to socket by host, port, and with specified timeout.
                socket.connect(new InetSocketAddress(InetAddress.getByName(host), port), timeout);

                // Read input stream from server and output said message.
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                Log.e("[FROM Server] ", reader.readLine());

                // Await user input via System.in (standard input stream).
                BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
                // Save input message.
                String message = "C "; //userInputBR.readLine();

                // Send message to server via output stream.
                writer.println(message);

                // If message is 'quit' or 'exit', intentionally disconnect.
                if ("quit".equalsIgnoreCase(message) || "exit".equalsIgnoreCase(message)) {
                    Log.e("DISCONNECTING", "Dis");
                    socket.close();
                    break;
                }

                Log.e("[TO Server] ", reader.readLine());
            }
        } catch (SocketException exception) {
            // Output expected SocketExceptions.
            Log.e("exception", exception.getLocalizedMessage());
        } catch (Exception exception) {
            // Output unexpected Exceptions.
            Log.e("exception.", "" + false);
        }
    }


    private PrintWriter output;
    private BufferedReader input;

    class Thread1 implements Runnable {
        public void run() {
            Socket socket;
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);

                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.tvMessages.setText("Connected\n");
                    }
                });
                new Thread(new Thread2()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "run: " + message);
                                binding.tvMessages.append("server: " + message + "\n");
                            }
                        });
                    } else {
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Thread3 implements Runnable {
        private String message;

        Thread3(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            output.write(message);
            output.flush();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: cl " + message);
                    binding.tvMessages.append("client: " + message + "\n");
//                    binding.etMessage.setText("");
                }
            });
        }
    }

}