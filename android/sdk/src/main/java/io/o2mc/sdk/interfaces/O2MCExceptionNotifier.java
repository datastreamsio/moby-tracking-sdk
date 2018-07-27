package io.o2mc.sdk.interfaces;

import io.o2mc.sdk.exceptions.O2MCException;

/**
 * Defines which exceptions the SDK can potentially throw.
 */
public interface O2MCExceptionNotifier {

  /**
   * Notifies the SDK user about an exception. This happens in a way the SDK user chose to do.
   * If they've set an exception listener, notify by throwing an exception there.
   * If they've not set an exception listener, log an error.
   *
   * @param e an exception with a message descriptive of what went wrong. Its type indicates at which subject the error occurred.
   * @param isFatal true if exception is fatal for base SDK functionality. SDK will then stop working.
   */
  void notifyException(O2MCException e, boolean isFatal);
}
