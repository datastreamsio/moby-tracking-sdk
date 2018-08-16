package io.o2mc.sdk.util;

import android.security.NetworkSecurityPolicy;
import java.util.UUID;

import static io.o2mc.sdk.util.LogUtil.LogD;
import static io.o2mc.sdk.util.LogUtil.LogW;

public final class Util {

  private static final String TAG = "Util";

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
      LogW(TAG,
          "isValidDispatchInterval: Using an interval between 2 minutes and 1 hour between dispatching batches. This seems long. Are you sure you want to do this?");
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
    return endpoint != null && !endpoint.isEmpty() && endpoint.length() >= 5 && (endpoint.substring(
        4, 5).equals("s") || endpoint.substring(4, 5).equals("S"));
  }

  /**
   * Checks whether or not the provided endpoint is in valid format.
   * This does not check whether or not the endpoint actually exists / is online.
   *
   * @param endpoint URL pointing to a backend
   * @return true if the parameter is valid
   */
  public static boolean isValidEndpoint(String endpoint) {
    if (endpoint == null || endpoint.isEmpty()) {
      return false;
    }

    // Check if we have a valid web / online url
    String webUrlPattern =
        "(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$";

    boolean isValidWebEndpoint = endpoint.matches(webUrlPattern);
    if (isValidWebEndpoint) {
      // Check if using HTTP or HTTPS
      if (!isHttps(endpoint)) {
        LogW(TAG,
            "validEndpointFormat: Endpoint is valid, but detected usage of HTTP instead of HTTPS. It is strongly recommended to use HTTPS in production usage.");
      }
      LogD(TAG, "validEndpointFormat: Valid web url '%s'");
      return true;
    }

    String localUrlPattern = "^\\w{4,5}://\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b.*";
    boolean isValidLocalEndpoint = endpoint.matches(localUrlPattern);
    if (isValidLocalEndpoint) {
      LogD(TAG, "validEndpointFormat: Valid local url.");
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

    //noinspection SimplifiableIfStatement
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
      return NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted();
    }
    return true;
  }

  /**
   * Checks whether or not the max retries given is above 0.
   * Anything above is allowed. Some insanely high values may not make much sense, and will be given a warning, but allowed.
   *
   * @param maxRetries the amount of times to retry dispatching events
   * @return true if the max retries value is allowed
   */
  public static boolean isValidMaxRetries(int maxRetries) {
    if (maxRetries > 1000) {
      LogW(TAG, String.format(
          "isValidMaxRetries: Max retries '%s' is valid, but seems excessive. Are you sure you want to wait %s iterations before giving up on sending events?",
          maxRetries, maxRetries));
    }
    return maxRetries > 0;
  }

  /**
   * Securely generates a random ID.
   * https://stackoverflow.com/questions/24876188/how-big-is-the-chance-to-get-a-java-uuid-randomuuid-collision
   */
  public static String generateUUID() {
    return String.valueOf(UUID.randomUUID());
  }

  /**
   * Validates correctness of an event name.
   *
   * @param eventName the name to validate
   * @return true if event name is valid
   */
  public static boolean isValidEventName(String eventName) {
    // Must me non-empty
    if (eventName == null || eventName.isEmpty()) {
      return false;
    }

    // All conditions passed, event name is valid
    return true;
  }

  /**
   * Validates correctness of an event value.
   *
   * @param value the value of an event to validate
   * @return true if event value is valid
   */
  public static boolean isValidEventValue(Object value) {
    // Must me non-empty
    if (value == null) {
      return false;
    }

    // All conditions passed, event value is valid
    return true;
  }
}
