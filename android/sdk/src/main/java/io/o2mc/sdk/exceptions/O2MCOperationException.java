package io.o2mc.sdk.exceptions;

/**
 * Occurs on errors related to the tracking methods.
 */
// Suppress unused because the constructors are likely useful for future development.
@SuppressWarnings("unused") public class O2MCOperationException extends O2MCException {
  public O2MCOperationException(String message) {
    super(message);
  }

  public O2MCOperationException(Throwable cause) {
    super(cause);
  }

  public O2MCOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}
