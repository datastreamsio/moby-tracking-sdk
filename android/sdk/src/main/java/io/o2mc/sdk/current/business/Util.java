package io.o2mc.sdk.current.business;

import java.sql.Timestamp;

public class Util {
    public static Timestamp generateTimestamp() {
        long l = new java.util.Date().getTime();
        return new Timestamp(l);
    }
}
