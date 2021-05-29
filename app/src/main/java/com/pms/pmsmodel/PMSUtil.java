package com.pms.pmsmodel;

public class PMSUtil {

    public static byte calculateLRC(byte[] bytes) {
        byte LRC = 0;
        for (int i = 0; i < bytes.length; i++) {
            LRC ^= bytes[i];
        }
        return LRC;
    }

}
