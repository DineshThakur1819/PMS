package com.pms;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;

import com.pms.pmsmodel.CommonUtils;
import com.pms.pmsmodel.PMSConstants;
import com.pms.pmsmodel.PMSField;
import com.pms.pmsmodel.PMSMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestStructureActivity extends AppCompatActivity {

    String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_structure);

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        PMSMessage pmsMessage = new PMSMessage(PMSConstants.PMSMessageType.TERMINAL_TEST, PMSConstants.RequestResponseType.RESPONSE, "00", false);

        /*e.setField("02", m_active ? "ECR COMMS - OK" : "TERMINAL BUSY");
        e.setField("03", date);
        e.setField("04", time);

        string mid = m_merchantId, tid = m_terminalId;
        e.setField("EA", StringUtility::padRight(tid, 8, ' ') + StringUtility::padRight(mid, 15, ' '));
        e.setField("VR", m_version);*/

        pmsMessage.addField(new PMSField("02", "ECR COMMS - OK"));

        SimpleDateFormat sdf = new SimpleDateFormat("YYMMdd");
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

//        String ipAddress = "192.168.1.1";
        String ipPOrt = "11002";
        pmsMessage.addField(new PMSField("IA", ipAddress));
        pmsMessage.addField(new PMSField("IP", ipPOrt));

        Log.d("PMS Message:", CommonUtils.byteArrayToHexString(pmsMessage.getMessage()));
    }
}