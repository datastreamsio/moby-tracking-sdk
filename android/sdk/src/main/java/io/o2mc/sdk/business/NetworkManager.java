package io.o2mc.sdk.business;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

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


}