package com.pms.pmsmodel;

public class PMSConstants {
    public static final byte[] SP = " ".getBytes();
    public static byte STX = 0x02;
    public static byte ETX = 0x03;
    public static byte FS = 0x1C;
    public static byte[] LL = "1".getBytes();
    public static byte[] LL1 = "32".getBytes();

    public enum RequestResponseType {
        REQUEST("0"),
        RESPONSE("1"),
        REQUEST_NO_EXPECTED_RESPONSE("2");

        private final String type;

        RequestResponseType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }


    public enum PMSMessageType {
        TERMINAL_TEST("D1");

        private final String type;

        PMSMessageType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}
