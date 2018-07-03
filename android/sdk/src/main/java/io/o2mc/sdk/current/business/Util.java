package io.o2mc.sdk.current.business;

import android.util.Log;

import java.sql.Timestamp;

public class Util {

    private static final String TAG = "Util";

    /**
     * Generates a timestamp based on current time.
     *
     * @return timestamp in String format
     */
    public static String generateTimestamp() {
        long l = new java.util.Date().getTime();
        Timestamp t = new Timestamp(l);
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
            if (endpoint.charAt(4) != 's' && endpoint.charAt(4) != 'S') {
                Log.w(TAG, "validEndpointFormat: Endpoint is valid, but detected usage of HTTP instead of HTTPS. It is strongly recommended to use HTTPS in production usage.");
            }
            Log.d(TAG, String.format("validEndpointFormat: Valid web url '%s'", endpoint));
            return true;
        }

        String localUrlPattern = "^\\w{4,5}://\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b.*";
        boolean isValidLocalEndpoint = endpoint.matches(localUrlPattern);
        if (isValidLocalEndpoint) {
            Log.d(TAG, String.format("validEndpointFormat: Valid local url '%s'", endpoint));
            return true;
        }

        // No valid pattern matches found
        return false;
    }
}
