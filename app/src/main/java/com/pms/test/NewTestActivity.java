package com.pms.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.pms.ClientThread;
import com.pms.R;
import com.pms.databinding.ActivityNewTestBinding;
import com.pms.pmsmodel.PMSUtil;

public class NewTestActivity extends AppCompatActivity {

    ActivityNewTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_test);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_test);

        new UdpListen().start();


        byte[] byteMsg = PMSUtil.getPMSMessage(PMSUtil.getLocalIpAddress());

        ClientThreadConnect clientThread = new ClientThreadConnect(byteMsg);
        new Thread(clientThread).start();

        ClientThread msgThread = new ClientThread(byteMsg);

        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                new Thread(msgThread).start();
            }
        }, 2000);

//        new Thread(msgThread).start();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                byte[] byteMsg = PMSUtil.getPMSMessage(PMSUtil.getLocalIpAddress());

                msgThread.sendMessage(byteMsg);

            }
        });


    }
}