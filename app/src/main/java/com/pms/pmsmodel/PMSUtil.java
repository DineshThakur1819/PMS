package com.pms.pmsmodel;

public class PMSUtil {

    public static byte calculateLRC(byte[] bytes) {
        byte LRC = 0;
        for (byte aByte : bytes) {
            LRC ^= aByte;
        }
        return LRC;
    }

}
