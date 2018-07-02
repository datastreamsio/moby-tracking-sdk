package io.o2mc.sdk.current.business;

import java.sql.Timestamp;

public class Util {
    public static String generateTimestamp() {
        long l = new java.util.Date().getTime();
        Timestamp t = new Timestamp(l);
        return String.format("%tFT%<tTZ", t);
    }
}
