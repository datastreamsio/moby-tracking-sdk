package io.o2mc.sdk.util;

import android.util.Log;
import io.o2mc.sdk.BuildConfig;

/**
 * This class serves as a wrapper around the android.util.Log class.
 * It makes sure logs are only generated in the debug build variant.
 *
 * Putting the checks in this class instead of before every log individually reduces human-errors
 * (i.e. you might forget to add a check somewhere).
 *
 * This way, we can use the Android Studio search tool to make sure there is only one 'Log.e(...)'
 * statement in the entire project.
 *
 * Suggestion: It would be better to use ProGuard to remove logs in the release build variant.
 * This, however, has proven to require a lot of understanding about ProGuard and thus time consuming.
 */
// Suppressing JavaDoc warnings for methods because the explanation above is sufficient for all methods.
@SuppressWarnings("JavaDoc")
public class LogUtil {

  public static void LogE(final String TAG, String message) {
    if (BuildConfig.DEBUG) {
      Log.e(TAG, message);
    }
  }

  public static void LogE(final String TAG, String message, Throwable tr) {
    if (BuildConfig.DEBUG) {
      Log.e(TAG, message, tr);
    }
  }

  public static void LogW(final String TAG, String message) {
    if (BuildConfig.DEBUG) {
      Log.w(TAG, message);
    }
  }

  public static void LogW(final String TAG, String message, Throwable tr) {
    if (BuildConfig.DEBUG) {
      Log.w(TAG, message, tr);
    }
  }

  public static void LogI(final String TAG, String message) {
    if (BuildConfig.DEBUG) {
      Log.i(TAG, message);
    }
  }

  public static void LogI(final String TAG, String message, Throwable tr) {
    if (BuildConfig.DEBUG) {
      Log.i(TAG, message, tr);
    }
  }

  public static void LogD(final String TAG, String message) {
    if (BuildConfig.DEBUG) {
      Log.d(TAG, message);
    }
  }

  public static void LogD(final String TAG, String message, Throwable tr) {
    if (BuildConfig.DEBUG) {
      Log.d(TAG, message, tr);
    }
  }
}
