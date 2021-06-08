package com.pms.pmsmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class PMSUtil {

    public static final int DEFAULT_UDP_PORT = 11000;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private String APP_PREF_NAME = "asdgjsbdfvbcygefwvtrh";
    private static Context mContext;
    private static PMSUtil mInstance;

    public static String HICAPS_CONNECT = "HICAPSConnect";
    public static String LAPTOP = "laptop";
    public static String IP_PORT = "ipPort";


    public PMSUtil(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(APP_PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static synchronized PMSUtil getInstance() {
        mContext = PmsApplication.getContext();
        if (mInstance == null) {
            mInstance = new PMSUtil(mContext);
        }
        return mInstance;
    }

    public void saveValue(String key, String value) {
        if (value == null) {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public String getValue(String PREFS_KEY) {
        return pref.getString(PREFS_KEY, null);
    }

    public static byte calculateLRC(byte[] bytes) {
        byte LRC = 0;
        for (byte aByte : bytes) {
            LRC ^= aByte;
        }
        return LRC;
    }

    public static String getIpAddress(String s) {

        if (!TextUtils.isEmpty(s)) {
            String[] udpData = s.split("\\+");

            String[] ipAndPort = udpData[3].split(":");

            return ipAndPort[0];
        }

        return null;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] getPMSMessage(String ipAddress) {
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

//        ipAddress = "192.168.43.5";
        String ipPOrt = "11001";
        pmsMessage.addField(new PMSField("IA", ipAddress));
        pmsMessage.addField(new PMSField("IP", ipPOrt));

        return pmsMessage.getMessage();
    }
}
