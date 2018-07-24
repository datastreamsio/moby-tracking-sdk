package io.o2mc.sdk.exceptions;

/**
 * Occurs on errors related to the user's device.
 */
// Suppress unused because the constructors are likely useful for future development.
@SuppressWarnings("unused") public abstract class O2MCException extends Exception {
  public O2MCException(String message) {
    super(message);
  }

  public O2MCException(Throwable cause) {
    super(cause);
  }

  public O2MCException(String message, Throwable cause) {
    super(message, cause);
  }
}
