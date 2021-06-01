package com.pms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import com.pms.databinding.ActivityRequestStructureBinding;
import com.pms.pmsmodel.CommonUtils;
import com.pms.pmsmodel.PMSConstants;
import com.pms.pmsmodel.PMSField;
import com.pms.pmsmodel.PMSMessage;
import com.pms.pmsmodel.PMSUtil;

import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RequestStructureActivity extends AppCompatActivity {

    String ipAddress;

    private ActivityRequestStructureBinding binding;
    private static final String TAG = "RequestStructureActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_request_structure);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_structure);

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        PMSMessage pmsMessage = new PMSMessage(PMSConstants.PMSMessageType.TERMINAL_TEST, PMSConstants.RequestResponseType.RESPONSE, "00", false);

        /*e.setField("02", m_active ? "ECR COMMS - OK" : "TERMINAL BUSY");
        e.setField("03", date);
        e.setField("04", time);

        string mid = m_merchantId, tid = m_terminalId;
        e.setField("EA", StringUtility::padRight(tid, 8, ' ') + StringUtility::padRight(mid, 15, ' '));
        e.setField("VR", m_version);*/

//        pmsMessage.addField(new PMSField("02", "ECR COMMS - OK"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        String strDate = sdf.format(date);

        sdf = new SimpleDateFormat("HHMMss");
        String strTime = sdf.format(date);

        pmsMessage.addField(new PMSField("02", "ECR COMMS - OK"));
        pmsMessage.addField(new PMSField("03", strDate));
        pmsMessage.addField(new PMSField("04", strTime));

        String TID = "SE200A";
        String MID = "33123433";

        String DID = CommonUtils.rightPadding(TID, ' ', 8) + CommonUtils.rightPadding(MID, ' ', 15);

        pmsMessage.addField(new PMSField("EA", DID));

        String version = "EVO_12.0.0.3";
        pmsMessage.addField(new PMSField("VR", version));

        ipAddress = "192.168.43.5";
        String ipPOrt = "11005";
        pmsMessage.addField(new PMSField("IA", ipAddress));
        pmsMessage.addField(new PMSField("IP", ipPOrt));

//        byte[] m = pmsMessage.getMessage();
//
//        String c = String.valueOf(CommonUtils.calculateLrc(m, m.length));

        byte[] byteMsg = pmsMessage.getMessage();
        String msg = CommonUtils.byteArrayToHexString(byteMsg);
//
//        Inet4Address inet4Address=new Inet4Address()

        ClientThread clientThread = new ClientThread();
        new Thread(clientThread).start();


        binding.listenUDPServer.setOnClickListener(v -> {
            ListenUDP echoServer = new ListenUDP();
            echoServer.start();
        });

        binding.sendMsgToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PMS Message:", msg);
                clientThread.sendMessage(byteMsg);

            }
        });


        binding.createTerminal.setOnClickListener(v -> createTerminal());


    }

    private void createTerminal() {

        Observable.create((ObservableOnSubscribe<String>) emitter -> {

            DatagramSocket socket = new DatagramSocket(PMSUtil.DEFAULT_UDP_PORT);
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received
                    = new String(packet.getData(), 0, packet.getLength());
            socket.close();
            if (!emitter.isDisposed()) emitter.onNext(received);
            emitter.isDisposed();
        }).map(PMSUtil::getIpAddress)

                .flatMap((Function<String, ObservableSource<Boolean>>) s -> Observable.create(emitter -> {

                    InetAddress serverAddr = InetAddress.getByName(s);
                    Socket socket = new Socket(serverAddr, PMSUtil.DEFAULT_UDP_PORT);

                    OutputStream socketWriter = socket.getOutputStream();
                    System.out.println("Start sending " + s);

                    socketWriter.write(PMSUtil.getPMSMessage(PMSUtil.getLocalIpAddress()));
                    socketWriter.flush();
                    System.out.println("Send completed, start receiving information");


                    if (!emitter.isDisposed()) emitter.onNext(true);
                    emitter.isDisposed();


                }))

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean s) {

                        Log.e(TAG, "onNext: " + s);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.e(TAG, "onError: " + e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}