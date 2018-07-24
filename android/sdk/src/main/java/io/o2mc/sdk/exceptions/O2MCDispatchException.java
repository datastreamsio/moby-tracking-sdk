package io.o2mc.sdk.exceptions;

/**
 * Occurs on errors related to dispatching a batch.
 */
// Suppress unused because the constructors are likely useful for future development.
@SuppressWarnings("unused") public class O2MCDispatchException extends O2MCException {
  public O2MCDispatchException(String message) {
    super(message);
  }

  public O2MCDispatchException(Throwable cause) {
    super(cause);
  }

  public O2MCDispatchException(String message, Throwable cause) {
    super(message, cause);
  }
}
