package io.o2mc.sdk.exceptions;

/**
 * Occurs on internal errors which are intended to be read by O2MC developers.
 */
// Suppress unused because the constructors are likely useful for future development.
@SuppressWarnings("unused") public class O2MCInternalException extends O2MCException {
  public O2MCInternalException(String message) {
    super(message);
  }

  O2MCInternalException(Throwable cause) {
    super(cause);
  }

  O2MCInternalException(String message, Throwable cause) {
    super(message, cause);
  }
}
