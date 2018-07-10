package io.o2mc.sdk.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Helper class for handling a most common subset of ISO 8601 strings
 * (in the following format: "2008-03-01T13:00:00+01:00"). It supports
 * parsing the "Z" timezone, but many other less-used features are
 * missing.
 * <p>
 * See this link for the source:
 * https://stackoverflow.com/a/10621553/5273299
 */
public final class TimeUtil {

  // We can safely ignore this warning, because we're using an ISO standard. Android Studio does
  // not look at those, and just tells us to use a Java standard -- which is not necessarily
  // universal.
  @SuppressLint("SimpleDateFormat")
  private static final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

  /**
   * Transform Calendar to ISO 8601 string.
   */
  private static String fromCalendar(final Calendar calendar) {
    Date date = calendar.getTime();

    String formatted = isoFormat.format(date);

    return formatted.substring(0, 22) + ":" + formatted.substring(22);
  }

  /**
   * Get current date and time formatted as ISO 8601 string.
   */
  public static String generateTimestamp() {
    return fromCalendar(GregorianCalendar.getInstance());
  }
}
