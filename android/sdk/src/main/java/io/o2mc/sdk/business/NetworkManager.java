package io.o2mc.sdk.business;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Class pulled from Github. (With some small changes)
 * It returns the current networkstate as a String
 */
@SuppressWarnings("WeakerAccess")
public class NetworkManager {

    /**
     * Get the network info
     *
     * @param context
     * @return null if unable to retrieve active network information
     */
    @SuppressWarnings("JavaDoc")
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //noinspection ConstantConditions -- null is being dealt with in calling methods
        return cm.getActiveNetworkInfo();
    }

    public static String getConnectivityType(Context context) {

        NetworkInfo info = NetworkManager.getNetworkInfo(context);
        int type = info.getType();
        int subType = info.getSubtype();

        switch (type) {
            case ConnectivityManager.TYPE_WIFI:
                return "WIFI";
            case ConnectivityManager.TYPE_MOBILE:
                switch (subType) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        return "1xRTT"; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        return "CDMA"; // ~ 14-64 kbps
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return "EDGE"; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        return "EVDO_0"; // ~ 400-1000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        return "EVDO_A"; // ~ 600-1400 kbps
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        return "GPRS"; // ~ 100 kbps
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        return "HSDPA"; // ~ 2-14 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        return "HSPA"; // ~ 700-1700 kbps
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        return "HSUPA"; // ~ 1-23 Mbps
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        return "UMTS"; // ~ 400-7000 kbps
                    /*
                     * Above API level 7, make sure to set android:targetSdkVersion
                     * to appropriate level to use these
                     */
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                        return "EHRPD"; // ~ 1-2 Mbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                        return "EVDO_B"; // ~ 5 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                        return "HSPAP"; // ~ 10-20 Mbps
                    case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                        return "IDEN"; // ~25 kbps
                    case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                        return "LTE"; // ~ 10+ Mbps
                    // Unknown
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    default:
                        return "unknown";
                }
            default:
                return "unknown";
        }
    }

}