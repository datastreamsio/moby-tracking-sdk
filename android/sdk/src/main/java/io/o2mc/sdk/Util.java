package io.o2mc.sdk;

import android.annotation.SuppressLint;
import android.os.Build;
import android.security.NetworkSecurityPolicy;
import android.util.Log;

import java.sql.Timestamp;

public class Util {

    private static final String TAG = "Util";

    /**
     * Generates a timestamp based on current time.
     *
     * @return timestamp in String format
     */
    @SuppressLint("DefaultLocale")
    public static String generateTimestamp() {
        long l = new java.util.Date().getTime();
        Timestamp t = new Timestamp(l);
        // TODO: 7/3/18 research timestamp generation for a correct implementation
        return String.format("%tFT%<tTZ", t);
    }

    /**
     * Checks whether or not the provided interval is valid and reasonable.
     * This method will warn the user upon unreasonable usage, e.g. by mistaking seconds for milliseconds.
     *
     * @param seconds the amount of seconds to wait before each batch is dispatch
     * @return true if the provided interval is valid and reasonable
     */
    public static boolean isValidDispatchInterval(int seconds) {
        // Value must be positive
        if (seconds <= 0) {
            return false;
        }

        // Waiting an hour before dispatching is outrageous. Don't allow it.
        int oneHour = 60 * 60;
        if (seconds > oneHour) {
            return false;
        }

        // Waiting longer than 2 minutes to dispatch batches seems very long. The user will probably
        // have left the app by then. Allow it, but warn the developer.
        int twoMinutes = 2 * 60;
        if (seconds > twoMinutes) {
            Log.w(TAG, "isValidDispatchInterval: Using an interval between 2 minutes and 1 hour between dispatching batches. This seems long. Are you sure you want to do this?");
        }

        return true;
    }

    /**
     * Checks whether or not an endpoint is communicated over using HTTPS.
     *
     * @param endpoint backend endpoint
     * @return true on https
     */
    public static boolean isHttps(String endpoint) {
        if (endpoint == null || endpoint.isEmpty() || endpoint.length() < 5) {
            return false;
        }

        if (endpoint.substring(4, 5).equals("s") || endpoint.substring(4, 5).equals("S")) {
            return true;
        }

        return false;
    }

    /**
     * Checks whether or not the provided endpoint is in valid format.
     * This does not check whether or not the endpoint actually exists / is online.
     *
     * @param endpoint URL pointing to a backend
     * @return true if the parameter is valid
     */
    public static boolean isValidEndpoint(String endpoint) {
        // Check if we have a valid web / online url
        String webUrlPattern = "(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$";

        boolean isValidWebEndpoint = endpoint.matches(webUrlPattern);
        if (isValidWebEndpoint) {
            // Check if using HTTP or HTTPS
            if (!isHttps(endpoint)) {
                Log.w(TAG, "validEndpointFormat: Endpoint is valid, but detected usage of HTTP instead of HTTPS. It is strongly recommended to use HTTPS in production usage.");
            }
            Log.d(TAG, "validEndpointFormat: Valid web url '%s'");
            return true;
        }

        String localUrlPattern = "^\\w{4,5}://\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b.*";
        boolean isValidLocalEndpoint = endpoint.matches(localUrlPattern);
        if (isValidLocalEndpoint) {
            Log.d(TAG, "validEndpointFormat: Valid local url.");
            return true;
        }

        // No valid pattern matches found
        return false;
    }

    /**
     * Checks whether or not the client is allowed to dispatch events to the backend.
     *
     * @param usingHttpEndpoint indicates whether or not app is using the HTTPS protocol to dispatch events
     * @return true if this client is allowed to dispatch events
     */
    public static boolean isAllowedToDispatchEvents(boolean usingHttpEndpoint) {
        // Https traffic is always allowed
        if (usingHttpEndpoint) {
            return true;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            boolean allowed = NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted();
            if (!allowed) {
                Log.e(TAG, "isAllowedToDispatchEvents: Http traffic is not allowed on newer versions of the Android API. Please use HTTPS instead, or lower your min/target SDK version.");
            }
            return allowed;
        }

        return true;
    }
}
